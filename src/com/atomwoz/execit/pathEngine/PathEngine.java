package com.atomwoz.execit.pathEngine;

public class PathEngine
{
	public static FileName pathToFileName(String path)
	{
		FileType type;
		path = path.replace('\\', '/');
		String protocol = "";
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
			}
		}
		int pos = path.lastIndexOf('/');
		String fullPath;
		String name;
		String ext;
		if (pos == -1)
		{
			fullPath = name = path;
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
}
