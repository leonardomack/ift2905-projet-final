package com.example.nobelprize;

import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends Activity
{

	// Le button pour conmpter la quantite de fois que le button retour a ete
	// appuyé
	private Integer exitCounter = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}

	public void buttonChercherClick(View view)
	{
		Intent intentChercher = new Intent(getApplicationContext(), SearchActivity.class);
		startActivity(intentChercher);
	}

	public void buttonJouerClick(View view)
	{
		Intent intentJouer = new Intent(getApplicationContext(), MainActivity.class);
		startActivity(intentJouer);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		// getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public void onBackPressed()
	{
		if (exitCounter < 1)
		{
			// Not pressed 2 times yet
			exitCounter++;

			// Montrer le message pour appuyer une autre fois pour sortir
			Toast.makeText(this, R.string.MainActivity_toast_sortir, Toast.LENGTH_LONG).show();

			Handler handlerNewPage = new Handler();
			handlerNewPage.postDelayed(new Runnable()
			{
				@Override
				public void run()
				{
					exitCounter = 0;
				}

			}, 4000);// 4 secondes pour faire le couter a 0 une autre fois
		}
		else
		{
			// Il veut sortir
			finish();
		}
	}
}
