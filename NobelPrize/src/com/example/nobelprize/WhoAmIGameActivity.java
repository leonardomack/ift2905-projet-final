package com.example.nobelprize;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;


import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

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
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nobelAPI.WhoAmIGameAPI;
import com.example.nobelobjects.Laureate;
import com.example.nobelobjects.Player;
import com.example.nobelobjects.TrueFalseQuestion;
import com.example.nobelobjects.WhoAmIQuestion;
import com.example.tasks.DownloadImagesTask;

public class WhoAmIGameActivity extends Activity implements OnPageChangeListener,OnSharedPreferenceChangeListener{

	private int score;
	private final String TAG = "WhoAmIGameActivity";
	private WhoAmIGameAPI questionsGenerator=null;
	private ArrayList <WhoAmIQuestion> questions;
	private WhoAmIQuestion currentQuestion;
	private ArrayList<ArrayList<Laureate>> laureatesList ;
	private boolean finishedLoading;
	ViewPager viewPager;
	MonPagerAdapter monAdapter;

	Context ctx;

	//pour la requete des laureats = on peut affiner pour faire des quizzs thematiques
	private String name;
	private int year;
	private String category;
	private String gender;
	SparseArray<Laureate> arrayOfLaureates;

	private Vibrator vib;
	private SharedPreferences prefs;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().requestFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		setProgressBarIndeterminateVisibility(false);
		setContentView(R.layout.who_am_i_game_layout);

		ctx=this;

		finishedLoading = false;

		new SendRequestForNobelPrizeQuestions().execute();

		vib = (Vibrator) getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);
		prefs = PreferenceManager.getDefaultSharedPreferences(this);
		prefs.registerOnSharedPreferenceChangeListener(this);
	}

	//@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		//getMenuInflater().inflate(R.menu.main, menu);

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


	//Laureats utilisés dans les quesitons...	
	class SendRequestForNobelPrizeQuestions extends AsyncTask<String, Integer, String>
	{
		SearchLaureateAPI api;

		@Override
		protected String doInBackground(String... params)
		{
			try
			{
				//on peut faire des qcm plus sélectifs = ex que sur les nobels d'une certaine catégorie etc...
				name="";
				year=-1;
				gender="all";
				category="all";
				api = new SearchLaureateAPI(name, year, gender, category);









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

			laureatesList = new ArrayList<ArrayList<Laureate>>();
			int number_of_questions = 5;
			int number_of_different_random_laureates = 4;
			// super.onPostExecute(result);
			arrayOfLaureates = new SparseArray<Laureate>();
			arrayOfLaureates = api.getFinalArray();
			int key = 0;
			Random r = new Random();

			do{
				ArrayList<Laureate> laureates = new ArrayList<Laureate>();
				boolean first = true;

				do{
					key = r.nextInt(arrayOfLaureates.size());
					Laureate l = arrayOfLaureates.get(key);	
					//on peut ne tester l'exitence de la photo que ici... pour le premier de chaque liste = reponse 
					//si le premier element de la liste ui est la reponse a une photo vide, alors on ne l'ajoute pas
					try {
						if (first && ( l.getImageUrl(l)==null || l.getImageUrl(l).equals("") ))
						{
							Log.v(TAG, "NON-added laureate #" + l.getId() + " : " + l.toString()+" n' a PAS DE PHOTO");
							continue;
						}
					} catch (Exception e) {
						// TODO Auto-generated catch block						
						e.printStackTrace();
						continue;
					}


					first = false;
					if (!laureates.contains(l) 
							&& l != null 
							&& l.getFirstname() != null && !l.getFirstname().equals("") 
							&& l.getSurname() != null && !l.getSurname().equals("") 
							&& l.getPrizes() != null && l.getPrizes().size() != 0
							)
					{
						laureates.add(l);
						Log.v(TAG, "added laureate #" + l.getId() + " : " + l.toString());
					}	

				}
				while(laureates.size() < number_of_different_random_laureates);

				laureatesList.add(laureates);

			}
			while(laureatesList.size()<number_of_questions);

			questionsGenerator = new WhoAmIGameAPI(laureatesList);	

			finishedLoading=true;

			questionsGenerator.shuffleQuestions();
			questions = questionsGenerator.getQuestions();


			viewPager=(ViewPager)findViewById(R.id.who_am_i_game_pager);
			monAdapter = new MonPagerAdapter();
			viewPager.setAdapter(monAdapter);
			viewPager.setOnPageChangeListener((OnPageChangeListener) ctx);


			//doit on mettre ça a la toute fin ??
			setProgressBarIndeterminateVisibility(false);
		}

	}

	class MonPagerAdapter extends PagerAdapter implements OnClickListener{
		private TextView currentQuestionNumber;
		private ImageView responseImage;


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
			return WhoAmIGameAPI.getAmountOfQuestion();
		}

		@Override
		public boolean isViewFromObject(View v, Object ob) { return v==(View)ob; }

		@Override
		public Object instantiateItem(View container, int position) {
			Log.v(TAG,"instantiate item"+ position);

			View layout;
			layout=(View)inflater.inflate(R.layout.who_am_i_game_layout_page, null);


			//VINCENT
			currentQuestion = questions.get(position);


			TextView question = (TextView) layout.findViewById(R.id.TextViewWhoAmIGame_Question);
			TextView questionNumber = (TextView)layout.findViewById(R.id.TextViewWhoAmIGame_QNumber);
			question.setText(currentQuestion.getQuestionString());
			questionNumber.setText("Question #"+(position+1)+" of "+questions.size());

			ArrayList<String> printedAnswers = currentQuestion.getPrintedAnswers();

			Button b1 = (Button)layout.findViewById(R.id.ButtonWhoAmIGame_button1);
			Button b2 = (Button)layout.findViewById(R.id.ButtonWhoAmIGame_button2);
			Button b3 = (Button)layout.findViewById(R.id.ButtonWhoAmIGame_button3);
			Button b4 = (Button)layout.findViewById(R.id.ButtonWhoAmIGame_button4);


			//on adapte le texteSize  selon la longueur du string ??
			b1.setText(printedAnswers.get(0));
			b2.setText(printedAnswers.get(1));
			b3.setText(printedAnswers.get(2));
			b4.setText(printedAnswers.get(3));


			ImageView photo = (ImageView)layout.findViewById(R.id.ImageViewWhoAmIGame_laureate_photo);
			String laureateImageUrl = currentQuestion.getUrlImage();
			photo.setTag(laureateImageUrl);
			new DownloadImagesTask().execute(photo);			

			question.setText(questions.get(position).getQuestionString());
			questionNumber.setText("Question #"+(position+1)+" of "+questions.size());
			currentQuestionNumber= (TextView) layout.findViewById(R.id.TextViewWhoAmIGame_QNumber);			
			responseImage = (ImageView)layout.findViewById(R.id.WhoAmIGameImageFeedbackQuestion);


			if(currentQuestion.isAnswered){
				if(currentQuestion.isAnsweredCorrectly){
					responseImage.setImageResource(R.drawable.truequestion);
					currentQuestionNumber.setTextColor(Color.GREEN);
				}
				else{
					responseImage.setImageResource(R.drawable.falsequestion);
					currentQuestionNumber.setTextColor(Color.RED);
				}
			}
			else{
				b1.setOnClickListener(this);
				b2.setOnClickListener(this);
				b3.setOnClickListener(this);
				b4.setOnClickListener(this);
			}		

			((ViewPager)container).addView(layout,0);

			return layout;
		}

		@Override
		public void setPrimaryItem(ViewGroup container, int position,
				Object object) {
			View currentView = (View)object;
			currentQuestionNumber= (TextView) currentView.findViewById(R.id.TextViewWhoAmIGame_QNumber);
			responseImage = (ImageView)currentView.findViewById(R.id.WhoAmIGameImageFeedbackQuestion);
			super.setPrimaryItem(container, position, object);
		}

		@Override
		public void destroyItem(View collection, int position, Object view) {
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
		@Override
		public void onClick(View v) {
			if(finishedLoading){
				switch(v.getId()){
				case R.id.ButtonWhoAmIGame_button1 :
					handleClick(1);
					break;
				case R.id.ButtonWhoAmIGame_button2 :
					handleClick(2);
					break;
				case R.id.ButtonWhoAmIGame_button3 :
					handleClick(3);
					break;
				case R.id.ButtonWhoAmIGame_button4 :
					handleClick(4);
					break;				
				}
			}
		}


		public void handleClick(int answer){
			int pos = viewPager.getCurrentItem();
			currentQuestion = questions.get(pos);
			//TextView currentQuestionNumber = (TextView) findViewById(R.id.TextViewTrueFalse_QNumber);
			if(!currentQuestion.isAnswered){
				currentQuestion.setAnswered(true);

				if(currentQuestion.getRightAnswers().contains(currentQuestion.getPrintedAnswers().get(answer-1))){
					Log.d(TAG, "Answered correctly");
					currentQuestion.setAnsweredCorrectly(true);
					score++;
					//update database
					//on garde ça ???
					//ICI
					responseImage.setImageResource(R.drawable.truequestion);
					currentQuestionNumber.setTextColor(Color.GREEN);
					Toast.makeText(getApplicationContext(), R.string.RightAnswerToast, Toast.LENGTH_SHORT).show();
				}
				else{
					Log.d(TAG, "Answered wrongly");
					currentQuestion.setAnsweredCorrectly(false);
					if(prefs.getBoolean("vibrate", true))
						vib.vibrate(500);
					//update database
					responseImage.setImageResource(R.drawable.falsequestion);
					currentQuestionNumber.setTextColor(Color.RED);
					Toast.makeText(getApplicationContext(), R.string.WrongAnswerToast, Toast.LENGTH_SHORT).show();
				}

				int j = 0;
				for(int i=0; i < questions.size(); i++){
					if(questions.get(i).isAnswered)
						j++;
				}
				if(j==questions.size()){
					Toast.makeText(getApplicationContext(), "Completed : "+score+"/"+questions.size(), Toast.LENGTH_SHORT).show();
					String playerName = prefs.getString("username", "");
					Log.d(TAG, "Player name is :" +playerName);
					Player player = new Player(getApplicationContext(),prefs.getString("username", ""));
					Log.d(TAG, "player score was : "+player.toString());
					player.addScorePicture(score, questions.size());
					Log.d(TAG, "player score is now : "+player.toString());
					Intent goBackToGameMenu = new Intent(getApplicationContext(), MenuGameActivity.class);
					startActivity(goBackToGameMenu);
				}

				//	if(pos<questions.size())
				//	viewPager.setCurrentItem(pos+1);
			}

		}

	}



	//autres methodes

	@Override
	public void onPageScrollStateChanged(int arg0) { }

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) { }

	@Override
	public void onPageSelected(int position) {
	}

	//	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
			String key) {
		// TODO Auto-generated method stub

	}

}
