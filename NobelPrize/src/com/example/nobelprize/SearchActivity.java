package com.example.nobelprize;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.SimpleAdapter.ViewBinder;

public class SearchActivity extends Activity {

	private String name;
	private int year;
	private String category;
	private String gender;
	private ListView mainList;
	private SimpleAdapter mainAdapter;
	
	private class SearchListOnItemClick implements OnItemClickListener
	{

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			// TODO Auto-generated method stub
			
		}
		
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.search_activity);
		
		ArrayList<HashMap<String,Object>> items = new ArrayList<HashMap<String,Object>>();
		
		HashMap<String, Object> element = new HashMap<String, Object>();
		element.put("name", "Albert Einstein");
		element.put("picture", R.drawable.einstein);
		items.add(element);
		HashMap<String, Object> element2 = new HashMap<String, Object>();
		element2.put("name", "Test McTest");
		element2.put("picture", R.drawable.ic_launcher);
		items.add(element2);
		HashMap<String, Object> element3 = new HashMap<String, Object>();
		element3.put("name", "Test McTest");
		element3.put("picture", R.drawable.einstein);
		items.add(element3);

		
		mainAdapter = new SimpleAdapter(getApplicationContext(), items, R.layout.search_list_view,
				new String[] { "picture", "name"},
				new int[]  { R.id.picture, R.id.searchViewName });
		
		mainAdapter.setViewBinder(VIEW_BINDER);
		
		mainList = (ListView)findViewById(R.id.mainList);
		mainList.setAdapter(mainAdapter);
		mainList.setOnItemClickListener(new SearchListOnItemClick());
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	public void searchLaureates(View view){
		EditText edit = (EditText)findViewById(R.id.nameSearch);
		name = edit.getText().toString();
		edit = (EditText)findViewById(R.id.dateSearch);
		String yearString = edit.getText().toString();
		if(yearString.length() != 4)
			year = -1;
		else
			year = Integer.parseInt(yearString);
		Spinner categorySpinner = (Spinner)findViewById(R.id.fieldSearch);
		Spinner genderSpinner = (Spinner)findViewById(R.id.genderSearch);
		category = categorySpinner.getSelectedItem().toString();
		gender = genderSpinner.getSelectedItem().toString();
		Toast.makeText(this.getApplicationContext(), name + "|"+year+"|"+category+"|"+gender, Toast.LENGTH_SHORT).show();
		new SendRequestForNobelPrize().execute();
	}
	
	private static final ViewBinder VIEW_BINDER = new ViewBinder() {

		@Override
		public boolean setViewValue(View view, Object data, String arg2) {
			
			if(data == null)
				return false;
			
			switch(view.getId())
			{
			case R.id.searchViewName:
				((TextView)view).setText((String)data);
				break;
			case R.id.picture:
				((ImageView)view).setImageResource((Integer)data);
				break;
			default:
				return false;
			}
			
			return true;
		}
		
	};
	
	class SendRequestForNobelPrize extends AsyncTask<String, Integer, String>{

		@Override
		protected String doInBackground(String... params) {
			try{
				SearchLaureateAPI api = new SearchLaureateAPI(name, year, gender, category);
				return "Worked";
			}catch(Exception e){
				e.printStackTrace();
				return "Failed";
			}
		}
		
	}

}
