package com.atomwoz.execit.commands;

import java.util.Set;

import com.atomwoz.execit.base.CommandBase;
import com.atomwoz.execit.base.CommandRuntimeExcepiton;
import com.atomwoz.execit.base.StartFlag;

public class YesCommand extends CommandBase
{
	public YesCommand(Thread thread, String name)
	{
		super(thread, name);
	}

	@Override
	public int doCommand(String command, String[] argues, String argue, Set<StartFlag> startFlags)
			throws CommandRuntimeExcepiton
	{
		if (!argue.isBlank())
		{
			while (true)
			{
				println(argue);
			}
		}
		else
		{
			while (true)
			{
				println("yes");
			}
		}

	}

}
