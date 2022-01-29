package com.atomwoz.execit.base;

import org.jline.utils.AttributedStringBuilder;
import org.jline.utils.AttributedStyle;

import com.atomwoz.execit.pathEngine.PathEngine;

public class Main
{
	static SystemInfo sysInfo;
	static BasicIO io;

	public static void interpreter()
	{
		while (true)
		{
			try
			{

				RuntimeInfo.makeShellIdle();
				String prompt = PathEngine.getLoc() + "> ";
				AttributedStringBuilder builder = new AttributedStringBuilder()
						.style(AttributedStyle.BOLD.foreground(AttributedStyle.GREEN)).append(prompt);
				String readedLine;
				io.print(sysInfo.isSupportsAnsi() ? builder.toAnsi() : prompt);

				readedLine = io.readLine();

				if (readedLine.isBlank())
				{
					continue;
				}
				String input = readedLine.substring(1).strip();
				if (readedLine.startsWith("!"))
				{
					Executor.executeCommand(input, false, false);
				}
				else if (readedLine.startsWith("-"))
				{
					Executor.executeCommand(input, false, true);
				}
				else if (readedLine.startsWith("@"))
				{
					Executor.executeCommand(input, true, false);
				}
				else if (readedLine.startsWith("#"))
				{
					Executor.executeCommand(input, true, true);
				}
				else if (readedLine.startsWith("$"))
				{
					if (readedLine.length() >= 2)
					{
						ProcessController.startSystemCommand(false, input);
					}
					else
					{
						ProcessController.enterSystemShell(true);
					}
				}
				else if (readedLine.startsWith("*"))
				{
					if (readedLine.length() >= 2)
					{
						ProcessController.startSystemCommand(true, input);
					}
					else
					{
						ProcessController.enterSystemShell(true);
					}
				}
				else
				{
					try
					{
						Executor.executeCommand(readedLine, false, false);
					}
					catch (CommandNotExist ex)
					{
						throw new InputNotSpecifiedOrder();
					}
				}
			}
			catch (CommandNotExist e)
			{
				io.printError("Entered phrase is not recognized as a Exec-it command.");
			}
			catch (InputNotSpecifiedOrder e)
			{
				io.printError("Entered phrase is not recognize as executable and as Exec-it command.");
			}
			catch (ObserverNotExistException e)
			{
				String observerName = e.getMessage();
				io.printError("Given command observer " + (observerName.isBlank() ? "<empty name>" : observerName)
						+ " not exist.");
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
	}

	public static void main(String args[])
	{

		sysInfo = SystemInfo.getInfoFromSystem();
		io = BasicIO.getInstance();
		io.print("Starting..");
		TerminalFunctions.clearScreen();
		Runtime.getRuntime().addShutdownHook(new ShutdownHook());
		io.println("Dev preview of new Execit v1.0.1");
		io.printSeparator();
		interpreter();
	}

}

class InputNotSpecifiedOrder extends Exception
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 6508724157312975444L;

}
