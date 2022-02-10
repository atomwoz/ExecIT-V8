package com.atomwoz.execit.commands;

import java.io.File;
import java.io.IOException;

import com.atomwoz.execit.base.CommandBase;
import com.atomwoz.execit.base.CommandRuntimeExcepiton;
import com.atomwoz.execit.base.StartArgue;
import com.atomwoz.execit.pathEngine.FileName;
import com.atomwoz.execit.pathEngine.PathEngine;

public class LsCommand extends CommandBase
{

	public LsCommand(Thread thread, String name, Integer ID)
	{
		super(thread, name, ID);
	}

	@Override
	public int doCommand(StartArgue argues) throws CommandRuntimeExcepiton
	{
		FileName whereRead;
		if (argues.getArgueLine().isBlank())
		{
			whereRead = PathEngine.getLoc();
		}
		else
		{
			try
			{
				whereRead = PathEngine.pathToFileName(argues.getArgueLine());
			}
			catch (IOException e1)
			{
				error(e1.getMessage());
				return 1;
			}
		}
		switch (whereRead.getType())
		{
			case PHYSICAL_FILE:

				String name = "";
				File[] files = new File(whereRead.getFileFullPath()).listFiles();
				if (files.length < 1)
				{
					println("Directory is empty");
				}
				if (files.length >= 10)
				{
					println();
				}
				for (var x : files)
				{
					name = x.getName();
					if (name.contains(" "))
					{
						name = "\"" + name + "\"";
					}
					print(name + "   ");
				}
				println();
				if (files.length >= 10)
				{
					println();
				}
			break;
			case VIRTUAL_FILE:
			case WEB_FILE:
			default:
		}
		return 0;
	}

}
