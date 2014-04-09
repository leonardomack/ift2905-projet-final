package com.example.nobelprize;

import java.util.Calendar;
import java.util.Random;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
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

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

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
		try
		{
			// Try to find some Random Winner
			PrizeCategories randomCategory = PrizeCategories.getRandomCategory();
			int randomYearPrize = 0;

			while (selectedLaureate.getId() == 0)
			{
				randomYearPrize = r.nextInt(currentYear - startPrizeYear) + startPrizeYear;
				prize = new Prize(randomYearPrize, randomCategory.toString(), null);

				SparseArray<Laureate> arrayOfLaureates = new DownloadWinnersTask().execute(prize).get();
				int selectedIndex = r.nextInt(arrayOfLaureates.size());
				selectedLaureate = arrayOfLaureates.valueAt(selectedIndex);
			}

			winnerName.setText(selectedLaureate.getFirstname() + " " + selectedLaureate.getSurname());
			descriptionName.setText("Category : " + randomCategory + " - Year : " + randomYearPrize);
		}
		catch (Exception e)
		{

		}

		String winnerImageUrl = "";

		try
		{
			winnerImageUrl = selectedLaureate.getImageUrl(selectedLaureate);
		}
		catch (Exception e)
		{

		}

		imgView.setTag(winnerImageUrl);
		new DownloadImagesTask().execute(imgView);

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

}
