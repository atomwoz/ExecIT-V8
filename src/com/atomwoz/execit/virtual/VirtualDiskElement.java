package com.atomwoz.execit.virtual;

import java.math.BigInteger;
import java.sql.Time;
import java.time.LocalTime;

public class VirtualDiskElement implements Cloneable
{
	String name;
	Time accesed;
	Time created;
	String author;
	VirtualFolder parent = null;
	long currentSize = 0;
	long maxSize = -1;
	long elements = 0;

	public VirtualDiskElement(String fileName)
	{
		setName(fileName);
		created = Time.valueOf(LocalTime.now());
		accesed = created;
	}

	public VirtualDiskElement(String fileName, long maxSize)
	{
		this(fileName);
		this.maxSize = maxSize;
	}

	public VirtualDiskElement(String fileName, String author, long maxSize)
	{
		this(fileName);
		this.author = author;
		this.maxSize = maxSize;
	}

	public void touch()
	{
		accesed = Time.valueOf(LocalTime.now());
	}

	public String getName()
	{
		return name;
	}

	public VirtualFolder getParent()
	{
		return parent;
	}

	public void setName(String s)
	{

		if (s.startsWith("/") && s.length() > 1)
		{
			s = s.substring(1);
		}
		s = s.replace("/", "");
		s = s.replace("\\", "");
		s = s.strip();
		name = s;
	}

	public Time getAccesed()
	{
		return accesed;
	}

	public Time getCreated()
	{
		return created;
	}

	public String getAuthor()
	{
		return author;
	}

	public void setAuthor(String author)
	{
		this.author = author;
	}

	@Override
	public String toString()
	{
		return getName();
	}

	@Override
	public Object clone() throws CloneNotSupportedException
	{
		return super.clone();
	}

	protected boolean canResize(long size)
	{
		BigInteger bi = BigInteger.valueOf(currentSize);
		bi = bi.add(BigInteger.valueOf(size));
		boolean exceese = false;
		if (maxSize > 0)
		{
			exceese = bi.compareTo(BigInteger.valueOf(maxSize)) == 1;
		}
		if (bi.compareTo(BigInteger.valueOf(Long.MAX_VALUE)) == 1 || exceese)
		{
			return false;
		}
		else
		{
			if (parent != null)
			{
				return parent.canResize(size);
			}
			else
			{
				return true;
			}
		}
	}

	protected void changeSize(long sizee, long count, boolean schrink)
	{
		long size = schrink ? -sizee : sizee;
		currentSize += size;
		elements += schrink ? -count : count;
		if (parent != null)
		{
			parent.changeSize(sizee, count, schrink);
		}
	}

	void updateSize(long size, long count, boolean schrink) throws VolumeStorageLimitExceesed
	{
		if (canResize(size))
		{
			changeSize(size, count, schrink);
		}
		else
		{
			throw new VolumeStorageLimitExceesed(this);
		}

	}

	public long getMaxSize()
	{
		return maxSize;
	}

	public long getCount()
	{
		return elements;
	}

	public long getSize()
	{
		return currentSize;
	}
}