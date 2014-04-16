package com.example.nobelprize;

import java.util.ArrayList;
import java.util.Collections;


import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nobelAPI.WhoAmIGameAPI;
import com.example.nobelobjects.Player;
import com.example.nobelobjects.TrueFalseQuestion;
import com.example.nobelobjects.WhoAmIQuestion;

public class WhoAmIGameActivity extends Activity implements OnPageChangeListener, OnSharedPreferenceChangeListener{

	private int score;
	private final String TAG = "WhoAmIGameActivity";
	private WhoAmIGameAPI api=null;
	private ArrayList <WhoAmIQuestion> questions;


	private boolean finishedLoading;
	ViewPager pager;
	MonPagerAdapter monAdapter;

	Context ctx;

	TextView tvInfo;



	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().requestFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		setProgressBarIndeterminateVisibility(false);
		setContentView(R.layout.who_am_i_game_layout);

		Log.v(TAG,"avant la requête");

		ctx=this;

		//new sendRequestForQuestionsWho().execute();


		new sendRequestForQuestionsWho().execute();
		
		//WhoAmIGameAPI api2 = new WhoAmIGameAPI();

		//Log.v(TAG,"api2 \n"+api2.getQuestions());
		
		Log.v(TAG,"aprèsla requête");
		
		finishedLoading = false;
		tvInfo=(TextView)findViewById(R.id.textView1);
		tvInfo.setText("this is it");

		Log.v(TAG,"à la fin du constructeur");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}





	class sendRequestForQuestionsWho extends AsyncTask<String, Integer, String>
	{
		@Override
		protected String doInBackground(String... params)
		{
			
			
			
			try
			{
				//do{
					Log.v(TAG,"DEBUT REQUETE");
					
				api = new WhoAmIGameAPI();
				
			//	}while(api==null);
				

				Log.v(TAG,"FIN REQUETE");
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

			Log.v(TAG,"onPreExecute");
		}

		@Override
		protected void onPostExecute(String result)
		{
			setProgressBarIndeterminateVisibility(false);

			Log.v(TAG,"onPostExecute");

			//updateQuestion();

			finishedLoading=true;

			pager=(ViewPager)findViewById(R.id.who_am_i_game_pager);
			monAdapter = new MonPagerAdapter();
			pager.setAdapter(monAdapter);
			pager.setOnPageChangeListener((OnPageChangeListener) ctx);



			Log.v(TAG,"avant shuffle");
			api.shuffleQuestions();
			questions = api.getQuestions();

			Log.v(TAG,"apres shuffle");

		}
	}









	class MonPagerAdapter extends PagerAdapter {

		LayoutInflater inflater;

		MonPagerAdapter() {
			// on va utiliser les services d'un "inflater"
			inflater= (LayoutInflater)ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}
		/**
		 * faire implémenter interface contenant CST globales comme les nombre de quqestions 
		 * (pareil pour les classes question etc...)
		 */
		@Override
		public int getCount() {
			return 5;
		}

		@Override
		public boolean isViewFromObject(View v, Object ob) { return v==(View)ob; }

		@Override
		public Object instantiateItem(View container, int position) {

			Log.v(TAG,"instantiate item");
			//compte a partir de 0
			Toast.makeText(ctx,"Page "+(position+1)+"créée", Toast.LENGTH_LONG).show();

			LinearLayout page;

			page=(LinearLayout)inflater.inflate(R.layout.who_am_i_game_layout_page, null);


			//numero de question = position + 1
			//on récupère la question
			WhoAmIQuestion question = questions.get(position);

			if (question == null)
				return page;
			TextView tvQuestion=(TextView)page.findViewById(R.id.question);
			//NULLPointer
			tvQuestion.setText("Question "+(position+1)+"\n"+question.getQuestionString());

			ArrayList<String> printedAnswers = question.getPrintedAnswers();

			Button b1 = (Button)page.findViewById(R.id.button1);
			Button b2 = (Button)page.findViewById(R.id.button2);
			Button b3 = (Button)page.findViewById(R.id.button3);
			Button b4 = (Button)page.findViewById(R.id.button4);

			b1.setText(printedAnswers.get(0));
			b2.setText(printedAnswers.get(1));
			b3.setText(printedAnswers.get(2));
			b4.setText(printedAnswers.get(3));

			//tvTitre.setTextColor(Color.BLUE);


			// On doit ensuite l'ajouter au parent fourni 
			//pourquoi indic 0 ??
			((ViewPager)container).addView(page,0);

			return page;
		}

		@Override
		public void destroyItem(View collection, int position, Object view) {
			Toast.makeText(ctx,"DESTROY page"+(position+1),Toast.LENGTH_SHORT).show();

			// On commence par enlever notre page du parent
			((ViewPager) collection).removeView((View) view);
		}

		@Override
		public void finishUpdate(View v) {}

		@Override
		public void restoreState(Parcelable arg0, ClassLoader arg1) {}

		@Override
		public Parcelable saveState() { return null; }

		@Override
		public void startUpdate(View arg0) {}

	}



	//autres methodes

	@Override
	public void onPageScrollStateChanged(int arg0) { }

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) { }

	@Override
	public void onPageSelected(int position) {
		tvInfo.setText("La page "+(position+1)+" a ete choisie!!!!");
	}

	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
			String key) {
		// TODO Auto-generated method stub

	}

}
