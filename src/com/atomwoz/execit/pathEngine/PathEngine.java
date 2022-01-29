package com.atomwoz.execit.pathEngine;

import java.io.File;
import java.io.IOException;
import java.nio.file.InvalidPathException;
import java.nio.file.Paths;

import com.atomwoz.execit.base.SystemInfo;

public class PathEngine
{
	private static String currentLocation = SystemInfo.getInfoFromSystem().getUserDir();

	public static void changeDirectory(String loc) throws IOException
	{
		currentLocation = blendPath(loc, false, false, false, FileTypes.DIR);
	}

	public static String getLoc()
	{
		return currentLocation;
	}

	public static String resolvePath(String loc, FileTypes type) throws IOException
	{
		return blendPath(loc, false, false, false, type);
	}

	public static String pwd(String loc) throws IOException
	{
		return blendPath(loc, true, false, false, FileTypes.BOTH);
	}

	public static FileName pathToFileName(String path) throws IOException
	{
		FileType type;
		String protocol = "";
		path = path.replace("\\", "/");
		if (path.contains("://"))
		{
			int protocolPos = path.indexOf("://");
			type = FileType.WEB_FILE;
			protocol = path.substring(0, protocolPos);
			path = path.substring(protocolPos + 3);
		}
		else
		{
			if (path.startsWith("%"))
			{
				path = path.substring(1);
				type = FileType.VIRTUAL_FILE;
			}
			else
			{
				type = FileType.PHYSICAL_FILE;
				path = blendPath(path, false, false, false, FileTypes.BOTH);
			}
		}
		int pos = path.lastIndexOf(File.separator);
		String fullPath = "";
		String name;
		String ext;
		if (pos == -1)
		{
			name = fullPath = path;
		}
		else
		{
			fullPath = path.substring(0, pos + 1);
			name = path.substring(pos + 1);
			if (name.isBlank())
			{
				name = fullPath;
			}
		}
		ext = "";
		if (name.contains("."))
		{
			int dotPos = name.indexOf('.');
			ext = name.substring(dotPos);
			name = name.substring(0, dotPos);
		}
		FileName toReturn = new FileName(name, fullPath, ext, protocol, type);
		return toReturn;

	}

	public static String getParent(String path)
	{
		path = path.replace('\\', '/');
		if (path.endsWith("/") && path.length() > 1)
		{
			path = path.substring(path.length() - 1);
		}
		int pos = path.lastIndexOf('/');
		if (pos != -1)
		{
			return path.substring(0, pos);
		}
		else
		{
			return "/";
		}
	}

	public static String correctPath(String path)
	{
		path = path.replace('\\', '/');
		while (path.startsWith("/") && path.length() >= 1)
		{
			path = path.substring(1);
		}
		while (path.endsWith("/") && path.length() >= 1)
		{
			path = path.substring(0, path.length() - 1);
		}
		return path;
	}

	public static String getChild(String path)
	{
		path = path.replace('\\', '/');
		if (path.endsWith("/") && path.length() > 1)
		{
			path = path.substring(path.length() - 1);
		}
		int pos = path.lastIndexOf('/');
		if (pos != -1)
		{
			return path.substring(pos + 1);
		}
		else
		{
			return path;
		}
	}

	public static enum FileTypes
	{
		FILE, DIR, BOTH
	};

	private static String blendPath(String loc, boolean force, boolean ignore_last, boolean pointToExecFile,
			FileTypes type) throws IOException
	{
		String def_localization = currentLocation;
		File file;
		boolean absolute = false;
		SystemInfo sysInfo = SystemInfo.getInfoFromSystem();
		boolean isRunningOnWindows = sysInfo.isWindows();
		// TODO Auto path correct get from global settings
		boolean autoPathCorrect = true;
		String execLoc = System.getProperty("user.dir");
		String termSign = File.separator;
		if (ignore_last)
		{
			loc = trimLastInPath(loc)[1];
		}
		// Checking directory up
		loc = loc.replace("...", "..");
		if (loc.contains(".."))
		{
			try
			{
				loc = loc.replace("..", Paths.get(def_localization).getParent().toString());
				absolute = true;
			}
			catch (NullPointerException e)
			{
				throw new IOException("Choosen path not exist");
			}
		}
		if (!isRunningOnWindows && autoPathCorrect)
		{
			loc = loc.replace("\\", "/");
		}
		if (isRunningOnWindows && autoPathCorrect)
		{
			loc = loc.replace("/", "\\");
		}
		String root = Paths.get(def_localization).getRoot().toString();
		if (loc.startsWith("*"))
		{
			if (loc.length() >= 2)
			{
				loc = loc.substring(1);
			}
			else
			{
				loc = "";
			}
			loc = root.concat(loc);
			absolute = true;
		}
		if (!def_localization.endsWith(File.separator))
		{
			def_localization = def_localization + File.separator;
		}
		// Replacing tokens
		if (loc.contains("~"))
		{
			loc = loc.replace("~", System.getProperty("user.home"));
			absolute = true;
		}
		loc = loc.replace("#", execLoc);

		// Working with system specified start and end string
		if (!isRunningOnWindows && !loc.startsWith(File.separator))
		{
			loc = File.separator + loc;
		}
		// Path path;
		try
		{
			Paths.get(loc);
		}
		catch (InvalidPathException e)
		{
			if (!force)
			{
				throw new IOException("Invalid path given");
			}
		}
		// System.out.println(loc + " ABS: " + absolute);

		String prob_loc = "";
		try
		{
			prob_loc = compilePath(def_localization, loc);
		}
		catch (InvalidPathException e)
		{
		}
		// Path path = Path.of(loc);
		File oldfile = new File(loc);
		File newfile = new File(prob_loc);

		if (isRunningOnWindows && oldfile.exists())
		{
			absolute = true;
		}
		if (isRunningOnWindows && loc.contains(":"))
		{
			absolute = true;
		}
		// Decide when use the absolute or def_loc+argue localization
		if ((!(absolute || (oldfile.exists() && !newfile.exists())) || (!oldfile.exists() && force)) && !absolute)
		{
			loc = prob_loc;
			// System.out.println("Kompilowanie");
		}
		/*
		 * else if(new File(prob_loc).exists() && absolute) {
		 * ConsoleUtills.printError("Choosen absolute path not exist"); return ""; }
		 */
		// System.out.println(loc);
		try
		{
			file = new File(loc);
			if (isRunningOnWindows && pointToExecFile && !file.exists())
			{
				file = new File(loc + ".cmd");
			}
			if (isRunningOnWindows && pointToExecFile && !file.exists())
			{
				file = new File(loc + ".bat");
			}
			if (isRunningOnWindows && pointToExecFile && !file.exists())
			{
				file = new File(loc + ".exe");
			}
			if (absolute && file.exists())
			{
				File ready_file = new File(loc);
				return ready_file.getCanonicalPath().toString();
			}
			if (file.exists() || (!file.exists() && force))
			{
				// System.out.println(def_localization);
				try
				{
					loc = Paths.get(loc).normalize().toString();
				}
				catch (InvalidPathException e)
				{
					if (!force)
					{
						throw new IOException("Choosen path contains wrong characters");
					}
				}
				if (!loc.endsWith(termSign) && type != FileTypes.FILE)
				{
					loc = loc.concat(termSign);
				}
				if (file.isDirectory() && type == FileTypes.FILE)
				{
					throw new IOException("Choosen path is a file");
				}
				// System.out.println(loc);
				File ready_file = new File(loc);
				return ready_file.getCanonicalPath().toString();
			}
			else
			{
				throw new IOException("Choosen path " + loc + " not exist");
			}
		}
		catch (NullPointerException e)
		{
			throw new IOException("Choosen path is null (empty) ");
		}
	}

	private static String compilePath(String def_loc, String path) throws InvalidPathException
	{
		return Paths.get(def_loc + path).normalize().toAbsolutePath().toString();
	}

	private static String[] trimLastInPath(String argue)
	{
		String str[] = new String[2];
		int last = argue.lastIndexOf(File.separator);
		if (last >= 0)
		{
			str[0] = argue.substring(last);
			str[1] = argue.substring(0, last);
		}
		else
		{
			str[0] = argue;
			str[1] = "";
		}
		return str;
	}
}
