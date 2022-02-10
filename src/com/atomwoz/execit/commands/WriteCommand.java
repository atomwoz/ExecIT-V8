package com.atomwoz.execit.commands;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import com.atomwoz.execit.base.CommandBase;
import com.atomwoz.execit.base.CommandRuntimeExcepiton;
import com.atomwoz.execit.base.StartArgue;
import com.atomwoz.execit.pathEngine.FileName;
import com.atomwoz.execit.pathEngine.PathEngine;
import com.atomwoz.execit.pathEngine.PathEngine.FileTypes;
import com.atomwoz.execit.virtual.VirtualDiskException;
import com.atomwoz.execit.virtual.VirtualDiskRegister;
import com.atomwoz.execit.virtual.VolumeStorageLimitExceesed;

public class WriteCommand extends CommandBase
{

	public WriteCommand(Thread thread, String name, Integer commandID)
	{
		super(thread, name, commandID);
		minimumArgues = 2;
		maximumArgues = 2;
	}

	@Override
	public int doCommand(StartArgue args) throws CommandRuntimeExcepiton
	{
		String toWrite = args.getArgues()[0];
		FileName whereWrite;
		try
		{
			whereWrite = PathEngine.pathToFileName(args.getArgues()[1]);
		}
		catch (IOException e1)
		{
			error(e1.getMessage());
			return 1;
		}
		Charset charset = Charset.forName("UTF-8");
		boolean doNotCreate = args.hasFlag("no-create", "nc");
		if (doNotCreate)
		{
			echo("Disabled file creation");
		}
		if (args.getArgues().length > 2)
		{
			try
			{
				charset = Charset.forName(args.getArgues()[2]);
			}
			catch (Exception e)
			{
				error("Sorry but enconding " + args.getArgues()[1] + " is not known");
				return 3;
			}
		}
		switch (whereWrite.getType())
		{
			case VIRTUAL_FILE:
				try
				{
					VirtualDiskRegister.getFileByAbsoluteName(whereWrite.getFileFullPath(), !doNotCreate)
							.write(toWrite);
					echo("Writed " + toWrite.length() + " bytes to " + whereWrite.getFileFullPath() + " virtual path");
					return 0;
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
			case DEVICE:
				break;
			case PHYSICAL_FILE:
				Path path = null;
				try
				{
					if (doNotCreate)
					{
						path = Paths.get(PathEngine.resolvePath(whereWrite.getFileFullPath(), FileTypes.FILE));
					}
					else
					{
						path = Paths.get(PathEngine.resolvePathForce(whereWrite.getFileFullPath(), FileTypes.FILE));
					}
				}
				catch (IOException e1)
				{
					error(e1.getMessage());
					return 2;
				}
				try
				{
					Files.writeString(path, toWrite, charset, (doNotCreate ? null : StandardOpenOption.CREATE),
							StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.WRITE);
					echo("Writed " + toWrite.length() + " bytes to " + whereWrite.getFileFullPath()
							+ " real path, with encoding " + charset.displayName());
					return 0;
				}
				catch (IOException e)
				{
					error(e.getMessage());
					return 3;
				}
				catch (Exception e)
				{
					error("I can't write content to fille located in: " + path.toString());
					return 2;
				}
			case WEB_FILE:
				error("You cannot write to remote file");
				return 4;
			default:
				break;
		}
		return 10;
	}

}
