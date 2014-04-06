package com.example.nobelprize;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class TrueFalseGameActivity extends Activity{
	
	private String questionNumberString;
	private String question;
	private boolean answerIsTrue;
	private int score;
	private int questionNumber;
	private final int AMOUNT_OF_QUESTIONS = 5;
	private TextView questionTextView;
	private TextView questionNumberTextView;
	private final String TAG = "TrueFalseGameActivity";
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.true_false_game_layout);
		questionTextView = (TextView) findViewById(R.id.TextViewTrueFalse_Question);
		questionNumberTextView = (TextView) findViewById(R.id.TextViewTrueFalse_QNumber);
		questionNumber=1;
		questionNumberString = "Question #"+questionNumber+" of "+AMOUNT_OF_QUESTIONS;
		questionNumberTextView.setText(questionNumberString);
	}
	
	
	
	public void clickedFalse(View view){
		Log.d(TAG, "Clicked false");
	}
	
	public void clickedTrue(View view){
		Log.d(TAG, "Clicked true");
	}

}
