package com.example.nobelprize;

import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Random;

import com.example.nobelobjects.Achievements;
import com.example.nobelobjects.Player;
import com.example.nobelprize.AchievementsAdapter.AchievementData;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.graphics.drawable.Drawable;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class AchievementsMainActivityCustom extends Activity implements OnSharedPreferenceChangeListener{




	private class MainListOnItemClick implements OnItemClickListener
	{
		@Override
		public void onItemClick(AdapterView<?> adapter, View view, int position, long id) {

		}
	}

	private ListView mainList;
	private AchievementsAdapter mainAdapter;
	private String[] trophiesTitles={
			"Use a tip to solve a question",
			"Answer correctly a question without using a tip",
			"Answer 3 question correctly consecutively",
			"Answer correctly 5 questions from each game",
			"Your own Nobel Prize (Obtain every other trophies)"
	};



	private String[] trophiesDescription={
			"a",
			"a",
			"a",
			"a",
			"a"
	};


	private SharedPreferences prefs;
	private Player player;
	private Achievements trophies;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.achievements_main);

		prefs = PreferenceManager.getDefaultSharedPreferences(this);
		prefs.registerOnSharedPreferenceChangeListener(this);
		player = new Player(getApplicationContext(),prefs.getString("username", ""));
		player.resetHasNewTrophies();
		trophies = player.getTrophies();


		ArrayList<AchievementsAdapter.AchievementData> achievementData = new ArrayList<AchievementsAdapter.AchievementData>();
		for(int i = 0; i < trophiesTitles.length; i++)
		{			
			String title = trophiesTitles[i];
			String description = trophiesDescription[i];
			boolean isWon=false;
			if(trophies.get(i).hasTrophy())
				isWon = true;

			achievementData.add(new AchievementsAdapter.AchievementData(i, title, description, isWon));
		}

		mainAdapter = new AchievementsAdapter(getApplicationContext(), achievementData);
		mainList = (ListView)findViewById(R.id.achievements_mainList);
		mainList.setAdapter(mainAdapter);
		mainList.setOnItemClickListener(new MainListOnItemClick());
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {		
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu, menu);
		return true;
	}

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
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
			String key) {
		// TODO Auto-generated method stub
	}
}
