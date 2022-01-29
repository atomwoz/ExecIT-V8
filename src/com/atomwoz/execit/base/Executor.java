package com.atomwoz.execit.base;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class Executor
{
	/**
	 * It's executes with parameters command given in line param, command list is in
	 * NameMapper class
	 * 
	 * @author atomwoz
	 * @param line  - command with params to execute
	 * @param async - Does command has to be executed asynchronous
	 * @return code of ended program -1 when is async -2 when unexcepted error
	 * @throws MalformedInputException   when input is empty or has wrong characters
	 * @throws CommandNotExist           when command not exist
	 * @throws ObserverNotExistException
	 */
	// TODO Returning a value from started command
	public static int executeCommand(String line, boolean async, boolean muteIO)
			throws CommandNotExist, ObserverNotExistException
	{
		if (line.isBlank())
		{
			throw new CommandNotExist();
		}
		HashSet<Pair<String[], Class<? extends CommandBase>>> mapper = NameMapper.getCommandRegister();
		// HashMap<String,AbstractObserver> obervers = NameMapper.getObserverRegister();

		// Parsing observers
		int firstAppePos = line.indexOf('&');
		String observersLine = null;
		String sepCommand = null;
		if (firstAppePos != -1)
		{
			observersLine = line.substring(firstAppePos).strip();
			sepCommand = line.substring(0, firstAppePos).strip();
		}
		else
		{
			sepCommand = line;
		}
		String inputCommand = "", inputArgue = "";
		int splitterPos = sepCommand.indexOf(' ');
		if (splitterPos != -1)
		{
			inputCommand = sepCommand.substring(0, splitterPos);
			inputArgue = sepCommand.substring(splitterPos).strip();
		}
		else
		{
			inputCommand = sepCommand;
		}
		inputCommand = inputCommand.replace("_", "");
		inputCommand = inputCommand.replace("-", "");

		// Parsing argues
		ArrayList<LineArgue> parasedLines = tokenizeInput(sepCommand);
		HashSet<String> startFlags = new HashSet<>();
		if (parasedLines.size() < 1)
		{
			throw new CommandNotExist();
		}
		// variables effective final
		parasedLines.remove(0);

		// Start flag support
		for (int j = 0; j < parasedLines.size(); j++)
		{
			var x = parasedLines.get(j);
			String str = x.name;
			if (!x.isInApo && str.startsWith("--"))
			{
				str = str.substring(2);
				startFlags.add(str);
				parasedLines.set(j, new LineArgue("", false));
			}
		}
		// Remove flags from argues
		parasedLines.removeIf(x -> x.name.isEmpty());

		BasicIO.getInstance().println(Arrays.toString(startFlags.toArray(String[]::new)));
		String[] tokens = new String[parasedLines.size()];
		for (int j = 0; j < parasedLines.size(); j++)
		{
			tokens[j] = parasedLines.get(j).toString();
		}
		boolean isExist = false;

		// TODO Add start values support
		for (Pair<String[], Class<? extends CommandBase>> cmdToCheck : mapper)
		{
			for (String command : cmdToCheck.first)
			{
				if (command.equals(inputCommand))
				{
					isExist = true;
					// FIXME Remove start flags from argue line
					Starter commandStarter = new Starter(tokens, observersLine, cmdToCheck.second, inputCommand,
							inputArgue, muteIO, async, startFlags);
					Thread commandThread = new Thread(commandStarter, inputCommand);

					// Throwing when command not exist
					if (!isExist)
					{
						throw new CommandNotExist();
					}
					RuntimeInfo.makeShellBusy(command);
					// STARTING COMMAND
					commandThread.start();

					if (!commandStarter.getAsync())
					{
						try
						{
							commandThread.join();
						}
						catch (InterruptedException e)
						{
						}
					}
					else
					{
						try
						{
							// Waiting for force resign a CPU time for new started thread
							Thread.sleep(0, 1);
						}
						catch (InterruptedException e)
						{
						}
					}
					return commandStarter.getStopCode();
				}
			}
		}
		// Proceeding observers

		return 0;
	}

	/**
	 * It splits toSplit to two parts at first space
	 * 
	 * @param toSplit String to split
	 * @return 2 elements array with result
	 */
	@SuppressWarnings("unused")
	private static String[] splitOnFirstSpace(String toSplit)
	{
		int spacePos = toSplit.indexOf(' ');
		String firstPart = toSplit.substring(0, spacePos);
		String secondPart = toSplit.substring(spacePos);
		return new String[]
		{ firstPart, secondPart };
	}

	public static ArrayList<LineArgue> tokenizeInput(String toTokenize)
	{
		ArrayList<LineArgue> tokens = new ArrayList<>();
		String toToken2 = toTokenize.strip() + "\"";
		int lastDeletedPos = 0;
		boolean isInApostrophe = false;

		for (int i = 0; i < toToken2.length(); i++)
		{
			char ch = toToken2.charAt(i);
			char beforeCh = i == 0 ? '\u0000' : toToken2.charAt(i - 1);
			boolean okBefore = beforeCh != '\\';
			boolean onApostrophe = ch == '\"' && okBefore;
			if ((ch == ' ' && !isInApostrophe) || onApostrophe)
			{
				// Slicer
				String sliced = toToken2.substring(lastDeletedPos + 1, i);
				if (!sliced.isEmpty())
				{
					if (!isInApostrophe)
					{
						sliced = sliced.strip();
					}
					tokens.add(new LineArgue(sliced.replace("\\\"", "\""), isInApostrophe));
				}
				lastDeletedPos = i;
			}
			if (onApostrophe)
			{
				isInApostrophe = !isInApostrophe;
			}
		}
		return tokens;
	}

	private static String deleteApostrophes(String toDelete)
	{
		System.out.println(toDelete);
		final char NULLCHAR = '\u0000';
		StringBuilder toReturn = new StringBuilder(toDelete);
		int offset = 0;
		for (int i = 0; i < toDelete.length(); i++)
		{
			char beforeChar = i == 0 ? NULLCHAR : toDelete.charAt(i - 1);
			if (toDelete.charAt(i) == '\"')
			{
				if (beforeChar == '\\')
				{
					toReturn.deleteCharAt((i - offset) - 1);
				}
				toReturn.deleteCharAt(i - offset);
				offset++;
			}
		}
		System.out.println(toReturn.toString());
		return toReturn.toString();
	}

}

class Observator implements Runnable
{
	CommandBase cmdToCheck;
	String observersLine;

	public Observator(CommandBase cmdToCheck, String observersLine)
	{
		this.cmdToCheck = cmdToCheck;
		this.observersLine = observersLine;
	}

	@Override
	public void run()
	{
		paraseObservers(observersLine, cmdToCheck);
	}

	void initObservers() throws ObserverNotExistException
	{
		String observers[] = observersLine.split("&");
		HashMap<String, AbstractObserver> observersRegister = NameMapper.getObserverRegister();
		for (String observerName : observers)
		{
			if (observerName.isBlank())
			{
				continue;
			}
			String name;
			int spacePos = observerName.indexOf(' ');
			if (spacePos != -1)
			{
				name = observerName.substring(0, spacePos);
			}
			else
			{
				name = observerName;
			}
			if (!observersRegister.containsKey(name))
			{
				throw new ObserverNotExistException(name);
			}
		}
	}

	/**
	 * It parase and start observers attached to Controllable
	 * 
	 * @param observersLine
	 * @param toObserve
	 * @throws ObserverNotExistException
	 */
	private void paraseObservers(String observersLine2, Controllable toObserve)
	{
		String observers[] = observersLine.split("&");
		HashMap<String, AbstractObserver> observersRegister = NameMapper.getObserverRegister();
		for (String observerName : observers)
		{
			if (observerName.isBlank())
			{
				continue;
			}
			int spacePos = observerName.indexOf(' ');
			String name, body;
			if (spacePos != -1)
			{
				name = observerName.substring(0, spacePos);
				body = observerName.substring(spacePos);
			}
			else
			{
				name = observerName;
				body = "";
			}
			if (observersRegister.containsKey(name))
			{

				Runnable preparer = () ->
				{
					AbstractObserver x = observersRegister.get(name);
					x.startObservation(toObserve, body.strip().split(" "));
				};
				Thread observerThread = new Thread(preparer, "Observer " + name);
				observerThread.start();
			}
		}
	}

}

class Starter extends Thread
{
	private String[] tokens;
	private CommandBase commandToRun;
	private Class<? extends CommandBase> rawClass;
	private String inputCommand;
	private String inputArgue;
	private String observerLine;
	private boolean muteIO;
	private boolean async;
	private int stopCode = -1;
	private Set<String> startFlags;

	public Starter(String[] tokens, String observerLine, Class<? extends CommandBase> commandToRun, String inputCommand,
			String inputArgue, boolean muteIO, boolean async, Set<String> startFlags)
	{
		this.tokens = tokens;
		this.inputCommand = inputCommand;
		this.inputArgue = inputArgue;
		this.muteIO = muteIO;
		this.async = async;
		this.rawClass = commandToRun;
		this.observerLine = observerLine;
		this.startFlags = startFlags;

	}

	public boolean getAsync()
	{
		return async;
	}

	public int getStopCode()
	{
		return stopCode;
	}

	public boolean initCommand()
	{
		StartMode mode = commandToRun.startMode;
		BasicIO io = BasicIO.getInstance();
		int arguesCount = tokens.length;

		// Start mode handling
		if ((mode == StartMode.ONLY_NORMAL) && async)
		{
			io.printError("This command can't be runned in asynchronous mode");
			return false;
		}
		if ((mode == StartMode.ONLY_ASYNC) && !async)
		{
			io.printError("This command must be runned in asynchronous mode");
			return false;
		}
		if (mode == StartMode.ONLY_NORMAL_SILENCE)
		{
			io.echo("Changing start mode to normal");
			async = false;
		}
		else if (mode == StartMode.ONLY_ASYNC_SILENCE)
		{
			io.echo("Changing start mode to async");
			async = true;
		}
		if (arguesCount < commandToRun.minimumArgues)
		{
			io.printError("This command must be run with at least " + commandToRun.minimumArgues
					+ " arguments, running with " + arguesCount + " argues");
			return false;
		}
		if (arguesCount > commandToRun.maximumArgues)
		{
			io.printError("This command must be run with maximum " + commandToRun.maximumArgues
					+ " arguments, running with " + arguesCount + " argues");
			return false;
		}
		return true;
	}

	@Override
	public void run()
	{
		BasicIO io = BasicIO.getInstance();
		try
		{
			// THATS MAGIC !!! Create object of command
			commandToRun = rawClass.getConstructor(Thread.class, String.class).newInstance(currentThread(),
					inputCommand);
		}
		catch (Exception e)
		{
			io.printError("Wow this error can't appear, WTF !!!, fix your extension code !!! \nERROR_MESSAGE: "
					+ e.getMessage());
			return;
		}
		// Check requirements
		if (initCommand())
		{
			// Parsing observers
			if (observerLine != null)
			{
				Observator observator = new Observator(commandToRun, observerLine);
				Thread observerThread = new Thread(observator, "Observers initator");
				try
				{
					observator.initObservers();
				}
				catch (ObserverNotExistException e)
				{
					io.printError("We can't find " + e.getMessage() + " observer");
					return;
				}
				observerThread.start();
			}
			// Exception handler for not force quit app in exception in command
			Thread.setDefaultUncaughtExceptionHandler((thread, throwable) ->
			{
				io.printError(
						"Unexcepted error in command " + inputCommand + " error message: " + throwable.getMessage());
			});

			// Checking that we have to mute io
			if (muteIO)
			{
				commandToRun.muteIO();
			}
			try
			{
				// Starting command
				stopCode = commandToRun.doCommand(new StartArgue(inputCommand, tokens, inputArgue, startFlags, null));
			}
			catch (CommandRuntimeExcepiton e)
			{
				io.printError("Command " + inputCommand + " meet error, exit message: " + e.getMessage()
						+ " command halted !!!");
			}
		}
	}
}

class CommandNotExist extends Exception
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 762174614909038315L;

}

class ObserverNotExistException extends Exception
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -9216107357465998826L;

	public ObserverNotExistException(String observerName)
	{
		super(observerName);
	}

}

class MalformedInputException extends Exception
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2697138047821723409L;

}