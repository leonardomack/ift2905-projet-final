package com.example.nobelprize;

import java.util.Calendar;
import java.util.Date;
import java.util.Random;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.util.SparseArray;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nobelobjects.Laureate;
import com.example.nobelobjects.Prize;
import com.example.nobelobjects.Prize.PrizeCategories;
import com.example.tasks.DownloadImagesTask;
import com.example.tasks.DownloadWinnersTask;

public class MainActivity extends Activity
{

	// Le button pour conmpter la quantite de fois que le button retour a ete
	// appuyé
	private Integer exitCounter = 0;

	// Shake sensor - From:
	// http://stackoverflow.com/questions/2317428/android-i-want-to-shake-it
	private SensorManager mSensorManager;
	private float mAccel; // acceleration apart from gravity
	private float mAccelCurrent; // current acceleration including gravity
	private float mAccelLast; // last acceleration including gravity
	private Calendar lastShake;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// Loading a random winner
		loadRandomWinner();

		// Activating the Shake Sensor
		mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
		mSensorManager.registerListener(mSensorListener, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
		mAccel = 0.00f;
		mAccelCurrent = SensorManager.GRAVITY_EARTH;
		mAccelLast = SensorManager.GRAVITY_EARTH;
		lastShake = Calendar.getInstance();
	}

	private void loadRandomWinner()
	{
		// Get the components references
		ImageView imgView = (ImageView) findViewById(R.id.activity_main_image_winner);
		TextView winnerName = (TextView) findViewById(R.id.activity_main_name_winner);
		TextView descriptionName = (TextView) findViewById(R.id.activity_main_description_winner);

		// Setting the random winner
		Random r = new Random();
		int startPrizeYear = 1901;
		int currentYear = Calendar.getInstance().get(Calendar.YEAR);

		Prize prize = new Prize(0, "", null);
		Laureate selectedLaureate = new Laureate(0, "", "");
		String winnerImageUrl = "";
		try
		{
			// Try to find some Random Winner
			PrizeCategories randomCategory = PrizeCategories.getRandomCategory();
			int randomYearPrize = 0;

			// Need to make a while because some years have no winner in that
			// category, so, check another year
			while (selectedLaureate.getId() == 0)
			{
				randomYearPrize = r.nextInt(currentYear - startPrizeYear) + startPrizeYear;
				prize = new Prize(randomYearPrize, randomCategory.toString(), null);

				SparseArray<Laureate> arrayOfLaureates = new DownloadWinnersTask().execute(prize).get();
				int selectedIndex = r.nextInt(arrayOfLaureates.size());
				selectedLaureate = arrayOfLaureates.valueAt(selectedIndex);
			}

			// Setting the name and description
			winnerName.setText(selectedLaureate.getFirstname() + " " + selectedLaureate.getSurname());
			descriptionName.setText("Category : " + randomCategory + " - Year : " + randomYearPrize);

			// Setting the image
			winnerImageUrl = selectedLaureate.getImageUrl(selectedLaureate);
			imgView.setTag(winnerImageUrl);
			new DownloadImagesTask().execute(imgView);
		}
		catch (Exception e)
		{

		}

	}

	public void buttonChercherClick(View view)
	{
		Intent intentChercher = new Intent(getApplicationContext(), SearchActivity.class);
		startActivity(intentChercher);
	}

	public void buttonJouerClick(View view)
	{
		// Intent intentJouer = new Intent(getApplicationContext(),
		// MainActivity.class);
		Intent intentJouer = new Intent(getApplicationContext(), TrueFalseGameActivity.class);
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

	@Override
	protected void onResume()
	{
		super.onResume();
		mSensorManager.registerListener(mSensorListener, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
	}

	@Override
	protected void onPause()
	{
		mSensorManager.unregisterListener(mSensorListener);
		super.onPause();
	}

	private final SensorEventListener mSensorListener = new SensorEventListener()
	{
		@Override
		public void onSensorChanged(SensorEvent event)
		{
			float x = event.values[0];
			float y = event.values[1];
			float z = event.values[2];
			mAccelLast = mAccelCurrent;
			mAccelCurrent = (float) Math.sqrt((double) (x * x + y * y + z * z));
			float delta = mAccelCurrent - mAccelLast;
			mAccel = mAccel * 0.9f + delta; // perform low-cut filter

			if (mAccel > 20.0f)
			{
				Calendar now = Calendar.getInstance();
				now.setTime(new Date());

				long diff = now.getTimeInMillis() - lastShake.getTimeInMillis();
				if (diff >= 750)
				{
					Log.d("MainActivity", "Shake event { mAccel: " + mAccel + "}");
					loadRandomWinner();

					// Setting up the last shake time
					lastShake.setTime(new Date());
				}

			}
		}

		@Override
		public void onAccuracyChanged(Sensor sensor, int accuracy)
		{

		}
	};

}
