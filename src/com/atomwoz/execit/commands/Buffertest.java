package com.atomwoz.execit.commands;

import com.atomwoz.execit.base.CommandBase;
import com.atomwoz.execit.base.CommandRuntimeExcepiton;
import com.atomwoz.execit.base.IOConcurrencyUtills;
import com.atomwoz.execit.base.StartArgue;

public class Buffertest extends CommandBase
{

	public Buffertest(Thread thread, String name, Integer ID)
	{
		super(thread, name, ID);
	}

	@Override
	public int doCommand(StartArgue argues) throws CommandRuntimeExcepiton
	{
		for (var x : IOConcurrencyUtills.getTable())
		{
			println(x);
		}
		return 0;
	}

}
