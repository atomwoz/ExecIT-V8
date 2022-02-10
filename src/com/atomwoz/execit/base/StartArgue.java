package com.atomwoz.execit.base;

import java.util.Map;
import java.util.Set;

public class StartArgue
{
	private String command;
	private String[] argues;
	private String argueLine;
	private Set<String> startFlags;
	private Map<String, String> startValues;
	private String rawLine;

	public StartArgue(String command, String[] argues, String argueLine, Set<String> startFlags,
			Map<String, String> startValues, String rawLine)
	{
		this.command = command;
		this.argues = argues;
		this.argueLine = argueLine;
		this.startFlags = startFlags;
		this.startValues = startValues;
		this.rawLine = rawLine;
	}

	public String getCommand()
	{
		return command;
	}

	public String[] getArgues()
	{
		return argues;
	}

	public String getArgueLine()
	{
		return argueLine;
	}

	public String getRawLine()
	{
		return rawLine;
	}

	public boolean hasFlag(String flag)
	{
		// Can be problems with parser when given empty flag as a result of parser error
		return hasFlag(flag, "");
	}

	public Set<String> getFlags()
	{
		return startFlags;
	}

	public boolean hasFlag(String flag, String shortFlag)
	{
		return startFlags.contains(flag) || startFlags.contains(shortFlag);
	}

	public boolean isValueWasDefinied(String valueName)
	{
		return startValues.containsKey(valueName);
	}

	public String getValue(String valueName)
	{
		return startValues.get(valueName);
	}

}
