package com.atomwoz.execit.commands;

import java.io.IOException;

import com.atomwoz.execit.base.CommandBase;
import com.atomwoz.execit.base.CommandRuntimeExcepiton;
import com.atomwoz.execit.base.StartArgue;
import com.atomwoz.execit.pathEngine.PathEngine;

public class PwdCommand extends CommandBase
{

	public PwdCommand(Thread thread, String name, Integer commandID)
	{
		super(thread, name, commandID);
	}

	@Override
	public int doCommand(StartArgue argues) throws CommandRuntimeExcepiton
	{
		try
		{
			String loc = PathEngine.pwd(argues.getArgueLine());
			println(loc);
		}
		catch (IOException e)
		{
			error(e.getMessage());
			return 1;
		}
		return 0;
	}

}
