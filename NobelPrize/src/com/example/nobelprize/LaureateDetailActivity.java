package com.example.nobelprize;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import com.example.nobelobjects.Laureate;
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
			Laureate testL = new DownloadLaureateTask().execute(id).get();

			Log.d(TAG, "Laureate firstname is : " + testL.getFirstname());
		}
		catch (Exception e)
		{

		}
	}

}
