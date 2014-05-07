package com.example.nobelprize;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

import com.example.nobelAPI.GlobalConstants;
import com.example.nobelAPI.MultipleChoiceGameAPI;
import com.example.nobelAPI.SearchLaureateAPI;
import com.example.nobelAPI.GlobalConstants.buttonState;
import com.example.nobelobjects.Laureate;
import com.example.nobelobjects.MultipleChoiceQuestion;
import com.example.nobelobjects.Player;
import com.example.nobelobjects.WhoAmIQuestion;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
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
import android.view.ViewGroup;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MultipleChoiceQuestionActivity extends Activity implements OnPageChangeListener,OnSharedPreferenceChangeListener,GlobalConstants{

	private int score;

	private final String TAG = "MultipleChoiceGameActivity";
	private ArrayList<ArrayList<Laureate>> laureatesList ;
	private MultipleChoiceGameAPI questionsGenerator=null;
	private ArrayList<MultipleChoiceQuestion> questions;
	private boolean finishedLoading;

	ViewPager viewPager;
	MonPagerAdapter monAdapter;
	Context ctx;

	private boolean first = true;

	private MultipleChoiceQuestion currentQuestion;
	private int currentQuestionNumber;


	private int totalConsecutiveCorrect;
	//initialiser ça de manière dynamique plutôt
	private boolean [] cluesGiven ;
	private buttonState [][] buttonStateTab ;


	private Vibrator vib;
	private SharedPreferences prefs;


	// Shake sensor - From:
	// http://stackoverflow.com/questions/2317428/android-i-want-to-shake-it
	private SensorManager mSensorManager;
	private float mAccel; // acceleration apart from gravity
	private float mAccelCurrent; // current acceleration including gravity
	private float mAccelLast; // last acceleration including gravity
	private Calendar lastShake;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().requestFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		setProgressBarIndeterminateVisibility(false);
		setContentView(R.layout.multiple_choice_question_layout);

		ctx=this;

		finishedLoading = false;

		new SendRequestForNobelPrizeQuestions().execute();

		vib = (Vibrator) getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);
		prefs = PreferenceManager.getDefaultSharedPreferences(this);
		prefs.registerOnSharedPreferenceChangeListener(this);

		// Activating the Shake Sensor
		mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
		mSensorManager.registerListener(mSensorListener, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
		mAccel = 0.00f;
		mAccelCurrent = SensorManager.GRAVITY_EARTH;
		mAccelLast = SensorManager.GRAVITY_EARTH;
		lastShake = Calendar.getInstance();

		//intilaiser les tbleaux pour indices, recupere ces valeurs d'une interface qu'on fera plus tard
		//tout a false par defaut
		cluesGiven = new boolean [5];	
		Arrays.fill (cluesGiven, false);

		buttonStateTab = new buttonState [AMOUNT_OF_QUESTIONS][AMOUNT_OF_ANSWERS];
		for(int i = 0 ; i < AMOUNT_OF_QUESTIONS ; i++)
			for(int j = 0 ; j < AMOUNT_OF_ANSWERS ; j++)
				buttonStateTab[i][j]= buttonState.CLICKABLE;
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

		//pour la requete des laureats = on peut affiner pour faire des quizzs thematiques
		private String name;
		private int year;
		private String category;
		private String gender;
		SparseArray<Laureate> arrayOfLaureates;

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
			// super.onPostExecute(result);
			arrayOfLaureates = new SparseArray<Laureate>();
			arrayOfLaureates = api.getFinalArray();
			int key = 0;
			Random r = new Random();

			do{
				ArrayList<Laureate> laureates = new ArrayList<Laureate>();
				do{
					key = r.nextInt(arrayOfLaureates.size());
					Laureate l = arrayOfLaureates.get(key);	

					if (!laureates.contains(l) 
							&& l != null 
							&& l.getFirstname() != null && !l.getFirstname().equals("") 
							&& l.getSurname() != null && !l.getSurname().equals("") 
							&& l.getPrizes() != null && l.getPrizes().size() != 0
							)
					{
						laureates.add(l);
						Log.d(TAG, "added laureate #" + l.getId() + " : " + l.toString());
					}	

				}
				while(laureates.size() < AMOUNT_OF_ANSWERS);

				laureatesList.add(laureates);

			}
			while(laureatesList.size()<AMOUNT_OF_QUESTIONS);

			questionsGenerator = new MultipleChoiceGameAPI(laureatesList);	

			finishedLoading=true;

			questionsGenerator.shuffleQuestions();
			questions = questionsGenerator.getQuestions();


			viewPager=(ViewPager)findViewById(R.id.multiple_choice_question_pager);
			monAdapter = new MonPagerAdapter();
			viewPager.setAdapter(monAdapter);
			viewPager.setOnPageChangeListener((OnPageChangeListener) ctx);

			setProgressBarIndeterminateVisibility(false);
		}

	}

	class MonPagerAdapter extends PagerAdapter implements OnClickListener{
		private TextView currentQuestionNumberTextView;
		private ImageView responseImage;


		LayoutInflater inflater;
		View layout;
		View currentLayout;

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
			return AMOUNT_OF_QUESTIONS;
		}

		@Override
		public boolean isViewFromObject(View v, Object ob) { return v==(View)ob; }

		@Override
		public Object instantiateItem(View container, int position) {
			Log.d(TAG,"instantiate item"+ position);

			layout=(View)inflater.inflate(R.layout.multiple_choice_question_layout_page, null);


			printToastClue();		

			currentQuestion = questions.get(position);
			TextView question = (TextView) layout.findViewById(R.id.TextViewMultipleChoice_Question);
			TextView questionNumber = (TextView)layout.findViewById(R.id.TextViewMultipleChoice_QNumber);
			question.setText(currentQuestion.getQuestionString());
			questionNumber.setText("Question #"+(position+1)+" of "+questions.size());

			ArrayList<String> printedAnswers = currentQuestion.getPrintedAnswers();
			Button b1 = (Button)layout.findViewById(R.id.ButtonMultipleChoiceGame_button1);
			Button b2 = (Button)layout.findViewById(R.id.ButtonMultipleChoiceGame_button2);
			Button b3 = (Button)layout.findViewById(R.id.ButtonMultipleChoiceGame_button3);
			Button b4 = (Button)layout.findViewById(R.id.ButtonMultipleChoiceGame_button4);
			b1.setText(printedAnswers.get(0));
			b2.setText(printedAnswers.get(1));
			b3.setText(printedAnswers.get(2));
			b4.setText(printedAnswers.get(3));

			question.setText(questions.get(position).getQuestionString());
			questionNumber.setText("Question #"+(position+1)+" of "+questions.size());
			currentQuestionNumberTextView= (TextView) layout.findViewById(R.id.TextViewMultipleChoice_QNumber);			
			responseImage = (ImageView)layout.findViewById(R.id.MultipleChoiceGameImageFeedbackQuestion);


			if(currentQuestion.isAnswered){
				if(currentQuestion.isAnsweredCorrectly){
					responseImage.setImageResource(R.drawable.truequestion);
					currentQuestionNumberTextView.setTextColor(Color.GREEN);
				}
				else{
					responseImage.setImageResource(R.drawable.falsequestion);
					currentQuestionNumberTextView.setTextColor(Color.RED);
				}
			}
			else{
				b1.setOnClickListener(this);
				b2.setOnClickListener(this);
				b3.setOnClickListener(this);
				b4.setOnClickListener(this);
			}		


			printButtons(position, layout);
			((ViewPager)container).addView(layout,0);

			return layout;
		}

		private void printToastClue() {
			//on affiche ça que sur la toute première page...
			if(first){
				first = false; 
				Player player = new Player(getApplicationContext(), prefs.getString("username", ""));
				// si c'Es la premiere fois que le joueur faie ce jeu on affiche un toast
				if (player.getTotalQCM()==0)
					Toast.makeText(getApplicationContext(), "Clue given, if thy phone thou shake !", Toast.LENGTH_LONG).show();

				// sinon s'il a plus de 75% de taux d'erreur on l'affiche aussi = 1/reponse sur 4, => on en enleve une 66% d'echec
				else if((double)(player.getScoreQCM()/player.getTotalQCM()) < 0.24){
					Toast.makeText(getApplicationContext(), "Clue given, if thy phone thou shake !", Toast.LENGTH_LONG).show();
				}
			}				
		}

		@Override
		public void setPrimaryItem(ViewGroup container, int position,
				Object object) {
			View currentView = (View)object;
			currentLayout = currentView;
			currentQuestionNumberTextView= (TextView) currentView.findViewById(R.id.TextViewMultipleChoice_QNumber);
			responseImage = (ImageView)currentView.findViewById(R.id.MultipleChoiceGameImageFeedbackQuestion);
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
				case R.id.ButtonMultipleChoiceGame_button1 :
					handleClick(1);
					break;
				case R.id.ButtonMultipleChoiceGame_button2:
					handleClick(2);
					break;
				case R.id.ButtonMultipleChoiceGame_button3:
					handleClick(3);
					break;
				case R.id.ButtonMultipleChoiceGame_button4:
					handleClick(4);
					break;				
				}
			}
		}
		public void printButtons(int indQuestion, View layout) {
			Button b=null;  
			for(int i = 0 ; i < AMOUNT_OF_ANSWERS  ; i++)
			{
				switch (i) {
				case 0:
					b = (Button)layout.findViewById(R.id.ButtonMultipleChoiceGame_button1);
					break;
				case 1:
					b = (Button)layout.findViewById(R.id.ButtonMultipleChoiceGame_button2);		
					break;
				case 2:
					b = (Button)layout.findViewById(R.id.ButtonMultipleChoiceGame_button3);
					break;
				case 3:
					b = (Button)layout.findViewById(R.id.ButtonMultipleChoiceGame_button4);
					break;
				default:
					break;
				}
				printOneButton(b,indQuestion, i);

				Log.d(TAG, "on a colorié un bouton (de numero) !!"+i+" et on est dans la question D'indice " + currentQuestionNumberTextView);
			}

			return;
		}

		/** temop, réfléchir pour montrer avec autre chose que couleur = contrastes, formes etc..		 */
		private void printOneButton(Button b, int indQuestion,int i){
			buttonState state = buttonStateTab[indQuestion][i];
			switch (state) {
			case CLICKABLE:	//on ne fait rien par défaut
				break;
			case CLICKEDFALSE:	
				//on le met en rouge et non clickable
				b.setEnabled(false);
				b.setTextColor(Color.RED);
				break;
			case CLICKEDTRUE:		
				b.setEnabled(false);
				b.setTextColor(Color.GREEN);
				break;
			case TRUE:
				b.setEnabled(false);
				b.setTextColor(Color.CYAN);
				break;
			case DISABLED:		
				b.setEnabled(false);
				break;

			default:
				break;
			}
			return;
		}

		private void computeClue() {
			if(currentQuestion.isAnswered){
				Toast.makeText(getApplicationContext(), "Stop shaking this thing ! It's over !", Toast.LENGTH_SHORT).show();
				return;
			}
			if (cluesGiven[currentQuestionNumber]==false){
				MultipleChoiceQuestion printedQuestion = questions.get(currentQuestionNumber);
				//on donne l'indice en mettant un bouton en gcris
				cluesGiven[currentQuestionNumber]=true;
				int randInt =0;
				Random r = new Random();

				do{
					randInt = r.nextInt(printedQuestion.getPrintedAnswers().size());
				}
				while(printedQuestion.getRightAnswers().contains(printedQuestion.getPrintedAnswers().get(randInt)));
				//on ne selectionne pas les bonnes réponses quand on elimine une question...

				buttonStateTab[currentQuestionNumber][randInt] = buttonState.DISABLED;
				Log.v(TAG,"indice de question :"+(currentQuestion.getQuestionNumber()-1));
				//
				monAdapter.printButtons(currentQuestionNumber, currentLayout);

				Toast.makeText(getApplicationContext(), "You used a clue !", Toast.LENGTH_SHORT).show();
			}
			else{
				Toast.makeText(getApplicationContext(), "Clue already used !", Toast.LENGTH_SHORT).show();
			}
		}


		public void handleClick(int answer)
		{
			currentQuestionNumber = viewPager.getCurrentItem();			
			currentQuestion = questions.get(currentQuestionNumber);

			if(!currentQuestion.isAnswered){
				currentQuestion.setAnswered(true);


				//on grise tous les boutons, on repasse après en détail pour plus spécifique
				for(int i = 0 ; i < AMOUNT_OF_ANSWERS ; i++)
					buttonStateTab[currentQuestionNumber][i] = buttonState.DISABLED;

				//on affiche toutes les bonnes réponses
				for(Integer i : currentQuestion.getIndexRightAnswersInPrinted()){
					buttonStateTab[currentQuestionNumber][i] = buttonState.TRUE;
				}

				if(currentQuestion.getRightAnswers().contains(currentQuestion.getPrintedAnswers().get(answer-1))){
					Log.d(TAG, "Answered correctly");
					currentQuestion.setAnsweredCorrectly(true);
					score++;
					totalConsecutiveCorrect++;
					buttonStateTab[currentQuestionNumber][answer-1] = buttonState.CLICKEDTRUE;

					responseImage.setImageResource(R.drawable.truequestion);
					currentQuestionNumberTextView.setTextColor(Color.GREEN);
				}
				else{
					Log.d(TAG, "Answered wrongly");
					currentQuestion.setAnsweredCorrectly(false);
					if(prefs.getBoolean("vibrate", true))
						vib.vibrate(500);
					responseImage.setImageResource(R.drawable.falsequestion);
					currentQuestionNumberTextView.setTextColor(Color.RED);
					totalConsecutiveCorrect=0;

					//on met le faux en rouge
					buttonStateTab[currentQuestionNumber][answer-1] = buttonState.CLICKEDFALSE;
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
					Player player = new Player(getApplicationContext(),prefs.getString("username", "Player1"));
					Log.d(TAG, "player score was : "+player.toString());
					player.addScoreQCM(score, questions.size());
					Log.d(TAG, "player score is now : "+player.toString());

					updateTrophies(player);					

					Handler handlerNewPage = new Handler();
					handlerNewPage.postDelayed(new Runnable()
					{
						@Override
						public void run()
						{
							finish();
						}

					}, 2000);
				}


				printButtons(currentQuestionNumber, currentLayout);

			}

		}
		private void updateTrophies(Player player) {
			//si on répond juste à 3 questions d'affilée
			if(totalConsecutiveCorrect>=3)
				player.activateTrophy3Consecutive();

			//on utilise au moins un tip
			for(int i = 0 ; i < AMOUNT_OF_QUESTIONS;i++)
				if(cluesGiven[i]==true){
					player.activateTrophyUseATips();
					break;
				}

			//on utilise pas de tips pour une question a laquelle on a répondu vrai
			boolean exit = false;
			for(int i = 0 ; i < AMOUNT_OF_QUESTIONS ;i++){
				for(int j = 0 ; j < AMOUNT_OF_ANSWERS ; j++){
					if(cluesGiven[i]==false && buttonStateTab[i][j]==buttonState.CLICKEDTRUE)
					{
						player.activateTrophyNoTips();
						exit = true;
						break;
					}
				}
				if (exit)
					break;
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
		Log.d(TAG,"PAGE SELECTED : de num "+ position);
		currentQuestionNumber=position;
		return;
	}

	//	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
			String key) {
		// TODO Auto-generated method stub

	}

	private final SensorEventListener mSensorListener = new SensorEventListener()
	{
		@Override
		public void onSensorChanged(SensorEvent event)
		{
			float x = event.values[0];
			float y = event.values[1];
			float z = event.values[2];
			mAccelLast = mAccelCurrent;
			mAccelCurrent = (float) Math.sqrt((double) (x * x + y * y + z * z));
			float delta = mAccelCurrent - mAccelLast;
			mAccel = mAccel * 0.9f + delta; // perform low-cut filter

			if (mAccel > 20.0f)
			{
				Calendar now = Calendar.getInstance();
				now.setTime(new Date());

				long diff = now.getTimeInMillis() - lastShake.getTimeInMillis();
				if (diff >= 750)
				{
					Log.d("MainActivity", "Shake event { mAccel: " + mAccel + "}");
					monAdapter.computeClue();

					// Setting up the last shake time
					lastShake.setTime(new Date());
				}
			}
		}

		@Override
		public void onAccuracyChanged(Sensor sensor, int accuracy)
		{

		}
	};
}