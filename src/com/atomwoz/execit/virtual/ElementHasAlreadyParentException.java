package com.atomwoz.execit.virtual;

public class ElementHasAlreadyParentException extends VirtualDiskException
{

	public ElementHasAlreadyParentException(VirtualDiskElement vde)
	{
		super("This element is already assigned to other folder ", vde);
	}

}
