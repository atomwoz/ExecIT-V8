package com.atomwoz.execit.commands;

import com.atomwoz.execit.base.CommandBase;
import com.atomwoz.execit.base.CommandRuntimeExcepiton;
import com.atomwoz.execit.base.StartArgue;
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
	public int doCommand(StartArgue args) throws CommandRuntimeExcepiton
	{
		long capacity = 0;
		if (args.getArgues().length >= 2)
		{
			var capacityCandidate = TerminalFunctions.stringToBytes(args.getArgues()[1], true);
			if (capacityCandidate != null)
			{
				capacity = capacityCandidate.longValue();
			}
			else
			{
				return 1;
			}
		}
		String volName = args.getArgues()[0];
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
