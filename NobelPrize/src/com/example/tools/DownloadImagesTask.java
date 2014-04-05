package com.example.tools;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.widget.ImageView;

///http://developer.android.com/reference/android/os/AsyncTask.html
public class DownloadImagesTask extends AsyncTask<ImageView, Void, Drawable>
{
	ImageView imageView = null;

	@Override
	protected Drawable doInBackground(ImageView... imageViews)
	{
		this.imageView = imageViews[0];

		try
		{
			InputStream is = (InputStream) new URL(imageView.getTag().toString()).getContent();
			Drawable d = Drawable.createFromStream(is, "src name");
			return d;
		}
		catch (MalformedURLException e)
		{
			e.printStackTrace();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}

		return null;
	}

	@Override
	protected void onPostExecute(Drawable result)
	{
		imageView.setImageDrawable(result);
	}
}
