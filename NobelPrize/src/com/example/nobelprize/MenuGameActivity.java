package com.example.nobelprize;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nobelobjects.Player;

public class MenuGameActivity extends Activity implements OnSharedPreferenceChangeListener
{

	private SharedPreferences prefs;
	private TextView usernameView;
	private TextView mcqStat;
	private TextView trueFalseStat;
	private TextView whoAmIStat;
	private Player player;

	private TextView scoreTotal;
	private ImageButton trophees;

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch (item.getItemId())
		{
		case R.id.itemPrefs:
			startActivity(new Intent(this, PreferencesActivity.class));
			break;
		}
		return true;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
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
		player = new Player(getApplicationContext(), prefs.getString("username", "Player1"));
		usernameView = (TextView) findViewById(R.id.activity_main_username);
		usernameView.setText(player.getUsername());
		mcqStat = (TextView) findViewById(R.id.activity_main_MCQ_Stats);
		mcqStat.setText(player.getScoreQCM() + "/" + player.getTotalQCM());
		trueFalseStat = (TextView) findViewById(R.id.activity_main_TrueFalse_Stats);
		trueFalseStat.setText(player.getScoreTrueFalse() + "/" + player.getTotalTrueFalse());
		whoAmIStat = (TextView) findViewById(R.id.activity_main_WhoAmI_Stats);
		whoAmIStat.setText(player.getScorePicture() + "/" + player.getTotalPicture());
		scoreTotal = (TextView) findViewById(R.id.TextViewMenuGame_Mon_score_value);
		scoreTotal.setText(player.getScoreGames() + "/" + player.getTotalGames());

		//bouton trophee
		trophees = (ImageButton) findViewById(R.id.imageButton_trophees);
		//par défaut à on
		player.getTrophies();
		if (player.getHasNewTrophies()==0){
			trophees.setImageResource(R.drawable.btn_star_big_off);
		}
		else{
			trophees.setImageResource(R.drawable.btn_star_big_on);
			Toast.makeText(getApplicationContext(), "New Trophies unlocked!", Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	protected void onResume()
	{
		super.onResume();
		usernameView.setText(player.getUsername());
		mcqStat.setText(player.getScoreQCM() + "/" + player.getTotalQCM());
		trueFalseStat.setText(player.getScoreTrueFalse() + "/" + player.getTotalTrueFalse());
		whoAmIStat.setText(player.getScorePicture() + "/" + player.getTotalPicture());
		scoreTotal.setText(player.getScoreGames() + "/" + player.getTotalGames());

		player.getTrophies();
		if (player.getHasNewTrophies()==0){
			trophees.setImageResource(R.drawable.btn_star_big_off);}
		else{
			trophees.setImageResource(R.drawable.btn_star_big_on);
			Toast.makeText(getApplicationContext(), "New Trophies unchecked!", Toast.LENGTH_SHORT).show();
		}
	}

	public void buttonMCQGameClick(View view)
	{
		Intent intentJouerMCQ = new Intent(getApplicationContext(), MultipleChoiceQuestionActivity.class);
		startActivity(intentJouerMCQ);
		// finish();
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

	public void buttonMenuGameTropheClick(View view)
	{
		//Intent intentTrophees = new Intent(getApplicationContext(), TropheesActivity.class);
		Intent intentTrophees = new Intent(getApplicationContext(), AchievementsMainActivityCustom.class);
		startActivity(intentTrophees);
	}

	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key)
	{
		// Do nothing
		prefs = PreferenceManager.getDefaultSharedPreferences(this);
		prefs.registerOnSharedPreferenceChangeListener(this);
		player = new Player(getApplicationContext(), prefs.getString("username", ""));
		usernameView = (TextView) findViewById(R.id.activity_main_username);
		usernameView.setText(player.getUsername());
	}

}