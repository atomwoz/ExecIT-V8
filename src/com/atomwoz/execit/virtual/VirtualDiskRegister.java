package com.atomwoz.execit.virtual;

import java.util.HashMap;

import com.atomwoz.execit.pathEngine.PathEngine;

public class VirtualDiskRegister
{
	private static HashMap<String, VirtualVolume> disks = new HashMap<String, VirtualVolume>();

	public static void mount(String name, VirtualVolume volume) throws VirtualDiskException
	{
		if (!disks.containsKey(name))
		{
			disks.put(name, volume);
		}
		else
		{
			throw new VirtualDiskException("Volume with this name already exists");
		}
	}

	public static void createAndMount(String name, long capacity) throws VirtualDiskException
	{
		VirtualFolder vf = new VirtualFolder(name, capacity);
		VirtualVolume vv = new VirtualVolume(vf, capacity);
		mount(name, vv);
	}

	public static void createAndMount(String name) throws VirtualDiskException
	{
		createAndMount(name, -1);
	}

	public static HashMap<String, VirtualVolume> getStructure()
	{
		return disks;
	}

	public static VirtualFile getFileByAbsoluteName(String path, boolean canCreate) throws VirtualDiskException
	{
		var x = getElementByAbsoluteName(path, canCreate);
		if (x instanceof VirtualFile)
		{
			return (VirtualFile) x;
		}
		else
		{
			throw new VirtualDiskException("Choosen element is not a file");
		}
	}

	public static VirtualDiskElement getElementByAbsoluteName(String path, boolean canCreate)
			throws VirtualDiskException
	{
		path = PathEngine.correctPath(path);
		int last = path.indexOf('/');
		String volname = "";
		if (last != -1)
		{
			volname = path.substring(0, last);
			var vv = getVolumeByName(volname);
			return vv.resolveElementToPath(path.substring(last), canCreate);
		}
		else
		{
			try
			{
				var vv = getVolumeByName(path).getRoot();
				return vv;
			}
			catch (ElementNotExistOnVirtualVolumeException e)
			{
				throw new VirtualVolumeNotExist("We can't find volume " + path + " because that volume not exists.");
			}
		}
	}

	public static VirtualVolume getVolumeByName(String name) throws ElementNotExistOnVirtualVolumeException
	{
		var x = disks.get(name);
		if (x != null)
		{
			return x;
		}
		else
		{
			throw new VirtualVolumeNotExist("We can't find volume " + name + " because that volume not exists.");
		}
	}

	public static void unmountVolume(String name) throws ElementNotExistOnVirtualVolumeException
	{
		var x = disks.remove(name);
		if (x == null)
		{
			throw new VirtualVolumeNotExist("We can't delete " + name + " because that volume not exists.");
		}
	}

	public static boolean isEmpty()
	{
		return disks.size() <= 0;
	}
}
