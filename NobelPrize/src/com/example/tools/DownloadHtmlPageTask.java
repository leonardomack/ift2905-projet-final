package com.example.tools;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import android.os.AsyncTask;

public class DownloadHtmlPageTask extends AsyncTask<String, Void, String>
{
	String url = null;
	String html = null;

	@Override
	protected String doInBackground(String... urls)
	{
		this.url = urls[0];
		html = "";

		try
		{
			InputStream is = (InputStream) new URL(url).getContent();
			html = convertStreamToString(is);
			return html;
		}
		catch (MalformedURLException e)
		{
			e.printStackTrace();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}

		return html;
	}

	// http://stackoverflow.com/questions/4457492/simple-http-client-example-in-android
	private static String convertStreamToString(InputStream is)
	{
		/*
		 * To convert the InputStream to String we use the
		 * BufferedReader.readLine() method. We iterate until the BufferedReader
		 * return null which means there's no more data to read. Each line will
		 * appended to a StringBuilder and returned as String.
		 */
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		StringBuilder sb = new StringBuilder();

		String line = null;
		try
		{
			while ((line = reader.readLine()) != null)
			{
				sb.append(line + "\n");
			}
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		finally
		{
			try
			{
				is.close();
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
		return sb.toString();
	}
}