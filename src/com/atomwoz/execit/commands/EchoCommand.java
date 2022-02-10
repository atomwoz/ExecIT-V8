package com.atomwoz.execit.commands;

import com.atomwoz.execit.base.BasicIO;
import com.atomwoz.execit.base.CommandBase;
import com.atomwoz.execit.base.CommandRuntimeExcepiton;
import com.atomwoz.execit.base.StartArgue;

public class EchoCommand extends CommandBase
{

	public EchoCommand(Thread thread, String name, Integer ID)
	{
		super(thread, name, ID);
	}

	@Override
	public int doCommand(StartArgue argues) throws CommandRuntimeExcepiton
	{
		BasicIO io = BasicIO.getInstance();
		if (argues.getArgues().length == 0)
		{
			println("Echo is " + (io.isEchoing() ? "enabled" : "disabled"));
		}
		else
		{
			String line = argues.getArgueLine().toLowerCase();
			if (line.equals("on") || line.equals("enable"))
			{
				io.enableEcho();
				echo("Enabled echoing");
			}
			else if (line.equals("off") || line.equals("disable"))
			{
				io.disableEcho();
				echo("Disabled echoing");
			}
			else
			{
				error("Not known ECHO option, possible are ON or OFF");
			}
		}
		return 0;
	}

}
