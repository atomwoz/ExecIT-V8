package com.atomwoz.execit.pathEngine;

public class FileName
{
	private String name;
	private String fullPath;
	private String extension;
	private String protocol;
	private FileType type;

	public FileName(String name, String fullPath, String extension, String protocol, FileType type)
	{
		this.name = name;
		this.fullPath = fullPath;
		this.extension = extension;
		this.protocol = protocol;
		this.type = type;
	}

	public FileName(String name, String fullPath, String extension, boolean isVirtual)
	{
		this.name = name;
		this.fullPath = fullPath;
		this.extension = extension;
		this.protocol = null;
		this.type = isVirtual ? FileType.VIRTUAL_FILE : FileType.PHYSICAL_FILE;
	}

	public FileName(String name, String fullPath, String extension, FileType type)
	{
		this.name = name;
		this.fullPath = fullPath;
		this.extension = extension;
		this.protocol = null;
		this.type = type;
	}

	public String getName()
	{
		return name;
	}

	public String getFullPath()
	{
		return fullPath;
	}

	public String getExtension()
	{
		return extension;
	}

	public String getFileFullPath()
	{
		String str1 = fullPath + name + extension;
		String str2 = name + extension;

		if (!str1.equals(str2 + str2))
		{
			return str1;
		}
		else
		{
			return str2;
		}
	}

	public String getProtocol()
	{
		return protocol;
	}

	public FileType getType()
	{
		return type;
	}

}
