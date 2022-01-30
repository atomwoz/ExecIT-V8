package com.atomwoz.execit.base;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

public class IOConcurrencyUtills
{
	private static AtomicBoolean semaphore = new AtomicBoolean(false);
	private static ArrayList<String> buffer = new ArrayList<String>();

	public static void lockSempahore()
	{
		semaphore.set(true);
	}

	public static void unLockSempahore()
	{
		semaphore.set(false);
	}

	public static boolean getSemaphoreValue()
	{
		return semaphore.get();
	}

	public static void addToBuffer(String line)
	{
		buffer.add(line);
	}

	public static void clearBuffer()
	{
		buffer.clear();
	}

	public static String[] getTable()
	{
		String[] result = new String[buffer.size()];
		result = buffer.toArray(result);
		return result;
	}
}
