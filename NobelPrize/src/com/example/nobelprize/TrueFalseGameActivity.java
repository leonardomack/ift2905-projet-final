package com.example.nobelprize;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nobelobjects.TrueFalseQuestion;

public class TrueFalseGameActivity extends Activity implements OnSharedPreferenceChangeListener{
	
	private int score;
	private final String TAG = "TrueFalseGameActivity";
	private ArrayList<TrueFalseQuestion> questions;
	private TrueFalseQuestion currentQuestion;
	private TrueFalseGameAPI api;
	private boolean finishedLoading;
	private Vibrator vib;
	private SharedPreferences prefs;
    TrueFalseGamePagerAdapter pagerAdapter;
    ViewPager viewPager;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().requestFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		setProgressBarIndeterminateVisibility(false);
		setContentView(R.layout.true_false_game_layout);
		new sendRequestForQuestions().execute();
		vib = (Vibrator) getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);
		finishedLoading = false;
		prefs = PreferenceManager.getDefaultSharedPreferences(this);
		prefs.registerOnSharedPreferenceChangeListener(this);
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
	
	class sendRequestForQuestions extends AsyncTask<String, Integer, String>
	{
		@Override
		protected String doInBackground(String... params)
		{
			try
			{
				api = new TrueFalseGameAPI();
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
			setProgressBarIndeterminateVisibility(false);
			questions=api.getQuestions();
			//updateQuestion();
			finishedLoading=true;
			viewPager = (ViewPager)findViewById(R.id.true_false_pager);
			pagerAdapter = new TrueFalseGamePagerAdapter();
			viewPager.setAdapter(pagerAdapter);
		}

	}


	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
			String key) {
		//Do nothing
	}
	
	private class TrueFalseGamePagerAdapter extends PagerAdapter implements OnClickListener{
		private TextView currentQuestionNumber;
		
		@Override
		public int getCount() {
			return TrueFalseGameAPI.getAmountOfQuestion();
		}

	    @Override
	    public boolean isViewFromObject(View arg0, Object arg1) {
	    	return arg0 == ((View) arg1);
	    }

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View layout = inflater.inflate(R.layout.true_false_game_layout_page, null);
			TextView question = (TextView) layout.findViewById(R.id.TextViewTrueFalse_Question);
			TextView questionNumber = (TextView)layout.findViewById(R.id.TextViewTrueFalse_QNumber);
			Button trueButton = (Button)layout.findViewById(R.id.ButtonTrueFalseGame_True);
			Button falseButton = (Button)layout.findViewById(R.id.ButtonTrueFalseGame_False);
			trueButton.setOnClickListener(this);
			falseButton.setOnClickListener(this);
			Log.d(TAG, questions.get(position).getQuestionString());
			question.setText(questions.get(position).getQuestionString());
			questionNumber.setText("Question #"+(position+1)+" of "+questions.size());
			
	        ((ViewPager) container).addView(layout, 0);
	        return layout;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			//((ViewPager) container).removeView((View) object);
		}

		
		
		@Override
		public void setPrimaryItem(ViewGroup container, int position,
				Object object) {
			View currentView = (View)object;
			currentQuestionNumber= (TextView) currentView.findViewById(R.id.TextViewTrueFalse_QNumber);
			super.setPrimaryItem(container, position, object);
		}

		@Override
		public void onClick(View v) {
			if(finishedLoading){
				switch(v.getId()){
				case R.id.ButtonTrueFalseGame_True :
					handleClick(true);
					break;
				case R.id.ButtonTrueFalseGame_False :
					handleClick(false);
					break;
				}
			}
		}	
		
		public void handleClick(boolean answer){
			int pos = viewPager.getCurrentItem();
			currentQuestion = questions.get(pos);
			//TextView currentQuestionNumber = (TextView) findViewById(R.id.TextViewTrueFalse_QNumber);
			if(!currentQuestion.isAnswered){
				currentQuestion.setAnswered(true);
				if(currentQuestion.getAnswer() == answer){
					Log.d(TAG, "Answered correctly");
					currentQuestion.setAnsweredCorrectly(true);
					score++;
					//update database
					currentQuestionNumber.setTextColor(Color.GREEN);
					Toast.makeText(getApplicationContext(), R.string.TrueFalseGame_RightAnswerToast, Toast.LENGTH_SHORT).show();
				}
				else{
					Log.d(TAG, "Answered wrongly");
					currentQuestion.setAnsweredCorrectly(false);
					if(prefs.getBoolean("vibrate", true))
						vib.vibrate(500);
					//update database
					currentQuestionNumber.setTextColor(Color.RED);
					Toast.makeText(getApplicationContext(), R.string.TrueFalseGame_WrongAnswerToast, Toast.LENGTH_SHORT).show();
				}
				
				int j = 0;
				for(int i=0; i < questions.size(); i++){
					if(questions.get(i).isAnswered)
						j++;
				}
				if(j==questions.size()){
					Toast.makeText(getApplicationContext(), "Completed : "+score+"/"+questions.size(), Toast.LENGTH_SHORT).show();
					Intent goBackToGameMenu = new Intent(getApplicationContext(), MenuGameActivity.class);
					startActivity(goBackToGameMenu);
				}
				
				if(pos<questions.size())
					viewPager.setCurrentItem(pos+1);
			}
				
		}
		
	}
		
}
