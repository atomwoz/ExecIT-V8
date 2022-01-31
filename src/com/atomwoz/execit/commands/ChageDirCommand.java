package com.atomwoz.execit.commands;

import java.io.IOException;

import com.atomwoz.execit.base.CommandBase;
import com.atomwoz.execit.base.CommandRuntimeExcepiton;
import com.atomwoz.execit.base.StartArgue;
import com.atomwoz.execit.pathEngine.PathEngine;

public class ChageDirCommand extends CommandBase
{

	public ChageDirCommand(Thread thread, String name, Integer ID)
	{
		super(thread, name, ID);
		minimumArgues = 1;
		maximumArgues = 1;
	}

	@Override
	public int doCommand(StartArgue argues) throws CommandRuntimeExcepiton
	{
		try
		{
			PathEngine.changeDirectory(argues.getArgues()[0]);
		}
		catch (IOException e)
		{
			error(e.getMessage());
			return 1;
		}
		return 0;
	}

}
