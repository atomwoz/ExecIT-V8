package com.atomwoz.execit.commands;

import java.util.Set;

import com.atomwoz.execit.base.CommandBase;
import com.atomwoz.execit.base.CommandRuntimeExcepiton;
import com.atomwoz.execit.base.StartFlag;
import com.atomwoz.execit.base.TerminalFunctions;
import com.atomwoz.execit.virtual.VirtualDiskRegister;

public class MakeVolumeCommand extends CommandBase
{

	public MakeVolumeCommand(Thread thread, String name)
	{
		super(thread, name);
		minimumArgues = 1;
		maximumArgues = 2;
	}

	@Override
	public int doCommand(String command, String[] argues, String argue, Set<StartFlag> startFlags)
			throws CommandRuntimeExcepiton
	{
		long capacity = 0;
		if (argues.length >= 2)
		{
			var capacityCandidate = TerminalFunctions.stringToBytes(argues[1], true);
			if (capacityCandidate != null)
			{
				capacity = capacityCandidate.longValue();
			}
			else
			{
				return 1;
			}
		}
		String volName = argues[0];
		if (volName.isBlank())
		{
			io.printError("Volume name cannot be empty");
			return 1;
		}
		if (volName.startsWith("%") && volName.length() >= 1)
		{
			volName = volName.substring(1);
		}
		VirtualDiskRegister.createAndMount(volName, capacity);
		echo("Created virtual volume, named " + volName + (capacity > 0 ? (", max size " + capacity + "B") : ""));
		return 0;
	}

}
