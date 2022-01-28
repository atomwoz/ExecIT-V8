package com.atomwoz.execit.base;

import java.util.Set;

public interface Command 
{
	int doCommand(String command, String[] argues, String argue, Set<StartFlag> startFlags) throws CommandRuntimeExcepiton;
}

