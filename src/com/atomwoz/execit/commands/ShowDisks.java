package com.atomwoz.execit.commands;

import java.io.IOException;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.util.Formatter;
import java.util.Set;

import com.atomwoz.execit.base.CommandBase;
import com.atomwoz.execit.base.CommandRuntimeExcepiton;
import com.atomwoz.execit.base.StartFlag;
import com.atomwoz.execit.base.TerminalFunctions;
import com.atomwoz.execit.virtual.VirtualDiskRegister;

public class ShowDisks extends CommandBase
{
	public ShowDisks(Thread thread, String name)
	{
		super(thread, name);
	}

	@Override
	public int doCommand(String command, String[] argues, String argue, Set<StartFlag> startFlags)
			throws CommandRuntimeExcepiton
	{
		try (Formatter format = new Formatter())
		{
			boolean all = false;
			if (argues.length > 0)
			{
				if (argue.equals("all") || argue.equals("everything"))
				{
					all = true;
				}
				else
				{
					io.printError(
							argue + " is not recognize as argument, possible argues are \"all\" and \"everything\"");
					return 1;
				}
			}

			// Real volumes

			println("");
			printlnBold(" System drives ");
			FileSystem def = FileSystems.getDefault();
			var stores = def.getFileStores();
			int max = 15;
			if (stores != null)
			{
				for (var drive : stores)
				{
					max = Math.max(drive.toString().length() + 3, max);
				}
				if (max > 50)
				{
					max = 50;
				}
				for (var drive : stores)
				{
					try
					{
						float freeSpace = drive.getUsableSpace();
						float totalSpace = drive.getTotalSpace();
						double procentallocatedSpace = Math.round(10000 - ((freeSpace / totalSpace) * 10000)) / 100d;
						printf("  %-" + max + "s %s %n", drive.toString(), "[" + drive.type() + "]");
						println("\tCurrent size: "
								+ TerminalFunctions.simplifyMemory(totalSpace - freeSpace, false, false) + " ("
								+ procentallocatedSpace + "%)");
						println("\tVolume size: " + TerminalFunctions.simplifyMemory(totalSpace, false, false));
						println("");
					}
					catch (IOException e)
					{
						io.printError("While showing disk " + drive + " check that you have acces to it");
					}
				}
			}

			// VIrtual Volumes
			if (!VirtualDiskRegister.isEmpty())
			{
				printlnBold(" Virtual drives ");

				for (var vv : VirtualDiskRegister.getStructure().entrySet())
				{
					boolean showCapacity = false;
					long maxSpace = vv.getValue().getRoot().getMaxSize();
					long totalSpace = vv.getValue().getRoot().getSize();
					long freeSpace = maxSpace - totalSpace;
					double procentallocatedSpace = 0;
					if (maxSpace > 0)
					{
						showCapacity = true;
						if (totalSpace != 0)
						{
							procentallocatedSpace = Math.round(10000 - (freeSpace / totalSpace) * 10000) / 100d;
						}

					}

					println(vv.getKey() + " [-]");
					println("\tVolume size: " + TerminalFunctions.simplifyMemory(totalSpace, false, false)
							+ (showCapacity
									? (" / " + TerminalFunctions.simplifyMemory(maxSpace, false, false) + " ("
											+ procentallocatedSpace + "%)")
									: ""));
					println("\t" + vv.getValue().getRoot().getCount() + " elements");
				}
			}
			println("");

			// TODO Show all info
		}

		return 0;
	}

}
