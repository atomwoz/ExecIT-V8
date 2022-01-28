package com.atomwoz.execit.base;

import jline.Terminal;
import jline.console.ConsoleReader;

public class SystemInfo
{
	private boolean isWindows;
	private boolean isSupportsAnsi;
	private String systemName;
	private String userName;
	private String userHome;
	private String userDir;
	private String defaultShell;
	private String systemArch;

	private static SystemInfo instance = null;

	private static int DEFAULT_CONSOLE_SIZE = 50;

	private SystemInfo()
	{
		userName = System.getProperty("user.name");
		userHome = System.getProperty("user.home");
		userDir = System.getProperty("user.dir");
		systemName = System.getProperty("os.name");
		systemArch = System.getProperty("os.arch");
		isWindows = systemName.toLowerCase().contains("windows");

		// Identifying a system shell

		if (isWindows)
		{
			defaultShell = "cmd";
		}
		else
		{
			String shell = System.getProperty("SHELL");
			if (shell == null)
			{
				String defShell = ProcessController.checkSystemCommands("bash", "zsh", "fish", "ksh");
				if (defShell != null)
				{
					defaultShell = defShell;
				}
				else
				{
					defaultShell = "sh";
				}
			}
			else
			{
				defaultShell = shell;
			}

		}
		isSupportsAnsi = isAnsiTerminal();
	}

	private boolean isAnsiTerminal()
	{
		boolean isAnsii = false;
		try
		{
			ConsoleReader reader = new ConsoleReader();
			isAnsii = reader.getTerminal().isAnsiSupported();
			finalizeConsole(reader);
		}
		catch (Exception e)
		{
			isAnsii = false;
		}
		String osType = System.getProperty("os.name").toLowerCase();
		boolean isWindows = osType.contains("windows");
		if (isWindows)
		{

			try
			{
				if (osType.length() < 9)
				{
					return false;
				}
				int winVer = Integer.valueOf(osType.substring("windows".length() + 1));
				return winVer >= 10 & isAnsii;
			}
			catch (NumberFormatException e)
			{
				return false;
			}

		}
		else
		{
			return isAnsii;
		}

	}

	private void finalizeConsole(ConsoleReader console) throws Exception
	{
		Terminal term = console.getTerminal();
		term.setEchoEnabled(true);
		term.init();
		term.reset();
		term.restore();
		console.close();
		console.delete();

	}

	public static SystemInfo getInfoFromSystem()
	{
		if (instance == null)
		{
			instance = new SystemInfo();
		}
		return instance;

	}

	public void forceSupportANSI()
	{
		isSupportsAnsi = true;
	}

	public void disableSupportANSI()
	{
		isSupportsAnsi = false;
	}

	public boolean isWindows()
	{
		return isWindows;
	}

	public boolean isSupportsAnsi()
	{
		return isSupportsAnsi;
	}

	public String getSystemName()
	{
		return systemName;
	}

	public String getUserName()
	{
		return userName;
	}

	public String getUserHome()
	{
		return userHome;
	}

	public String getUserDir()
	{
		return userDir;
	}

	public String getDefaultShell()
	{
		return defaultShell;
	}

	public String getSystemArch()
	{
		return systemArch;
	}

	public int getConsoleWidth()
	{
		try
		{
			var term = new ConsoleReader();
			int width = term.getTerminal().getWidth();
			if (width < 10)
			{
				finalizeConsole(term);
				return DEFAULT_CONSOLE_SIZE;
			}
			else
			{
				finalizeConsole(term);
				return width;
			}

		}
		catch (Exception e)
		{
			return DEFAULT_CONSOLE_SIZE;
		}

	}

}
