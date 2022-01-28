package com.atomwoz.execit.base;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.atomic.AtomicBoolean;

public class IOConcurrencyUtills
{
	private static AtomicBoolean semaphore = new AtomicBoolean(false);
	private static ArrayList<String> buffer = new ArrayList<String>();
	
	public synchronized static void lockSempahore()
	{
		semaphore.set(true);
	}
	public synchronized static void unLockSempahore()
	{
		semaphore.set(false);
	}
	public synchronized static boolean getSemaphoreValue()
	{
		return semaphore.get();
	}
	public synchronized static void addToBuffer(String line)
	{
		buffer.add(line);
	}
	public synchronized static void clearBuffer()
	{
		buffer.clear();
	}
	public synchronized static Iterable<String> getBufferIterator()
	{
		return buffer;
	}
}
