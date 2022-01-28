package com.atomwoz.execit.base;

import static com.atomwoz.execit.base.IOConcurrencyUtills.addToBuffer;
import static com.atomwoz.execit.base.IOConcurrencyUtills.clearBuffer;
import static com.atomwoz.execit.base.IOConcurrencyUtills.getBufferIterator;
import static com.atomwoz.execit.base.IOConcurrencyUtills.getSemaphoreValue;
import static com.atomwoz.execit.base.IOConcurrencyUtills.lockSempahore;
import static com.atomwoz.execit.base.IOConcurrencyUtills.unLockSempahore;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import org.jline.utils.AttributedStringBuilder;
import org.jline.utils.AttributedStyle;

/**
 * Singleton class for simple IO on System out,in and error printing
 * 
 * @author atwoz
 *
 */
public class BasicIO
{
	private static BasicIO instance;

	private PrintWriter out;
	private PrintWriter err;
	private BufferedReader in;
	private SystemInfo sysInfo;
	private boolean echo = false;

	char SEPARATOR_CHAR = '=';

	public synchronized void print(String str)
	{
		if (getSemaphoreValue())
		{
			addToBuffer(str);
		}
		else
		{
			out.print(str);
			out.flush();
		}
	}

	public synchronized void println(String str)
	{
		print(str + System.lineSeparator());
	}

	public synchronized void println(String[] array)
	{
		for (String s : array)
		{
			println(s);
		}
		out.flush();
	}

	public PrintWriter getOutput()
	{
		return out;
	}

	public BufferedReader getInput()
	{
		return in;
	}

	public PrintWriter getErr()
	{
		return err;
	}

	void disableEcho()
	{
		echo = false;
	}

	void enableEcho()
	{
		echo = true;
	}

	public synchronized void echo(String str)
	{
		if (echo)
		{
			String toEcho = "..." + str + "...";
			AttributedStringBuilder builder = new AttributedStringBuilder()
					.style(AttributedStyle.DEFAULT.foreground(AttributedStyle.BRIGHT)).append(toEcho);
			println(sysInfo.isSupportsAnsi() ? builder.toAnsi() : toEcho);
		}
	}

	private void printCode(String info, String code, int style)
	{
		printCodeRaw(info, code, AttributedStyle.DEFAULT.foreground(style));
	}

	private void printCodeRaw(String info, String code, AttributedStyle style)
	{
		String message = "";
		if (code.isBlank() || code == null)
		{
			message = info;
		}
		else
		{
			message = code + " " + info;
		}
		AttributedStringBuilder builder = new AttributedStringBuilder().style(style).append(message);
		println(sysInfo.isSupportsAnsi() ? builder.toAnsi() : message);
	}

	void printInfo(String info)
	{
		printCode(info, "[INFO]", AttributedStyle.BLUE);
	}

	void printWarning(String info)
	{
		printCode(info, "[WARNING]", AttributedStyle.YELLOW);
	}

	void printSuccess(String info)
	{
		printCode(info, "[SUCCESS]", AttributedStyle.GREEN);
	}

	void printObserverInfo(String info)
	{
		printCode(info, "[OBSERVER LOG]", AttributedStyle.MAGENTA);
	}

	public void printlnBold(String line)
	{
		String rawLine = "---" + line + "---";
		if (sysInfo.isSupportsAnsi())
		{
			AttributedStringBuilder builder = new AttributedStringBuilder().style(AttributedStyle.BOLD).append(line);
			println(builder.toAnsi());
		}
		else
		{
			println(rawLine);
		}
	}

	synchronized String readLine() throws IOException
	{
		// printTerminalInfo("BUFFER LOCKED");
		lockSempahore();
		String line = in.readLine();
		// printTerminalInfo("BUFFER UnLOCKED");
		unLockSempahore();
		printAndClearBuffer();
		return line;
	}

	private synchronized void printAndClearBuffer()
	{
		for (String s : getBufferIterator())
		{
			out.print(s);
		}
		out.flush();
		clearBuffer();

	}

	void setOut(PrintWriter out)
	{
		this.out = out;
	}

	public void printError(String message)
	{
		String errCode = "[ERROR] " + message + " !!!";
		AttributedStringBuilder builder = new AttributedStringBuilder()
				.style(AttributedStyle.DEFAULT.foreground(AttributedStyle.RED)).append(errCode);
		err.println(sysInfo.isSupportsAnsi() ? builder.toAnsi() : errCode);
	}

	public void printObserverError(String message)
	{
		String errCode = "[OBSERVER ERROR] " + message;
		AttributedStringBuilder builder = new AttributedStringBuilder()
				.style(AttributedStyle.DEFAULT.foreground(AttributedStyle.RED)).append(errCode);
		err.println(sysInfo.isSupportsAnsi() ? builder.toAnsi() : errCode);
	}

	public void printSeparator()
	{
		String result = "";
		for (int i = 0; i < SystemInfo.getInfoFromSystem().getConsoleWidth(); i++)
		{
			result += SEPARATOR_CHAR;
		}
		println(result);
	}

	// Singleton stuff
	private BasicIO()
	{
		out = new PrintWriter(System.out, true);
		err = new PrintWriter(System.err, true);
		in = new BufferedReader(new InputStreamReader(System.in));
		sysInfo = SystemInfo.getInfoFromSystem();
		if (sysInfo.isWindows())
		{
			try
			{
				ProcessController.startSystemCommand(false, "chcp", "65001");
				// TODO Polskie znaki w konsoli , ogarnac linijke na gorze
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
	}

	public static BasicIO getInstance()
	{
		if (instance == null)
		{
			instance = new BasicIO();
		}
		return instance;
	}
}

class HeaderPrintWriter extends PrintWriter
{
	private String header;

	public HeaderPrintWriter(PrintWriter out, String header)
	{
		super(out);
		this.header = header;
	}

	@Override
	public void write(String s)
	{
		super.write(header + s);
	}

}
