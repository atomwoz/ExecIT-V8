package com.atomwoz.execit.commands;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Set;

import com.atomwoz.execit.base.CommandBase;
import com.atomwoz.execit.base.CommandRuntimeExcepiton;
import com.atomwoz.execit.base.StartFlag;
import com.atomwoz.execit.net.NetWrapper;
import com.atomwoz.execit.pathEngine.FileName;
import com.atomwoz.execit.pathEngine.PathEngine;
import com.atomwoz.execit.virtual.VirtualDiskException;
import com.atomwoz.execit.virtual.VirtualDiskRegister;

public class ReadCommand extends CommandBase
{

	public ReadCommand(Thread thread, String name)
	{
		super(thread, name);
		minimumArgues = 1;
	}

	@Override
	public int doCommand(String command, String[] argues, String argue, Set<StartFlag> startFlags)
			throws CommandRuntimeExcepiton
	{
		FileName whereRead = PathEngine.pathToFileName(argues[0]);
		Charset charset = Charset.forName("UTF-8");
		if (argues.length > 1)
		{
			try
			{
				charset = Charset.forName(argues[1]);
			}
			catch (Exception e)
			{
				error("Sorry but enconding " + argues[1] + " is not known");
				return 3;
			}
		}
		switch (whereRead.getType())
		{
			case VIRTUAL_FILE:
				try
				{
					String data = VirtualDiskRegister.getFileByAbsoluteName(whereRead.getFileFullPath(), false)
							.readAll();
					println(data);
					echo("Readed " + data.length() + " chars from " + whereRead.getFileFullPath() + " virtual path");
				}
				catch (VirtualDiskException e)
				{
					String cause = e.getMessage();
					if (cause.isBlank())
					{
						error("The file you want to read is not accessible.");
					}
					else
					{
						error(e.getMessage());
					}
					return 2;
				}
				break;
			case PHYSICAL_FILE:
				Path path = Path.of(whereRead.getFileFullPath());
				try
				{
					var lines = Files.readAllLines(path, charset);
					println();
					for (var line : lines)
					{
						println(line);
					}
					println();
				}
				catch (IOException e)
				{
					error("I can't read content of fille located in: " + path.toString());
					return 2;
				}
			case WEB_FILE:

				String[] lines;
				String loc = whereRead.getProtocol() + "://" + whereRead.getName() + whereRead.getExtension();
				try
				{
					lines = NetWrapper.readRemoteContent(loc);
					println();
					for (var line : lines)
					{
						println(line);
					}
					println();
				}
				catch (IOException e)
				{
					error("I can't read remote file located at: " + loc);
				}
			default:
				break;
		}
		return 0;
	}

}
