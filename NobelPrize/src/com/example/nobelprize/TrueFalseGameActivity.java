package com.example.nobelprize;

import java.util.ArrayList;

import android.graphics.Color;

import com.example.nobelobjects.TrueFalseQuestion;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.support.v4.view.GestureDetectorCompat;
import android.util.Log;
import android.view.GestureDetector.OnGestureListener;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

public class TrueFalseGameActivity extends Activity implements OnGestureListener, OnSharedPreferenceChangeListener{
	
	private int score;
	private int questionNumber;
	private final String TAG = "TrueFalseGameActivity";
	private ArrayList<TrueFalseQuestion> questions;
	private TrueFalseQuestion currentQuestion;
	private TrueFalseGameAPI api;
	private boolean finishedLoading;
	
	private TextView questionTextView;
	private TextView questionNumberTextView;
	private Vibrator vib;
	
	private SharedPreferences prefs;
	
	private GestureDetectorCompat mDetector;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().requestFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		setProgressBarIndeterminateVisibility(false);
		setContentView(R.layout.true_false_game_layout);
		new sendRequestForQuestions().execute();
		questionNumber=1;
		questionTextView = (TextView) findViewById(R.id.TextViewTrueFalse_Question);
		questionNumberTextView = (TextView) findViewById(R.id.TextViewTrueFalse_QNumber);
		mDetector = new GestureDetectorCompat(this, this);
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
	
	private void updateQuestion(){
		currentQuestion = questions.get(questionNumber-1);
		questionNumberTextView.setText("Question #"+questionNumber+" of "+questions.size());
		if(currentQuestion.isAnswered && currentQuestion.isAnsweredCorrectly)
			questionNumberTextView.setTextColor(Color.GREEN);
		else if(currentQuestion.isAnswered && !currentQuestion.isAnsweredCorrectly)
			questionNumberTextView.setTextColor(Color.RED);
		else
			questionNumberTextView.setTextColor(Color.BLACK);
		questionTextView.setText(currentQuestion.getQuestionString());
	}
	
	public void clickedTrue(View view){
		if(finishedLoading)
			handleAnswerClick(true);
	}
	
	public void clickedFalse(View view){
		if(finishedLoading)
			handleAnswerClick(false);
	}
	
	public void handleAnswerClick(boolean answer){
		if(!currentQuestion.isAnswered){
			currentQuestion.setAnswered(true);
			if(currentQuestion.getAnswer() == answer){
				Log.d(TAG, "Answered correctly");
				currentQuestion.setAnsweredCorrectly(true);
				score++;
				//update database
				Toast.makeText(getApplicationContext(), R.string.TrueFalseGame_RightAnswerToast, Toast.LENGTH_SHORT).show();
			}
			else{
				Log.d(TAG, "Answered wrongly");
				currentQuestion.setAnsweredCorrectly(false);
				if(prefs.getBoolean("vibrate", true))
					vib.vibrate(500);
				//update database
				Toast.makeText(getApplicationContext(), R.string.TrueFalseGame_WrongAnswerToast, Toast.LENGTH_SHORT).show();
			}
			updateQuestion();
		}
		int nbQuestionsAnswered = 0;
		for(int i=0; i<questions.size(); i++){
			if(questions.get(i).isAnswered)
				nbQuestionsAnswered++;
		}
		
		if(nbQuestionsAnswered==questions.size()){
			Toast.makeText(getApplicationContext(), "Complete : " + score + "/" + questions.size(), Toast.LENGTH_SHORT).show();
			//Intent retourPageJeux = new Intent(getApplicationContext(), ???????.class);
			//startActivity(retourPageJeux);
		}
		else if(questionNumber< questions.size()){
			questionNumber++;
			updateQuestion();
		}
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		this.mDetector.onTouchEvent(event);
		return super.onTouchEvent(event);
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
			updateQuestion();
			finishedLoading=true;
		}

	}

	@Override
	public boolean onDown(MotionEvent e) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public void onShowPress(MotionEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onLongPress(MotionEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		try {
			if(e1.getX() < e2.getX()){
				goLeft();
			}
			else if(e1.getX() > e2.getX()){
				goRight();
			}
        } catch (Exception e) {

        }
        return false;
	}
	
	public void goLeft(){
		Log.d(TAG, "swiped Left");
		if(questionNumber>1){
			questionNumber--;
			updateQuestion();
		}
		
	}
	
	public void goRight(){
		Log.d(TAG, "swiped Right");
		if(questionNumber<questions.size()){
			questionNumber++;
			updateQuestion();
		}
	}

	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
			String key) {
		//Do nothing
	}

}
