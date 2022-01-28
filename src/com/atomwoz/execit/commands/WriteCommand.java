package com.atomwoz.execit.commands;

import java.util.Set;

import com.atomwoz.execit.base.CommandBase;
import com.atomwoz.execit.base.CommandRuntimeExcepiton;
import com.atomwoz.execit.base.StartFlag;
import com.atomwoz.execit.pathEngine.FileName;
import com.atomwoz.execit.pathEngine.PathEngine;
import com.atomwoz.execit.virtual.VirtualDiskException;
import com.atomwoz.execit.virtual.VirtualDiskRegister;
import com.atomwoz.execit.virtual.VolumeStorageLimitExceesed;

public class WriteCommand extends CommandBase
{

	public WriteCommand(Thread thread, String name)
	{
		super(thread, name);
		minimumArgues = 2;
	}

	@Override
	public int doCommand(String command, String[] argues, String argue, Set<StartFlag> startFlags)
			throws CommandRuntimeExcepiton
	{
		String toWrite = argues[0];
		FileName whereWrite = PathEngine.pathToFileName(argues[1]);

		switch (whereWrite.getType())
		{
			case VIRTUAL_FILE:
				try
				{
					VirtualDiskRegister.getFileByAbsoluteName(whereWrite.getFileFullPath(), true).write(toWrite);
					echo("Writed " + toWrite.length() + " bytes to " + whereWrite.getFileFullPath() + " virtual path");
				}
				catch (VolumeStorageLimitExceesed e)
				{
					error("The content of file you want to write is too big for this folder, file or volume");
					return 1;
				}
				catch (VirtualDiskException e)
				{
					String cause = e.getMessage();
					if (cause.isBlank())
					{
						error("The file you want to write to is not accessible.");
					}
					else
					{
						error(e.getMessage());
					}
					return 2;
				}
				break;
			case DEVICE:
				break;
			case PHYSICAL_FILE:
				break;
			case WEB_FILE:
				break;
			default:
				break;
			// FIXME Inne opcje zapisywania i potestować
		}
		return 0;
	}

}
