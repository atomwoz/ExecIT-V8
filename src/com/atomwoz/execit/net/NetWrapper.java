package com.atomwoz.execit.net;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

public class NetWrapper
{
	public static String[] readRemoteContent(String URL) throws IOException
	{
		URL url = new URL(URL);
		BufferedReader br = new BufferedReader(new InputStreamReader(url.openConnection().getInputStream()));
		return br.lines().toArray(String[]::new);
	}
}
