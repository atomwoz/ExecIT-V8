package com.atomwoz.execit.base;

public class StartFlag implements Comparable<String>
{
	private String name;
	private String content;
	
	public StartFlag()
	{
		
	}
	public Pair<String,String> getPair()
	{
		return new Pair<String, String>(name, content);
	}
	public String getName()
	{
		return name;
	}
	public String getContent()
	{
		return name;
	}
	public StartFlag(String name, String content)
	{
		this.name = name;
		this.content = content;
	}
	
	@Override
	public int compareTo(String o)
	{
		return name.compareTo(o);
	}

}
