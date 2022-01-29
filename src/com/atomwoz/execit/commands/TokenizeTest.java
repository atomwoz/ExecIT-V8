package com.atomwoz.execit.commands;

import java.nio.charset.Charset;

import com.atomwoz.execit.base.CommandBase;
import com.atomwoz.execit.base.CommandRuntimeExcepiton;
import com.atomwoz.execit.base.StartArgue;

public class TokenizeTest extends CommandBase
{

	public TokenizeTest(Thread thread, String name)
	{
		super(thread, name);
	}

	@Override
	public int doCommand(StartArgue args) throws CommandRuntimeExcepiton
	{
		String command = args.getCommand();
		String[] argues = args.getArgues();
		String argue = args.getArgueLine();
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
