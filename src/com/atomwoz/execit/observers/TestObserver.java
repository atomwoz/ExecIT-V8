package com.atomwoz.execit.observers;

import com.atomwoz.execit.base.AbstractObserver;
import com.atomwoz.execit.base.Controllable;

public class TestObserver extends AbstractObserver
{

	public TestObserver()
	{
		super(0, false);
	}

	@Override
	public void startObservation(Controllable commandToObserve, String[] params)
	{
		io.echo("O kurwa to dziala info o obserwowanym watku ");
	}

	

}
