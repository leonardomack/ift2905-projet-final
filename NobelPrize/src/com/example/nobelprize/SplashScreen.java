package com.example.nobelprize;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;

public class SplashScreen extends Activity
{

	private boolean backButtonPressed;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash_screen);

		// Cacher la ActionBar et faire la reference a l'actionbar correcte
		// requestWindowFeature(Window.FEATURE_NO_TITLE);
		// setContentView(R.layout.activity_splash_screen);

		Handler handlerNewPage = new Handler();
		handlerNewPage.postDelayed(new Runnable()
		{
			@Override
			public void run()
			{
				if (!backButtonPressed)
				{
					Intent intent = new Intent(SplashScreen.this, MainActivity.class);
					SplashScreen.this.startActivity(intent);
				}
				finish();
			}

		}, 100);// The waiting time in miliseconds
	}

	@Override
	public void onBackPressed()
	{
		backButtonPressed = true;
		super.onBackPressed();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		// getMenuInflater().inflate(R.menu.start_screen, menu);
		return true;
	}

}
