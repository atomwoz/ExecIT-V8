package com.atomwoz.execit.base;

public class ShutdownHook extends Thread
{
	BasicIO bio = BasicIO.getInstance();

	@Override
	public void run()
	{

		if (RuntimeInfo.isWorking())
		{
			bio.printInfo("Killing " + RuntimeInfo.getForegroundName() + "....");
			try
			{
				Thread.sleep(300);
			}
			catch (InterruptedException e)
			{
				Thread.yield();
			}
			Thread.yield();
		}
		printBye();

	}

	void printBye()
	{
		bio.println("Thanks for using Exec-it shell");
	}
}
