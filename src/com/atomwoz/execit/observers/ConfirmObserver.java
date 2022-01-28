package com.atomwoz.execit.observers;

import com.atomwoz.execit.base.AbstractObserver;
import com.atomwoz.execit.base.Controllable;

public class ConfirmObserver extends AbstractObserver
{

	public ConfirmObserver()
	{
		super(0,true);
	}

	@Override
	public void startObservation(Controllable commandToObserve, String[] params)
	{
		while(true)
		{
			try
			{
				Thread.sleep(10);
			} 
			catch (InterruptedException e){}
			if(!commandToObserve.isAlive())
			{
				log("Task " + commandToObserve.getNiceName() + " finished its job");
				break;
			}
		}

	}

}
