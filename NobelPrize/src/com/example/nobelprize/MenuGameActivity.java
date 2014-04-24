package com.example.nobelprize;

import com.example.nobelobjects.Player;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

public class MenuGameActivity extends Activity implements OnSharedPreferenceChangeListener{

	private SharedPreferences prefs;
	private TextView usernameView;
	private TextView mcqStat;
	private TextView trueFalseStat;
	private TextView whoAmIStat;
	private Player player;
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
		prefs = PreferenceManager.getDefaultSharedPreferences(this);
		prefs.registerOnSharedPreferenceChangeListener(this);
		player = new Player(getApplicationContext(),prefs.getString("username", ""));
		usernameView = (TextView)findViewById(R.id.activity_main_username);
		usernameView.setText(player.getUsername());
		mcqStat = (TextView)findViewById(R.id.activity_main_MCQ_Stats);
		mcqStat.setText(player.getScoreQCM()+"/"+player.getTotalQCM());
		trueFalseStat = (TextView)findViewById(R.id.activity_main_TrueFalse_Stats);
		trueFalseStat.setText(player.getScoreTrueFalse()+"/"+player.getTotalTrueFalse());
		whoAmIStat = (TextView)findViewById(R.id.activity_main_WhoAmI_Stats);
		whoAmIStat.setText(player.getScorePicture()+"/"+player.getTotalPicture());
	}


	@Override
	protected void onResume() {
		super.onResume();
		usernameView.setText(player.getUsername());
		mcqStat.setText(player.getScoreQCM()+"/"+player.getTotalQCM());
		trueFalseStat.setText(player.getScoreTrueFalse()+"/"+player.getTotalTrueFalse());
		whoAmIStat.setText(player.getScorePicture()+"/"+player.getTotalPicture());
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
	
	public void buttonWhoAmIGameClick(View view)
	{
		Intent intentJouerWhoAmI = new Intent(getApplicationContext(), WhoAmIGameActivity.class);
		startActivity(intentJouerWhoAmI);
	}

	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
			String key) {
		player = new Player(getApplicationContext(),prefs.getString("username", ""));
		usernameView.setText(player.getUsername());
		mcqStat.setText(player.getScoreQCM()+"/"+player.getTotalQCM());
		trueFalseStat.setText(player.getScoreTrueFalse()+"/"+player.getTotalTrueFalse());
		whoAmIStat.setText(player.getScorePicture()+"/"+player.getTotalPicture());
	}	
}