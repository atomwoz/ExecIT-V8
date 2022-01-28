package com.atomwoz.execit.virtual;

import java.io.IOException;

public class VirtualDiskException extends IOException
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -9027639798775499193L;
	VirtualDiskElement throwingAbout = null;

	public VirtualDiskException(VirtualDiskElement throwingAbout)
	{
		this.throwingAbout = throwingAbout;
	}

	public VirtualDiskException(String message)
	{
		super(message);
	}

	public VirtualDiskException(String message, VirtualDiskElement throwingAbout)
	{
		super(message);
		this.throwingAbout = throwingAbout;
	}

}
