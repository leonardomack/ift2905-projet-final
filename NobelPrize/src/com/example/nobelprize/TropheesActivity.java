package com.example.nobelprize;

import com.example.nobelobjects.Achievements;
import com.example.nobelobjects.Player;
import com.example.nobelprize.TrueFalseGameActivity.sendRequestForQuestions;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.Window;
import android.widget.TextView;

public class TropheesActivity extends Activity implements OnSharedPreferenceChangeListener {

	private SharedPreferences prefs;
	private TextView trophy1;
	private TextView trophy2;
	private TextView trophy3;
	private TextView trophy4;
	private TextView trophy5;
	private Drawable completedTrophy;
	private Player player;
	private Achievements trophies;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.trophees_layout);
		prefs = PreferenceManager.getDefaultSharedPreferences(this);
		prefs.registerOnSharedPreferenceChangeListener(this);
		player = new Player(getApplicationContext(),prefs.getString("username", ""));
		player.resetHasNewTrophies();

		trophies = player.getTrophies();
		trophy1 = (TextView) findViewById(R.id.TextViewTrophees_1);
		trophy2 = (TextView) findViewById(R.id.TextViewTrophees_2);
		trophy3 = (TextView) findViewById(R.id.TextViewTrophees_3);
		trophy4 = (TextView) findViewById(R.id.TextViewTrophees_4);
		trophy5 = (TextView) findViewById(R.id.TextViewTrophees_5);
		Drawable starOn = getResources().getDrawable(R.drawable.btn_star_big_on);
		starOn.setBounds(0, 0, starOn.getIntrinsicWidth(), starOn.getIntrinsicHeight());
		if(trophies.get(0).hasTrophy())
			trophy1.setCompoundDrawables(null, null, starOn, null);
		if(trophies.get(1).hasTrophy())
			trophy2.setCompoundDrawables(null, null, starOn, null);
		if(trophies.get(2).hasTrophy())
			trophy3.setCompoundDrawables(null, null, starOn, null);
		if(trophies.get(3).hasTrophy())
			trophy4.setCompoundDrawables(null, null, starOn, null);
		if(trophies.get(4).hasTrophy())
			trophy5.setCompoundDrawables(null, null, starOn, null);
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
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu, menu);
		return true;
	}

	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
			String key) {
		// TODO Auto-generated method stub

	}


}
