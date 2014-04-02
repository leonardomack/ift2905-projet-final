package com.example.nobelprize;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter.LengthFilter;
import android.util.Log;
import android.widget.*;

public class LaureateDetailActivity extends Activity {
	private String TAG;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		String TAG = "LaureateDetailActivity";
		super.onCreate(savedInstanceState);
		setContentView(R.layout.laureate_detail_layout);
		//Retrieve Id from intent
		int id = -1;
		id = this.getIntent().getExtras().getInt("id");
		Log.d(TAG, "Laureate id is : " + id);
		//use id to retrieve info of laureate
	}

}
