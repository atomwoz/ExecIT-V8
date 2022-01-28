package com.atomwoz.execit.virtual;

import java.nio.charset.Charset;

public class VirtualFile extends VirtualDiskElement
{

	public VirtualFile(String fileName, String author)
	{
		super(fileName, author, 0);
	}

	public VirtualFile(String fileName, String author, long maxSize)
	{
		super(fileName, author, maxSize);
	}

	public VirtualFile(String fileName, long maxSize)
	{
		super(fileName, maxSize);
	}

	public VirtualFile(String fileName)
	{
		super(fileName);
	}

	String content;
	Charset encoding = Charset.forName("UTF-8");

	public byte[] readBytes()
	{
		touch();
		return content.getBytes(encoding);
	}

	public void writeBytes(byte[] bytes) throws VolumeStorageLimitExceesed
	{
		touch();
		updateSize(bytes.length - currentSize, 0, false);
		content = new String(bytes, encoding);
	}

	public void appendBytes(byte[] bytes) throws VolumeStorageLimitExceesed
	{
		touch();
		updateSize(bytes.length, 0, false);
		String dest = new String(bytes, encoding);
		content += dest;
	}

	public void clear()
	{
		content = "";
		try
		{
			updateSize(currentSize, 0, true);
		}
		catch (VolumeStorageLimitExceesed e)
		{
		}
		touch();
	}

	public String readAll()
	{
		return content;
	}

	public void write(String toWrite) throws VolumeStorageLimitExceesed
	{
		touch();
		updateSize(toWrite.getBytes().length - currentSize, 0, false);
		content = toWrite;
	}

	public void append(String toAppend) throws VolumeStorageLimitExceesed
	{
		updateSize(toAppend.getBytes().length, 0, false);
		content += toAppend;
	}

	public Charset getEncoding()
	{
		return encoding;
	}

	public void setEncoding(Charset charset)
	{
		this.encoding = charset;
	}
}
