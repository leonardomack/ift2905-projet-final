package com.example.nobelprize;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.nobelobjects.Laureate;
import com.example.nobelobjects.Prize;
import com.example.tasks.DownloadImagesTask;
import com.example.tasks.DownloadLaureateTask;

public class LaureateDetailActivity extends Activity
{
	private String TAG;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		TAG = "LaureateDetailActivity";
		super.onCreate(savedInstanceState);
		setContentView(R.layout.laureate_detail_layout);

		// Retrieve Id from intent
		int id = -1;
		id = this.getIntent().getExtras().getInt("id");
		Log.d(TAG, "Laureate id is : " + id);
		// use id to retrieve info of laureate

		try
		{
			Laureate selectedLaureate = new DownloadLaureateTask().execute(id).get();
			Prize prize = new Prize();
			if (selectedLaureate.getPrizes().size() > 0)
			{
				prize = selectedLaureate.getPrizes().get(0);
			}

			// Get the components references
			ImageView imgView = (ImageView) findViewById(R.id.laureate_detail_image_winner);
			TextView winnerName = (TextView) findViewById(R.id.laureate_detail_name_winner);

			// Set the values
			String laureateImageUrl = selectedLaureate.getImageUrl(prize);
			imgView.setTag(laureateImageUrl);
			new DownloadImagesTask().execute(imgView);
		}
		catch (Exception e)
		{

		}
	}

}
