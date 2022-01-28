package com.atomwoz.execit.base;

import java.io.IOException;
import java.math.BigInteger;
import java.text.DecimalFormat;

public class TerminalFunctions
{
	static BasicIO io = BasicIO.getInstance();
	static char NULLCHAR = '\u0000';

	public static void clearScreen()
	{
		SystemInfo sysInfo = SystemInfo.getInfoFromSystem();
		Process p = null;
		if (sysInfo.isWindows())
		{
			try
			{
				p = ProcessController.startSystemCommand(false, "cls");
			}
			catch (IOException e)
			{
				try
				{
					p = ProcessController.startSystemCommand(false, "clear");
				}
				catch (IOException e1)
				{
					io.printWarning("Can't clear the screen " + e1.getMessage());
				}
			}
		}
		else
		{
			try
			{
				p = ProcessController.startSystemCommand(false, "clear");
			}
			catch (IOException e)
			{
				try
				{
					p = ProcessController.startSystemCommand(false, "cls");
				}
				catch (IOException e1)
				{
					io.printWarning("Can't clear the screen " + e1.getMessage());
				}
			}
		}
		try
		{
			p.waitFor();
		}
		catch (InterruptedException e)
		{
		}
	}

	public static BigInteger stringToBytes(String mem, boolean useBytes)
	{
		int multiplier = 1024;
		int divider = !useBytes ? 8 : 1;
		mem = mem.toUpperCase();
		if (mem.length() < 1)
		{
			io.printError("Wrong size specified, it has to be number with K/M/G/T B");
			return null;
		}
		if (!mem.endsWith("B") && !mem.endsWith("b"))
		{
			mem = mem.concat("B");
		}
		char[] prefixList =
		{ NULLCHAR, 'K', 'M', 'G', 'T', 'P', 'E', 'Z', 'Y' };
		int globali = -2, lastChar = 0;
		char prefixChar = mem.charAt(mem.length() - 2);
		lastChar = mem.length() - 2;
		try
		{
			Integer.valueOf(String.valueOf(prefixChar));
			prefixChar = NULLCHAR;
			lastChar = mem.length() - 1;
		}
		catch (NumberFormatException e)
		{
			// Nie obslugujemy bledu braku wartosci numerycznej bo on sie sam obsluzy kodem
			// ponizej konwersji
			// We do not handle Number Format Exception because it handles itself upper
		}
		for (int i = 0; i < prefixList.length; i++)
		{
			if (prefixList[i] == prefixChar)
			{
				globali = i;
				break;
			}
		}
		if (globali == -2)
		{
			io.printError("Wrong size specified, it has to be number with K/M/G/T B");
			return null;
		}

		BigInteger bigMultiplier = BigInteger.valueOf(multiplier);
		BigInteger value = new BigInteger(mem.substring(0, lastChar));
		return bigMultiplier.pow(globali).multiply(value).divide(BigInteger.valueOf(divider));
		// return (Math.pow(multiplier,globali) * value)/divider;
	}

	public static String simplifyMemory(double memory, boolean isBite, boolean useTousands)
	{
		int onebyte = useTousands ? 1000 : 1024;
		char[] prefixList =
		{ NULLCHAR, 'K', 'M', 'G', 'T', 'P', 'E', 'Z', 'Y' };
		int i = 0;
		DecimalFormat df = new DecimalFormat("0.00");
		if (isBite)
		{
			memory /= 8;
		}
		while (memory >= onebyte && i < prefixList.length - 1)
		{
			memory /= 1024;
			i++;
		}
		return String.valueOf(df.format(memory)) + prefixList[i] + "B";

	}
}
