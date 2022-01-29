package com.atomwoz.execit.base;

public class LineArgue
{
	public String name;
	public boolean isInApo;

	public LineArgue(String name, boolean apo)
	{
		this.name = name;
		this.isInApo = apo;
	}

	@Override
	public String toString()
	{
		return name;
	}

}
