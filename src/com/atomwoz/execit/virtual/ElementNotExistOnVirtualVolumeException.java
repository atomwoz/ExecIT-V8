package com.atomwoz.execit.virtual;

public class ElementNotExistOnVirtualVolumeException extends VirtualDiskException
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3188857378510394208L;

	public ElementNotExistOnVirtualVolumeException(String message, boolean t)
	{
		super(message);
	}

	public ElementNotExistOnVirtualVolumeException(String message)
	{
		super("There is not such element like " + message);
	}

}
