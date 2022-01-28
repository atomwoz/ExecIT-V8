package test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;

import com.atomwoz.execit.base.CommandRuntimeExcepiton;
import com.atomwoz.execit.commands.WriteCommand;
import com.atomwoz.execit.virtual.ElementNotExistOnVirtualVolumeException;
import com.atomwoz.execit.virtual.VirtualDiskException;
import com.atomwoz.execit.virtual.VirtualDiskRegister;
import com.atomwoz.execit.virtual.VirtualFile;

class CommandTest
{

	@Test
	void write() throws CommandRuntimeExcepiton, VirtualDiskException
	{
		VirtualDiskRegister.createAndMount("TEST");
		WriteCommand wc = new WriteCommand(Thread.currentThread(), "write");
		wc.doCommand("write", new String[]
		{ "Lorem ipsum ip😊😋sum Loremść", "%TEST/tisch.txt" }, null, null);
		var a = (VirtualFile) VirtualDiskRegister.getElementByAbsoluteName("TEST/tisch.txt", false);
		assertEquals(a.readAll(), "Lorem ipsum ip😊😋sum Loremść");

		VirtualDiskRegister.getVolumeByName("TEST").makeDirectory("a");
		wc.doCommand("write", new String[]
		{ "Lorem ipsum ip😊😋sum Loremść", "%TEST/a/tisch.txt" }, null, null);
		a = (VirtualFile) VirtualDiskRegister.getElementByAbsoluteName("TEST/a/tisch.txt", false);
		assertEquals(a.readAll(), "Lorem ipsum ip😊😋sum Loremść");

	}

	@AfterAll
	public static void after() throws ElementNotExistOnVirtualVolumeException
	{
		VirtualDiskRegister.unmountVolume("TEST");
	}
}
