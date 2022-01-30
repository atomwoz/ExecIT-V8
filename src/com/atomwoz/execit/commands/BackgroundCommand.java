package com.atomwoz.execit.commands;

import com.atomwoz.execit.base.CommandBase;
import com.atomwoz.execit.base.CommandRuntimeExcepiton;
import com.atomwoz.execit.base.GlobalControllableRegister;
import com.atomwoz.execit.base.StartArgue;

public class BackgroundCommand extends CommandBase
{

	public BackgroundCommand(Thread thread, String name, Integer ID)
	{
		super(thread, name, ID);
	}

	@Override
	public int doCommand(StartArgue argues) throws CommandRuntimeExcepiton
	{
		var tab = GlobalControllableRegister.getTable();
		println();
		println("   ID    │         NAME         │  RUNNING?  ");
		println("─────────┼──────────────────────┼────────────");
		for (var a : tab.entrySet())
		{

			printf("%-5s│%-20s│  %s %n", center(9, a.getKey()), center(22, a.getValue().getNiceName()),
					(a.getValue().isAlive() ? "yes" : "no"));
		}
		println();
		GlobalControllableRegister.update();
		return 0;
	}

	private String center(int cols, Object obj)
	{
		String str = obj.toString();
		int len = str.length();
		int paddLen = (cols - len) / 2;
		int paddLen2 = cols - len - paddLen;
		if (paddLen < 0)
		{
			paddLen = 0;
		}
		if (paddLen2 < 0)
		{
			paddLen2 = 0;
		}
		String padding = " ".repeat(paddLen);
		String padding2 = " ".repeat(paddLen2);
		return padding + str + padding2;
	}

}
