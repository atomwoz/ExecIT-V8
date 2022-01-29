package com.atomwoz.execit.commands;

import com.atomwoz.execit.base.CommandBase;
import com.atomwoz.execit.base.CommandRuntimeExcepiton;
import com.atomwoz.execit.base.StartArgue;

public class YesCommand extends CommandBase
{
	public YesCommand(Thread thread, String name)
	{
		super(thread, name);
	}

	@Override
	public int doCommand(StartArgue args) throws CommandRuntimeExcepiton
	{
		String argue = args.getArgueLine();
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
