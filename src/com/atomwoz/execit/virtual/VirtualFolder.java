package com.atomwoz.execit.virtual;

import java.util.HashSet;

public class VirtualFolder extends VirtualDiskElement
{
	HashSet<VirtualDiskElement> children = new HashSet<VirtualDiskElement>();

	public VirtualFolder(String fileName)
	{
		super(fileName);
	}

	public VirtualFolder(String fileName, long maxSize)
	{
		super(fileName);
		this.maxSize = maxSize;
	}

	public VirtualFolder(String fileName, String author, long maxSize)
	{
		super(fileName, author, maxSize);
	}

	@Override
	public String toString()
	{
		return name + ":\n" + listChildren(true);
	}

	public VirtualDiskElement getByName(String name) throws ElementNotExistOnVirtualVolumeException
	{
		Object[] arr = children.stream().filter(a -> a.name.equals(name)).toArray();
		if (arr.length > 0)
		{
			return (VirtualDiskElement) arr[0];
		}
		else
		{
			throw new ElementNotExistOnVirtualVolumeException(name);
		}
	}

	public boolean contains(String name)
	{
		try
		{
			getByName(name);
			return true;
		}
		catch (ElementNotExistOnVirtualVolumeException e)
		{
			return false;
		}
	}

	public void addChild(VirtualDiskElement element) throws VirtualDiskException
	{
		if (contains(element.getName()))
		{
			throw new ElementExistsOnVirtualVolume("Element " + element.getName() + " already exists in this folder");
		}
		if (element.parent == null)
		{
			element.parent = this;
			updateSize(element.currentSize, element.elements + 1, false);
			children.add(element);
		}
		else
		{
			throw new ElementHasAlreadyParentException(element);
		}
	}

	public void removeChild(String name) throws ElementNotExistOnVirtualVolumeException
	{
		var x = children.stream().filter(ve -> ve.getName().equals(name)).iterator().next();
		if (!children.remove(x))
		{
			throw new ElementNotExistOnVirtualVolumeException(name);
		}
		try
		{
			updateSize(x.currentSize, x.elements + 1, true);
		}
		catch (VolumeStorageLimitExceesed e)
		{
			e.printStackTrace();
		}
	}

	public void removeChild(VirtualDiskElement vde) throws ElementNotExistOnVirtualVolumeException
	{
		if (!children.remove(vde))
		{
			throw new ElementNotExistOnVirtualVolumeException(vde.getName());
		}
		try
		{
			updateSize(vde.getSize(), vde.elements, true);
		}
		catch (VolumeStorageLimitExceesed e)
		{
			e.printStackTrace();
		}
	}

	private String listChildren(boolean tab)
	{
		String content = "";
		for (VirtualDiskElement file : children)
		{
			content += (tab ? "\t" : "") + file.name + "\n";
		}
		return content;
	}

	public String listChildren()
	{
		return listChildren(false);
	}

}

class ElementExistsOnVirtualVolume extends VirtualDiskException
{

	public ElementExistsOnVirtualVolume(String message)
	{
		super(message);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
