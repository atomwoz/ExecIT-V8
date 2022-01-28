package com.atomwoz.execit.base;

import java.io.IOException;

/**
 * This is base abstract class for every Exec it! command implements Command
 * interface without declaring doSomething(params ...)
 * 
 * @author atwoz
 * 
 */
public abstract class CommandBase extends Controllable implements Command
{

	protected BasicIO io;
	private Thread commandThread = null;
	public String commandName = null;
	/**
	 * Set to true when child class can't write and read from standard IO
	 */
	private boolean isMuted = false;
	/**
	 * How many argues this command requires (-1) when it can run on any argues
	 */
	protected int minimumArgues = -1;
	protected int maximumArgues = Integer.MAX_VALUE;

	protected String help = "";
	protected String shortHelp = "";
	protected String man = "";

	/**
	 * 
	 */
	StartMode startMode = StartMode.BOTH;

	public CommandBase(Thread thread, String name)
	{
		io = BasicIO.getInstance();
		commandThread = thread;
		commandName = name;

	}

	protected boolean isIOMuted()
	{
		return isMuted;
	}

	protected void muteIO()
	{
		isMuted = true;
	}

	/**
	 * Reading line from stdin, if IO muted, return null
	 * 
	 * @return readed line or null if IO was muted
	 * @throws IOException When IO Exception like null reader or something
	 */
	protected String readLine() throws IOException
	{
		if (!isMuted)
		{
			return io.readLine();
		}
		else
		{
			return null;
		}
	}

	/**
	 * Printing to stdout without endline
	 * 
	 * @param str to print
	 */
	protected void print(String str)
	{
		if (!isMuted)
		{
			io.print(str);
		}
	}

	/**
	 * Printing to stdout with endline
	 * 
	 * @param str to print
	 */
	protected void println(String str)
	{
		if (!isMuted)
		{
			io.println(str);
		}
	}

	protected void println()
	{
		if (!isMuted)
		{
			io.println("");
		}
	}

	protected void echo(String message)
	{
		io.echo(message);
	}

	protected void printf(String str, Object... args)
	{
		io.getOutput().printf(str, args);
	}

	protected void printInfo(String str)
	{
		if (!isMuted)
		{
			io.printInfo(str);
		}
	}

	protected void printWarning(String str)
	{
		if (!isMuted)
		{
			io.printWarning(str);
		}
	}

	protected void printSuccess(String str)
	{
		if (!isMuted)
		{
			io.printSuccess(str);
		}
	}

	protected void printlnBold(String str)
	{
		if (!isMuted)
		{
			io.printlnBold(str);
		}
	}

	/**
	 * Printing error on stderr stream, on rich ANSI terminals it is in red color
	 * 
	 * @param message Error message to print it should be without ERROR on begin
	 */
	protected void error(String message)
	{
		io.printError(message);
	}

	protected Process system(String command) throws IOException
	{
		return ProcessController.executeSystemCommand(command);
	}

	protected Thread getThread()
	{
		return commandThread;
	}

	@Override
	public String getNiceName()
	{
		return commandName;
	}

	@Override
	public boolean stop()
	{
		commandThread.interrupt();
		return false;
	}

	@Override
	public boolean isAlive()
	{
		return commandThread.isAlive();
	}

	@Override
	public boolean stopImediatly()
	{
		commandThread.stop();
		return false;
	}
}
