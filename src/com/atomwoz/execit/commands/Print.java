package com.atomwoz.execit.commands;

import com.atomwoz.execit.base.CommandBase;
import com.atomwoz.execit.base.CommandRuntimeExcepiton;
import com.atomwoz.execit.base.StartArgue;

public class Print extends CommandBase
{

	public Print(Thread thread, String name)
	{
		super(thread, name);
	}

	@Override
	public int doCommand(StartArgue args) throws CommandRuntimeExcepiton
	{
		println(args.getArgueLine());
		return 0;
	}

}
