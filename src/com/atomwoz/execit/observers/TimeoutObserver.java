package com.atomwoz.execit.observers;

import com.atomwoz.execit.base.AbstractObserver;
import com.atomwoz.execit.base.Controllable;

public class TimeoutObserver extends AbstractObserver
{

	public TimeoutObserver()
	{
		super(1, true);
	}

	@Override
	public void startObservation(Controllable commandToObserve, String params[])
	{
		try
		{
			// log("Starting");
			boolean secondFlag = false;
			String strTime = params[0];
			strTime = strTime.replace(',', '.');
			if (strTime.endsWith("ms"))
			{
				strTime = strTime.substring(0, strTime.length() - 2);
			}
			if (strTime.endsWith("s"))
			{
				strTime = strTime.substring(0, strTime.length() - 1);
				secondFlag = true;
			}
			float time = 0;
			try
			{
				time = Float.valueOf(strTime);
				time = secondFlag ? time * 1000 : time;
			} catch (NumberFormatException e)
			{
				error("Parametr must be a number !!!; optionaly ends with ms/s");
				return;
			}
			int rTime = Math.round(time);
			synchronized (Thread.currentThread())
			{
				Thread.currentThread().wait(rTime);
			}
			// log("Passed");
			if (commandToObserve.isAlive())
			{
				log("Task " + commandToObserve.getNiceName() + " was ended after " + rTime + "ms timeout");
				commandToObserve.stopImediatly();
			}

		}

		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

}
