package com.example.nobelprize;

import java.util.ArrayList;
import java.util.regex.Pattern;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;

import com.example.nobelAPI.SearchLaureateAPI;
import com.example.nobelobjects.Laureate;

public class SearchActivity extends Activity
{

	private String name;
	private int year;
	private String category;
	private String gender;
	private ListView mainList;
	private ArrayAdapter<String> arrayAdapter;
	private String TAG="SearchActivity";
	ArrayList<String> items;
	SparseArray<Laureate> arrayOfLaureates;

	private class SearchListOnItemClick implements OnItemClickListener
	{

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id)
		{
			int key = 0;
			key = arrayOfLaureates.keyAt(position);
			Laureate l = arrayOfLaureates.get(key);
			Log.d(TAG, "Laureate Clicked is :  " + l.toString());

			Intent showDetailIntent = new Intent(getApplicationContext(), LaureateDetailActivity.class);
			showDetailIntent.putExtra("id", l.getId());
			startActivity(showDetailIntent);
		}

	}

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		getWindow().requestFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		setProgressBarIndeterminateVisibility(false);
		setContentView(R.layout.search_activity);
		setProgressBarIndeterminateVisibility(false);
		TAG = "SearchActivity";
		items = new ArrayList<String>();

		arrayAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.search_list_view2, items);
		arrayAdapter.setNotifyOnChange(true);
		mainList = (ListView) findViewById(R.id.mainList);
		mainList.setAdapter(arrayAdapter);
		mainList.setOnItemClickListener(new SearchListOnItemClick());
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.shakesimulate)
		{
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	public void searchLaureates(View view)
	{
		// Cacher le clavier juste apr�s appuyer sur search
		// Get the components references
		Button buttonSearch = (Button) findViewById(R.id.search_activity_buttonSearch);
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(buttonSearch.getWindowToken(), 0);

		EditText edit = (EditText) findViewById(R.id.nameSearch);
		name = edit.getText().toString();
		edit = (EditText) findViewById(R.id.dateSearch);
		String yearString = edit.getText().toString();
		if (yearString.length() != 4)
			year = -1;
		else
			year = Integer.parseInt(yearString);
		Spinner categorySpinner = (Spinner) findViewById(R.id.fieldSearch);
		Spinner genderSpinner = (Spinner) findViewById(R.id.genderSearch);
		
		category = categorySpinner.getSelectedItem().toString();
		
		Log.d(TAG, "category "+category);
		if (Pattern.matches("all.*",category))
			category = "all";

		Log.d(TAG, "category "+category);

		gender = genderSpinner.getSelectedItem().toString();

		Log.d(TAG, "gender "+gender);
		if (Pattern.matches("all.*",gender))
			gender = "all";
		Log.d(TAG, "gender "+gender);
			
		new SendRequestForNobelPrize().execute();
		// mainList.invalidateViews();
		// mainAdapter.notifyDataSetChanged();
	}

	/*
	 * private static final ViewBinder VIEW_BINDER = new ViewBinder() {
	 * 
	 * @Override public boolean setViewValue(View view, Object data, String
	 * arg2) {
	 * 
	 * if(data == null) return false;
	 * 
	 * switch(view.getId()) { case R.id.searchViewFirstname: case
	 * R.id.searchViewSurname: ((TextView)view).setText((String)data); break;
	 * case R.id.picture: ((ImageView)view).setImageResource((Integer)data);
	 * break; default: return false; }
	 * 
	 * return true; }
	 * 
	 * };
	 */
	class SendRequestForNobelPrize extends AsyncTask<String, Integer, String>
	{
		SearchLaureateAPI api;

		@Override
		protected String doInBackground(String... params)
		{
			try
			{
				api = new SearchLaureateAPI(name, year, gender, category);
				return "Worked";
			}
			catch (Exception e)
			{
				e.printStackTrace();
				return "Failed";
			}
		}

		@Override
		protected void onPreExecute()
		{
			setProgressBarIndeterminateVisibility(true);
		}

		@Override
		protected void onPostExecute(String result)
		{
			// super.onPostExecute(result);
			arrayOfLaureates = new SparseArray<Laureate>();
			arrayOfLaureates = api.getFinalArray();
			items.clear();
			int key = 0;
			for (int i = 0; i < arrayOfLaureates.size(); i++)
			{
				// HashMap<String, Object> element = new HashMap<String,
				// Object>();
				key = arrayOfLaureates.keyAt(i);
				Laureate l = arrayOfLaureates.get(key);
				// element.put("id", l.getId());
				// element.put("firstname", l.getFirstname());
				// element.put("surname", l.getSurname());
				Log.d(TAG, "added laureate #" + l.getId() + " : " + l.toString());
				items.add(l.getFirstname() + " " + l.getSurname());
			}
			setProgressBarIndeterminateVisibility(false);
			arrayAdapter.notifyDataSetChanged();

		}

	}

}
