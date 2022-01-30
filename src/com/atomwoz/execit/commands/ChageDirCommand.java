package com.atomwoz.execit.commands;

import com.atomwoz.execit.base.CommandBase;
import com.atomwoz.execit.base.CommandRuntimeExcepiton;
import com.atomwoz.execit.base.StartArgue;

public class ChageDirCommand extends CommandBase
{

	public ChageDirCommand(Thread thread, String name, Integer ID)
	{
		super(thread, name, ID);
	}

	@Override
	public int doCommand(StartArgue argues) throws CommandRuntimeExcepiton
	{

		return 0;
	}

}
