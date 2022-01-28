package com.atomwoz.execit.base;

public abstract class AbstractObserver implements CommandObserver
{
	private int arguesRequired;
	private boolean startInSeparateThread;
	protected Thread watchedThread;
	private Thread currentThread;
	protected BasicIO io = BasicIO.getInstance();
	
	public AbstractObserver(int arguesRequired, boolean startInSeparateThread)
	{
		this.arguesRequired = arguesRequired;
		this.startInSeparateThread = startInSeparateThread;
	}
	
	protected void log(String toPrint)
	{
		io.printObserverInfo(toPrint);
	}
	protected void echo(String toEcho)
	{
		io.echo(toEcho);
	}
	protected void error(String message)
	{
		io.printObserverError(message);
	}
	public int getArguesRequired()
	{
		return arguesRequired;
	}

	public boolean isStartInSeparateThread()
	{
		return startInSeparateThread;
	}

	public boolean setUpObserverThread(Thread thread)
	{
		if(currentThread == null)
		{
			currentThread = thread;
			return true;
		}
		else
		{
			return false;
		}
	}
	
	public Thread getCurrentThread()
	{
		return currentThread;
	}
	
	

	
	
}
