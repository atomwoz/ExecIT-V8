package com.atomwoz.execit.virtual;

public class VirtualVolumeNotExist extends ElementNotExistOnVirtualVolumeException
{

	public VirtualVolumeNotExist(String message)
	{
		super(message, false);
	}

}
