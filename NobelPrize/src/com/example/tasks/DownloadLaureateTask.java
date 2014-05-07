package com.example.tasks;

import android.os.AsyncTask;
import android.util.SparseArray;

import com.example.nobelAPI.SearchLaureateAPI;
import com.example.nobelobjects.Laureate;

public class DownloadLaureateTask extends AsyncTask<Integer, Void, Laureate>
{
	@Override
	protected Laureate doInBackground(Integer... ids)
	{
		int selectedId = ids[0];
		SparseArray<Laureate> arrayOfLaureates = new SparseArray<Laureate>();
		Laureate laureate = new Laureate();

		try
		{
			SearchLaureateAPI api = new SearchLaureateAPI(selectedId);

			arrayOfLaureates = api.getFinalArray();
			if (arrayOfLaureates.size() > 0)
			{
				laureate = arrayOfLaureates.valueAt(0);
			}

			return laureate;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return laureate;
		}
	}
}