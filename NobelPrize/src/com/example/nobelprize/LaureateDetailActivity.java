package com.example.nobelprize;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.nobelobjects.Laureate;
import com.example.nobelobjects.Prize;
import com.example.tasks.DownloadImagesTask;
import com.example.tasks.DownloadLaureateTask;

public class LaureateDetailActivity extends Activity
{
	private String TAG;
	private ArrayAdapter<String> arrayAdapter;

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
			TextView winnerBorn = (TextView) findViewById(R.id.laureate_detail_textview_born);
			ListView winnerPrizes = (ListView) findViewById(R.id.laureate_detail_listview_prizes);

			// Set the values
			// Set the image
			String laureateImageUrl = selectedLaureate.getImageUrl(selectedLaureate);
			imgView.setTag(laureateImageUrl);
			new DownloadImagesTask().execute(imgView);

			// Set winner's informations
			winnerName.setText(selectedLaureate.getFirstname() + " " + selectedLaureate.getSurname());
			winnerBorn.setText("Born: " + selectedLaureate.getDateBorn() + ", " + selectedLaureate.getBornCity() + ", " + selectedLaureate.getBornCountry());

			List<String> list = new ArrayList<String>();

			for (Prize p : selectedLaureate.getPrizes())
			{
				list.add(p.getCategory() + " " + p.getYear());
			}

			arrayAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.laureate_details_prizes_listview, list);
			arrayAdapter.setNotifyOnChange(true);
			winnerPrizes.setAdapter(arrayAdapter);

		}
		catch (Exception e)
		{

		}
	}
}
