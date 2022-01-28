package com.atomwoz.execit.commands;

import java.util.Set;

import com.atomwoz.execit.base.CommandBase;
import com.atomwoz.execit.base.CommandRuntimeExcepiton;
import com.atomwoz.execit.base.StartFlag;

public class Print extends CommandBase
{

	public Print(Thread thread, String name)
	{
		super(thread, name);
	}

	@Override
	public int doCommand(String command, String[] argues, String argue, Set<StartFlag> startFlags)
			throws CommandRuntimeExcepiton
	{
		println(argue);
		return 0;
	}

}
