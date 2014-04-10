package com.example.nobelprize;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

public class MenuGameActivity extends Activity {

	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()){
		case R.id.itemPrefs:
			startActivity(new Intent(this, PreferencesActivity.class));
		break;
		}
		return true;
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu, menu);
		return true;
	}


	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.menu_game_layout);
	}


	public void buttonMCQGameClick(View view)
	{
		Intent intentJouerMCQ = new Intent(getApplicationContext(), MultipleChoiceQuestionActivity.class);
		startActivity(intentJouerMCQ);
	}


	public void buttonTrueFalseGameClick(View view)
	{
		Intent intentJouerTrueFalse = new Intent(getApplicationContext(), TrueFalseGameActivity.class);
		startActivity(intentJouerTrueFalse);
	}	
}