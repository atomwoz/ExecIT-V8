package com.atomwoz.execit.base;

public abstract class Controllable
{
	private boolean isGlobalID = false;
	private long globalID = -1;
	private boolean wasSetted = false;
	
	public abstract boolean stop();
	public abstract boolean isAlive();
	public abstract boolean stopImediatly();
	public abstract String getNiceName();
	
	public boolean setID(long ID, boolean isGlobalID)
	{
		if(!wasSetted)
		{
			this.globalID = ID;
			this.isGlobalID = isGlobalID;
			wasSetted = true;
			return true;
		}
		else
		{
			return false;
		}
		
	}
	protected boolean isIDGlobal()
	{
		return isGlobalID;
	}
	protected long getID()
	{
		return globalID;
	}
}