package com.atomwoz.execit.base;

import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;

import com.atomwoz.execit.commands.BackgroundCommand;
import com.atomwoz.execit.commands.ChageDirCommand;
import com.atomwoz.execit.commands.ClearScreenCommand;
import com.atomwoz.execit.commands.MakeVolumeCommand;
import com.atomwoz.execit.commands.Print;
import com.atomwoz.execit.commands.PwdCommand;
import com.atomwoz.execit.commands.ReadCommand;
import com.atomwoz.execit.commands.ShowDisks;
import com.atomwoz.execit.commands.TokenizeTest;
import com.atomwoz.execit.commands.WriteCommand;
import com.atomwoz.execit.commands.YesCommand;
import com.atomwoz.execit.observers.ConfirmObserver;
import com.atomwoz.execit.observers.TestObserver;
import com.atomwoz.execit.observers.TimeoutObserver;

class NameMapper implements Serializable
{
	private static final long serialVersionUID = 1438466963545266514L;

	private static HashSet<Pair<String[], Class<? extends CommandBase>>> resolver = new HashSet<>();
	private static HashMap<String, AbstractObserver> observerResolver = new HashMap<String, AbstractObserver>();

	static
	{
		resolver.add(cmd(new String[]
		{ "print", "println" }, Print.class));
		resolver.add(cmd(new String[]
		{ "yes", "garbage", "printloop" }, YesCommand.class));
		resolver.add(cmd(new String[]
		{ "tokentest", "tokenizetest" }, TokenizeTest.class));
		resolver.add(cmd(new String[]
		{ "mkvol" }, MakeVolumeCommand.class));
		resolver.add(cmd(new String[]
		{ "listdisks", "listroots", "listdrives", "showdrives", "showvolumes", "showroots", "showdisks", "diskinfo" },
				ShowDisks.class));
		resolver.add(cmd(new String[]
		{ "write", "wr" }, WriteCommand.class));
		resolver.add(cmd(new String[]
		{ "read", "re", "cat" }, ReadCommand.class));
		resolver.add(cmd(new String[]
		{ "pwd", "path" }, PwdCommand.class));
		resolver.add(cmd(new String[]
		{ "bg", "background" }, BackgroundCommand.class));
		resolver.add(cmd(new String[]
		{ "cd", "changedirectory" }, ChageDirCommand.class));
		resolver.add(cmd(new String[]
		{ "cls", "clear" }, ClearScreenCommand.class));
		observerResolver.put("observe", new TestObserver());
		observerResolver.put("timeout", new TimeoutObserver());
		observerResolver.put("confirm", new ConfirmObserver());
	}

	public static HashMap<String, AbstractObserver> getObserverRegister()
	{
		return observerResolver;
	}

	public static HashSet<Pair<String[], Class<? extends CommandBase>>> getCommandRegister()
	{
		return resolver;
	}

	private static Pair<String[], Class<? extends CommandBase>> cmd(String[] strArray, Class<? extends CommandBase> arr)
	{
		return new Pair<String[], Class<? extends CommandBase>>(strArray, arr);
	}
}