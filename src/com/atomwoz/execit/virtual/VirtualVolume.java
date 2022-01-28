package com.atomwoz.execit.virtual;

import java.io.IOException;

import org.jline.utils.AttributedStringBuilder;
import org.jline.utils.AttributedStyle;

import com.atomwoz.execit.base.SystemInfo;
import com.atomwoz.execit.pathEngine.IFolder;
import com.atomwoz.execit.pathEngine.PathEngine;

public class VirtualVolume implements IFolder
{
	private String name;
	private VirtualFolder rootFolder;

	public VirtualVolume(VirtualFolder root)
	{
		this(root, -1);
	}

	public VirtualVolume(VirtualFolder root, long maxSize)
	{
		rootFolder = root;
		name = root.getName();
		rootFolder.maxSize = maxSize;
	}

	public String getName()
	{
		return name;
	}

	public VirtualFolder getRoot()
	{
		return rootFolder;
	}

	public VirtualFile resolveFileToPath(String path) throws VirtualDiskException
	{
		var x = resolveElementToPath(path, false);
		if (x instanceof VirtualFile)
		{
			return (VirtualFile) x;
		}
		else
		{
			throw new VirtualDiskException("Choosen element is not a file");
		}
	}

	public VirtualDiskElement resolveElementToPath(String path, boolean canCreate) throws VirtualDiskException
	{
		path = correctPath(path);
		if (path.isBlank())
		{
			return rootFolder;
		}
		String[] elements = path.split("/");
		VirtualFolder v = rootFolder;
		VirtualDiskElement cde = null;
		int last = elements.length - 1;
		for (int i = 0; i < last; i++)
		{
			String element = elements[i];
			cde = v.getByName(element);
			if (cde instanceof VirtualFolder)
				v = (VirtualFolder) cde;
			else
				throwElementNotFound("/" + path);
		}
		// System.out.println(path + "^^^" +elements[last]);
		try
		{
			return v.getByName(elements[last]);
		}
		catch (ElementNotExistOnVirtualVolumeException e)
		{
			if (canCreate)
			{
				var a = new VirtualFile(elements[last]);
				v.addChild(a);
				return a;
			}
			else
			{
				throwElementNotFound("/" + path);
				return null;
			}
		}
	}

	public VirtualFolder resolveFolderToPath(String path) throws VirtualDiskException
	{
		path = correctPath(path);
		VirtualDiskElement nearestParent = resolveElementToPath(path, false);
		if (nearestParent instanceof VirtualFolder)
		{
			return (VirtualFolder) nearestParent;
		}
		else
		{
			throw new VirtualDiskException(path + " is a file, not a directory");
		}
	}

	public boolean existsOnVolume(String path)
	{
		try
		{
			resolveElementToPath(path, false);
			return true;
		}
		catch (IOException e)
		{
			return false;
		}
	}

	private void throwElementNotFound(String couse) throws VirtualDiskException
	{
		throw new ElementNotExistOnVirtualVolumeException(couse);
	}

	@Override
	public VirtualDiskElement touchFile(String path) throws VirtualDiskException
	{
		path = correctPath(path);
		VirtualDiskElement v = resolveElementToPath(path, true);
		v.touch();
		return v;

	}

	@Override
	public void makeDirectory(String path) throws VirtualDiskException
	{
		path = correctPath(path);
		String parent = PathEngine.getParent(path);
		if (existsOnVolume(path))
		{
			throw new VirtualDiskException("Path like " + path + " already exists on virtual volume");
		}
		else
		{
			var v = (VirtualFolder) resolveElementToPath(parent, false);
			v.addChild(new VirtualFolder(PathEngine.getChild(path)));
		}
	}

	@Override
	public void removeElement(String path) throws VirtualDiskException
	{
		path = correctPath(path);
		if (path.isBlank())
		{
			throw new VirtualDiskException("You can't remove root folder, try rm or unmout");
		}
		// TODO Ask if removing root folder
		String parent = PathEngine.getParent(path);
		VirtualFolder nearestParent = resolveFolderToPath(parent);
		VirtualFolder vf = nearestParent;
		vf.removeChild(PathEngine.getChild(path));

	}

	@Override
	public void moveElement(String from, String to) throws VirtualDiskException
	{
		copyElement(from, to);
		removeElement(from);
	}

	@Override
	public void copyElement(String what, String to) throws VirtualDiskException
	{
		what = correctPath(what);
		to = correctPath(to);
		String name = PathEngine.getChild(to);
		String toParent = PathEngine.getParent(to);
		VirtualFolder dest = null;
		VirtualDiskElement toCopy = null;
		try
		{
			dest = resolveFolderToPath(toParent);
		}
		catch (IOException e)
		{
			throw new VirtualDiskException("Destination path to copy/move element not exist");
		}
		try
		{
			toCopy = resolveElementToPath(what, false);
		}
		catch (IOException e)
		{
			throw new VirtualDiskException("Source element to copy/move not exists");
		}
		try
		{
			VirtualDiskElement v = (VirtualDiskElement) toCopy.clone();
			v.setName(name);
			v.parent = null;
			dest.addChild(v);
		}
		catch (CloneNotSupportedException e)
		{
		}
	}

	private String correctPath(String path)
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

	@Override
	public VirtualDiskElement[] listFolder(String path) throws VirtualDiskException
	{
		var a = resolveFolderToPath(path).children;
		VirtualDiskElement vde[] = new VirtualDiskElement[a.size()];
		return a.toArray(vde);
	}

	@Override
	public VirtualDiskElement[] listFolder()
	{
		VirtualDiskElement output[] = new VirtualDiskElement[rootFolder.children.size()];
		return rootFolder.children.toArray(output);
	}

	@Override
	public String tree() throws VirtualDiskException
	{
		return tree1(rootFolder, 0, SystemInfo.getInfoFromSystem().isSupportsAnsi());
	}

	@Override
	public String tree(boolean b) throws VirtualDiskException
	{
		return tree1(rootFolder, 0, b);
	}

	@Override
	public String tree(String folder) throws VirtualDiskException
	{
		return tree1(resolveFolderToPath(folder), 0, SystemInfo.getInfoFromSystem().isSupportsAnsi());
	}

	@Override
	public String tree(String folder, boolean b) throws VirtualDiskException
	{
		return tree1(resolveFolderToPath(folder), 0, b);
	}

	String tree1(VirtualFolder vf, int nodenum, boolean isRichANSITerminal) throws VirtualDiskException
	{
		String output = "";
		if (nodenum == 0)
		{
			output += vf.getName() + "\n";
		}
		if (vf.children.size() > 0)
		{
			for (var child : vf.children)
			{

				AttributedStringBuilder builder = new AttributedStringBuilder()
						.style(AttributedStyle.DEFAULT.foreground(nodenum / 3).bold().background(AttributedStyle.WHITE))
						.append(child.getName());
				output += genLine(nodenum) + "├─" + (isRichANSITerminal ? builder.toAnsi() : child.getName()) + "\n";
				if (child instanceof VirtualFolder)
				{
					output += tree1((VirtualFolder) child, nodenum + 3, isRichANSITerminal);
				}
			}
		}
		return output;
	}

	private String genLine(int width)
	{
		String out = "";
		for (int i = 0; i < width / 3; i++)
		{
			out += "│  ";
		}
		return out;
	}

}
