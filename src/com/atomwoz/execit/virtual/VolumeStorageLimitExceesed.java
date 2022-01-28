package com.atomwoz.execit.virtual;

public class VolumeStorageLimitExceesed extends VirtualDiskException
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 4667999964054679211L;

	public VolumeStorageLimitExceesed(VirtualDiskElement lowFolder)
	{
		super(lowFolder);
	}

}
