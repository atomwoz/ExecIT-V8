package com.atomwoz.execit.base;

import java.util.Optional;

public class RuntimeInfo
{
	public enum ShellState
	{
		BUSY, IDLE, IN_SYSTEM_SHELL;
	}
	private static ShellState shellState = ShellState.IDLE;
	private static String foregroundName = "";
	
	public static void makeShellBusy(String appName)
	{
		shellState = ShellState.BUSY;
		foregroundName = appName;
	}
	public static void makeShellIdle()
	{
		shellState = ShellState.IDLE;
		foregroundName = "";
	}
	public static boolean isWorking()
	{
		return !(shellState != ShellState.IDLE);
	}
	public static Optional<String> getForegroundName()
	{
		return Optional.of(foregroundName);
	}
}
