package com.atomwoz.execit.commands;

import java.nio.charset.Charset;
import java.util.Set;

import com.atomwoz.execit.base.CommandBase;
import com.atomwoz.execit.base.CommandRuntimeExcepiton;
import com.atomwoz.execit.base.StartFlag;

public class TokenizeTest extends CommandBase
{

	public TokenizeTest(Thread thread, String name)
	{
		super(thread, name);
	}

	@Override
	public int doCommand(String command, String[] argues, String argue, Set<StartFlag> startFlags)
			throws CommandRuntimeExcepiton
	{
		println(Charset.availableCharsets().toString());
		printInfo("Super ważna informacja");
		printWarning("Uwaga !!!");
		println("Command " + command);
		for (long l = 8000000000L; l > 0; l--)
		{
			var x = l;
		}
		println("---------------");
		for (var a : argues)
		{
			println(a);
		}
		println("----------------");
		println("Alline " + command + " " + argue);
		println("Argue " + argue);
		return 0;

	}

}
