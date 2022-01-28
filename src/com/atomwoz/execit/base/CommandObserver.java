package com.atomwoz.execit.base;

public interface CommandObserver
{
	void startObservation(Controllable commandToObserve, String params[]);
}
