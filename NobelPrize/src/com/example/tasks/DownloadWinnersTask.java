package com.example.tasks;

import android.os.AsyncTask;
import android.util.SparseArray;

import com.example.nobelobjects.Laureate;
import com.example.nobelobjects.Prize;
import com.example.nobelprize.SearchLaureateAPI;

public class DownloadWinnersTask extends AsyncTask<Prize, Void, SparseArray<Laureate>>
{
	@Override
	protected SparseArray<Laureate> doInBackground(Prize... prizes)
	{
		SparseArray<Laureate> arrayOfLaureates = new SparseArray<Laureate>();

		try
		{
			Prize currentPrize = prizes[0];
			SearchLaureateAPI api = new SearchLaureateAPI("", currentPrize.getYear(), "all", currentPrize.getCategory());

			arrayOfLaureates = api.getFinalArray();

			return arrayOfLaureates;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return arrayOfLaureates;
		}
	}
}
