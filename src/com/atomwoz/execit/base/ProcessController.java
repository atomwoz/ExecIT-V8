package com.atomwoz.execit.base;

import java.io.IOException;

/**
 * This is a class contains useful methods to work with system processes
 * 
 * @author atomwoz
 *
 */
public class ProcessController
{

	/**
	 * This method checks that system can run given commands
	 * 
	 * @param commands - List of commands to check
	 * @return First command that not failed or null if all failed
	 */
	public static String checkSystemCommands(String... commands)
	{
		ProcessBuilder pb;
		for (String command : commands)
		{
			pb = new ProcessBuilder(command);
			try
			{
				pb.start();
				return command;
			}
			catch (IOException e)
			{
			}
		}
		return null;
	}

	public static Process startProcess(String... args) throws IOException
	{
		ProcessBuilder pb = new ProcessBuilder(args);
		return pb.start();
	}

	public static Process startProcessInConsole(String... args) throws IOException
	{
		ProcessBuilder pb = new ProcessBuilder(args);
		pb.inheritIO();
		return pb.start();

	}

	public static Process startSystemCommand(boolean async, String... args) throws IOException
	{

		int START_ARGS_SIZE = 2;

		SystemInfo sysinfo = SystemInfo.getInfoFromSystem();
		String firstArgs[] = new String[START_ARGS_SIZE];
		String readyArgs[] = new String[args.length + START_ARGS_SIZE];
		firstArgs[0] = sysinfo.getDefaultShell();
		firstArgs[1] = sysinfo.isWindows() ? "/c" : "-c";
		for (int i = 0; i < args.length + START_ARGS_SIZE; i++)
		{
			if (i < START_ARGS_SIZE)
			{
				readyArgs[i] = firstArgs[i];
			}
			else
			{
				readyArgs[i] = args[i - START_ARGS_SIZE];
			}
		}

		ProcessBuilder pb = new ProcessBuilder(readyArgs);
		pb.inheritIO();
		if (async)
		{
			return pb.start();
		}
		else
		{
			Process p = pb.start();
			try
			{
				p.waitFor();
			}
			catch (InterruptedException e)
			{
			}
			return p;
		}

	}

	public static int enterSystemShell(boolean appendHeaderIO) throws IOException
	{
		SystemInfo sysinfo = SystemInfo.getInfoFromSystem();
		ProcessBuilder pb = new ProcessBuilder(sysinfo.getDefaultShell());
		pb.inheritIO();
		try
		{

			return pb.start().waitFor();

		}
		catch (InterruptedException e)
		{
			BasicIO.getInstance().printError("Shell interupted exiting...");
			return -1;
		}

	}

	/**
	 * Equal to system() in C/C++
	 * 
	 * @param command - command to execute
	 * @throws IOException - throwed from Proccess.start()
	 * @return Spawned process
	 */
	public static Process executeSystemCommand(String command) throws IOException
	{
		var params = Executor.tokenizeInput(command);
		String args[] = new String[params.size()];
		args = params.toArray(args);
		return startSystemCommand(false, args);

	}
}
