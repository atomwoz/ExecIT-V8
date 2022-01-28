package com.atomwoz.execit.pathEngine;

import java.io.IOException;

import com.atomwoz.execit.virtual.VirtualDiskElement;

public interface IFolder
{
	public VirtualDiskElement touchFile(String path) throws IOException;
	public VirtualDiskElement[] listFolder(String path) throws IOException;
	public VirtualDiskElement[] listFolder() throws IOException;
	public void makeDirectory(String path) throws IOException;
	public void removeElement(String path) throws IOException;
	public void moveElement(String from, String to) throws IOException;
	public void copyElement(String from, String to) throws IOException;
	public String tree(String path) throws IOException;
	public String tree(String path, boolean colorMode) throws IOException;
	public String tree() throws IOException;
	public String tree(boolean colorMode) throws IOException;
}
