package test;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.IOException;

import org.junit.jupiter.api.Test;

import com.atomwoz.execit.pathEngine.FileName;
import com.atomwoz.execit.pathEngine.PathEngine;
import com.atomwoz.execit.virtual.VirtualDiskElement;
import com.atomwoz.execit.virtual.VirtualDiskException;
import com.atomwoz.execit.virtual.VirtualFile;
import com.atomwoz.execit.virtual.VirtualFolder;
import com.atomwoz.execit.virtual.VirtualVolume;
import com.atomwoz.execit.virtual.VolumeStorageLimitExceesed;

class VirtualDiskTest
{
	@Test
	void testGettingFolderByNames() throws VirtualDiskException, VolumeStorageLimitExceesed
	{
		VirtualFile file = new VirtualFile("test");
		file.write("raz dwa trzy");
		VirtualFile file2 = new VirtualFile("test2");
		VirtualFolder root = new VirtualFolder("root");
		root.addChild(file);
		root.addChild(file2);

		// assertEquals(file.getName(), "test");
		VirtualDiskElement ve = root.getByName("test");
		VirtualFile vf = (VirtualFile) ve;
		assertEquals(vf.readAll(), "raz dwa trzy");

	}

	@Test
	void testVirtualMakeDirectory()
	{
		VirtualFolder virtualFolder = new VirtualFolder("sda");
		VirtualVolume vv = new VirtualVolume(virtualFolder);
		if (vv.existsOnVolume("dupa"))
		{
			fail("Ghost folder!");
		}
		try
		{
			vv.makeDirectory("/dupa");
			vv.makeDirectory("dupa/franek/");
			vv.makeDirectory("dupa/franek/lorem");
			assertEquals(vv.resolveElementToPath("/dupa/franek/lorem/", false).getName(), "lorem");
		}
		catch (VirtualDiskException e)
		{
			fail(e.getMessage());
		}
	}

	@Test
	void readWriteFileTest() throws VirtualDiskException
	{
		VirtualFolder root = new VirtualFolder("jabłko");
		VirtualVolume vv = new VirtualVolume(root);
		VirtualFile file1 = new VirtualFile("obrazek.jpg");
		VirtualFile file2 = new VirtualFile("kuatsy.exe./\\/.txt");
		root.addChild(file1);
		root.addChild(file2);
		file1.write("poci😍g");
		assertEquals("poci😍g", file1.readAll());
		file2.write("");
		file2.write("");
		file2.write("");
		assertTrue(file2.readBytes().length == 0);
		file2.writeBytes(new byte[]
		{ 0x41, 0x62, 0x43, 0x21 });
		assertEquals(vv.resolveFileToPath("//kuatsy.exe..txt").readAll(), "AbC!");
	}

	@Test
	void testRemoveFile() throws VirtualDiskException
	{
		VirtualFile file = new VirtualFile("test");
		file.write("raz dwa trzy");
		VirtualFile file2 = new VirtualFile("test2");
		VirtualFolder root = new VirtualFolder("root");
		VirtualFolder kww = new VirtualFolder("kwiatki");
		VirtualFolder kw = new VirtualFolder("kwiatki");
		VirtualVolume vv = new VirtualVolume(root);
		kw.addChild(file);
		root.addChild(kw);
		root.addChild(file2);
		if (!vv.existsOnVolume("/kwiatki"))
		{
			fail("Not properly configured");
		}
		try
		{
			vv.removeElement("/");
			fail("Not properly removing root configured");
		}
		catch (VirtualDiskException e)
		{
		}
		try
		{
			vv.removeElement("/kwiatki/test/");
			vv.removeElement("kwiatki");
		}
		catch (VirtualDiskException e)
		{
			fail(e.getMessage());
		}
		if (vv.existsOnVolume("/kwiatki") || vv.existsOnVolume("/kwiatki/test"))
		{
			fail("Not removed");
		}
	}

	@Test
	void testVirtualFileResolve() throws VirtualDiskException
	{
		VirtualFile file = new VirtualFile("test");
		file.write("raz dwa trzy");
		VirtualFile file2 = new VirtualFile("test");
		VirtualFolder root = new VirtualFolder("root");
		VirtualFolder kww = new VirtualFolder("kwiatki");
		VirtualFolder kw = new VirtualFolder("kwiatki");
		VirtualVolume vv = new VirtualVolume(root);
		kww.addChild(kw);
		kw.addChild(file);
		root.addChild(kww);
		kww.addChild(file2);
		VirtualDiskElement a = null;
		VirtualDiskElement ab = null;
		VirtualDiskElement abc = null;
		try
		{
			a = vv.resolveElementToPath("////kwiatki\\test\\\\\\\\\\\\\\/", false);
			ab = vv.resolveElementToPath("", false);
			abc = vv.resolveElementToPath("/kwiatki", false);
			assertEquals(a.getName(), "test");
			assertEquals(ab.getName(), "root");
			assertEquals(abc.getName(), "kwiatki");
		}
		catch (VirtualDiskException e)
		{
			fail(e.getMessage());
		}
	}

	@Test
	void testPhysicalBlendingNames() throws IOException
	{
		FileName fn = PathEngine.pathToFileName("C:\\Users/atwoz\\woz.jpg");
		assertEquals("C:\\Users\\atwoz\\woz.jpg", fn.getFileFullPath());
		assertEquals("woz", fn.getName());
		assertEquals(".jpg", fn.getExtension());

	}

	@Test
	void testPhysicalAdvancedNames() throws IOException
	{
		FileName fn = PathEngine.pathToFileName("~/\\\\woz.jpg");
		assertEquals("C:\\Users\\atwoz\\woz.jpg", fn.getFileFullPath());

	}

	@Test
	void testVirtualBlendingNames() throws IOException
	{
		FileName fn = PathEngine.pathToFileName("http://google.com");
		assertEquals("google.com", fn.getFullPath());
		assertEquals("http", fn.getProtocol());
		assertEquals("google", fn.getName());
		assertEquals(".com", fn.getExtension());

	}

	@Test
	void testTouching() throws VirtualDiskException
	{
		VirtualFile f = new VirtualFile("file");
		VirtualFolder root = new VirtualFolder("/root");
		root.addChild(f);
		VirtualVolume vv = new VirtualVolume(root);
		try
		{
			vv.touchFile("/underichtt");
			String s = vv.resolveElementToPath("/underichtt", false).getName();
			assertEquals(s, "underichtt");
		}
		catch (VirtualDiskException e)
		{
			fail(e.getMessage());
			// e.printStackTrace();
		}
	}

	@Test
	void testCopying() throws VirtualDiskException
	{
		VirtualFile file = new VirtualFile("test");
		file.write("raz dwa trzy");
		VirtualFile file2 = new VirtualFile("test");
		VirtualFolder root = new VirtualFolder("root");
		VirtualFolder kww = new VirtualFolder("kwiatki");
		VirtualFolder kw = new VirtualFolder("kwiatki");
		VirtualVolume vv = new VirtualVolume(root);
		root.addChild(kw);
		kw.addChild(file);
		kw.addChild(kww);
		root.addChild(file2);
		try
		{
			vv.copyElement("/kwiatki/test", "/test45");
			vv.resolveElementToPath("/test45", false);
		}
		catch (VirtualDiskException e)
		{
			fail(e.getMessage());
		}
	}

	@Test
	void testMoving() throws VirtualDiskException
	{
		VirtualFile file = new VirtualFile("test");
		file.write("raz dwa trzy");
		VirtualFolder root = new VirtualFolder("root");
		VirtualFolder kww = new VirtualFolder("kwiatki");
		VirtualFolder kw = new VirtualFolder("kwiatki");
		VirtualVolume vv = new VirtualVolume(root);
		root.addChild(kw);
		kw.addChild(file);
		kw.addChild(kww);

		try
		{
			vv.moveElement("/kwiatki/test", "/test45");
			vv.resolveElementToPath("/test45", false);
		}
		catch (VirtualDiskException e)
		{
			fail(e.getMessage());
		}
		try
		{
			vv.resolveElementToPath("/kwiatki/test", false);
			fail();
		}
		catch (VirtualDiskException e)
		{
		}
	}

	@Test
	void treeTest() throws IOException
	{
		String pattern = "kwiatki\n" + "├─placek\n" + "├─kwiatki\n";
		String pattern2 = "kwiatki\n" + "├─kwiatki\n" + "├─placek\n";
		VirtualFile file = new VirtualFile("test");
		file.write("raz dwa trzy");
		VirtualFolder root = new VirtualFolder("root");
		VirtualFolder kww = new VirtualFolder("kwiatki");
		VirtualFolder kw = new VirtualFolder("kwiatki");
		VirtualFile vf = new VirtualFile("placek");
		VirtualVolume vv = new VirtualVolume(root);
		root.addChild(kw);
		kw.addChild(kww);
		kw.addChild(vf);
		root.addChild(file);
		String tree = vv.tree("/kwiatki/kwiatki");
		assertEquals("kwiatki\n", tree);
		tree = vv.tree("/kwiatki/");
		assertTrue(pattern.equals(tree) || pattern2.equals(tree));
	}

	@Test
	void fileSizeLimitTest() throws VirtualDiskException, VolumeStorageLimitExceesed
	{
		VirtualFile file = new VirtualFile("test");
		file.write("raz dwa trzyert ");
		VirtualFolder root = new VirtualFolder("root");
		VirtualFolder kww = new VirtualFolder("kwiatki");
		VirtualFolder kw = new VirtualFolder("kwiatki");
		VirtualVolume vv = new VirtualVolume(root, 35);
		root.addChild(kw);
		kw.addChild(file);
		kw.addChild(kww);
		assertEquals(16, file.getSize());
		try
		{
			file.append("asdfghjklzxcvbnmjsjsjsjsjsdds");
			fail();
		}
		catch (VolumeStorageLimitExceesed e)
		{
		}
	}

	@Test
	void fileCountSizeTest() throws VirtualDiskException
	{
		VirtualFile file = new VirtualFile("test");
		file.write("raz dwa trzyert ");
		VirtualFolder root = new VirtualFolder("root");
		VirtualFolder kww = new VirtualFolder("kwiatki");
		VirtualFolder kw = new VirtualFolder("kwiatki");
		VirtualVolume vv = new VirtualVolume(root, 35);
		root.addChild(kw);
		kw.addChild(file);
		kw.addChild(kww);
		assertEquals(3, root.getCount());
	}

	@Test
	void testSameNameFileAdd() throws VirtualDiskException, VolumeStorageLimitExceesed
	{
		VirtualFile file = new VirtualFile("test");
		file.write("raz dwa trzyert ");
		VirtualFolder root = new VirtualFolder("root");
		VirtualFolder kww = new VirtualFolder("kwiatki");
		VirtualFile vf = new VirtualFile("kwiatki");
		VirtualFolder kw = new VirtualFolder("kwiatki", 40);
		VirtualVolume vv = new VirtualVolume(root);
		root.addChild(kw);
		kw.addChild(file);
		kw.addChild(kww);
		try
		{
			root.addChild(vf);
			fail();
		}
		catch (VirtualDiskException e)
		{
		}
	}

}
