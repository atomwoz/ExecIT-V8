package com.atomwoz.execit.base;

import java.util.HashMap;
import java.util.Map;

public class GlobalControllableRegister
{
	private static int lastID = 0;
	private static HashMap<Integer, Controllable> register = new HashMap<>();

	public static synchronized int getNextId()
	{
		return ++lastID;
	}

	public static synchronized int registerControllable(Controllable cntrl)
	{
		register.put(lastID, cntrl);
		return lastID;
	}

	public static synchronized Controllable getById(int ID)
	{
		return register.get(ID);
	}

	public static synchronized Map<Integer, Controllable> getTable()
	{
		return register;
	}

	public static void update()
	{
		register.entrySet().removeIf(t -> !t.getValue().isAlive());
	}
}
