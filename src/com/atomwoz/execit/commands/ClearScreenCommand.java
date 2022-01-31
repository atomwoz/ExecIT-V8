package com.atomwoz.execit.commands;

import com.atomwoz.execit.base.CommandBase;
import com.atomwoz.execit.base.CommandRuntimeExcepiton;
import com.atomwoz.execit.base.StartArgue;
import com.atomwoz.execit.base.TerminalFunctions;

public class ClearScreenCommand extends CommandBase
{
	public ClearScreenCommand(Thread thread, String name, Integer ID)
	{
		super(thread, name, ID);
		maximumArgues = 0;
	}

	@Override
	public int doCommand(StartArgue argues) throws CommandRuntimeExcepiton
	{
		TerminalFunctions.clearScreen();
		return 0;
	}

}
