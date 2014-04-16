package com.example.nobelAPI;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Random;
import java.util.concurrent.ExecutionException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.example.nobelobjects.Laureate;
import com.example.nobelobjects.Prize;
import com.example.nobelobjects.TrueFalseQuestion;
import com.example.nobelobjects.WhoAmIQuestion;
import com.example.tasks.DownloadLaureateTask;
/**
 * 2types de questions = "qui suis je" et "quel prix nobel ai-je gagné"
 * @author locust
 *
 */

public class WhoAmIGameAPI {
	private String prizeURL;
	private String laureateURL;
	private ArrayList<WhoAmIQuestion> questions;
	private String erreur;
	private final static int AMOUNT_OF_QUESTIONS = 5;
	private final int LAST_LAUREATE = 896; //dernier lauréat de la liste répertorié Avril 2014
	private int questionNumber;
	private final static String TAG = "WhoAmIAPI";

	public WhoAmIGameAPI(){		
		prizeURL = "http://api.nobelprize.org/v1/prize.json";
		laureateURL = "http://api.nobelprize.org/v1/laureate.json";
		questions = new ArrayList<WhoAmIQuestion>();
		erreur = null;
		questionNumber=1;

		while(questions.size()<AMOUNT_OF_QUESTIONS){
			//NULLpointerEcepion here
			WhoAmIQuestion questionElement = computeRandomQuestion(questionNumber);
			if(questionElement != null){

				Log.v(TAG,questionElement.toString());
				questions.add(computeRandomQuestion(questionNumber));
				questionNumber++;
			}
		}
	}
	/**
	 on vérifie qu'on ne crée pas de questions trop ressemblantes cf equals dans WhoAmIQuestion 
	 * @param questionNumber
	 * @return
	 */
	private WhoAmIQuestion computeRandomQuestion(int questionNumber) {

		Laureate laureate = fetchRandomLaureate();		

		ArrayList<String> randomAnswers = new ArrayList<String>();
		ArrayList<String> rightAnswers = new ArrayList<String>();
		ArrayList<String> answersToPrint = new ArrayList<String>();

		//on choisit l'un des deux types de question
		boolean type = randomFiftyPercentChance();
		if(type){
			rightAnswers=getCategoriesWon(laureate);

			//moche mais bon...
			randomAnswers=fetchRandomCategories(rightAnswers.get(0));
		}
		else{
			//NULLPOINTEREXCEPITON
			rightAnswers.add(laureate.getFirstname()+" "+laureate.getSurname());
			randomAnswers =fetchRandomNames(laureate);
		}

		answersToPrint.addAll(randomAnswers);
		//on ajoute une des reponses possibles au champ, toujours la première...
		answersToPrint.add(rightAnswers.get(0));

		answersToPrint.removeAll(Collections.singleton(null)); 
		rightAnswers.removeAll(Collections.singleton(null));  

		WhoAmIQuestion question= null;

		try {
			question = new WhoAmIQuestion(questionNumber, type, answersToPrint, rightAnswers,laureate.getImageUrl(laureate));
		} catch (Exception e) {
			// TODO Auto-generated catch block

			e.printStackTrace();
			return null;
		}



		//si on a déjà créé cette question, alors on n'en veut pas
		if(questions.contains(question)){
			return null;
		}
		return question;
	}


	private ArrayList<String> getCategoriesWon(Laureate laureate) {
		ArrayList<String> categoriesWon = new ArrayList<String>();
		for(Prize p : laureate.getPrizes())
			categoriesWon.add(p.getCategory());
		return categoriesWon;
	}

	private ArrayList<String> fetchRandomNames( Laureate laureate){
		ArrayList<String> answers = new ArrayList<String>();  
		ArrayList<Laureate> laureateList = new ArrayList<Laureate>();	

		while(laureateList.size() < 3){
			Laureate tempLaureate = fetchRandomLaureate();
			if ( tempLaureate != null && !tempLaureate.equals (laureate) && !laureateList.contains(tempLaureate)){
				laureateList.add (tempLaureate);
				answers.add(tempLaureate.getFirstname()+" "+tempLaureate.getSurname());
			}
		}
		return answers;
	}

	/**si prize == null ou (alors contient un earraylist non nulle d'objets null... tester aussi) alors retourner*/
	private Laureate fetchRandomLaureate() {
		Random r = new Random();
		//soit appeler json unique = je pense que ca fetch la totalité des laureats 
		// soit initialiser une liste locale dans constructeur et chercher le laureat dedans
		try
		{
			Laureate selectedLaureate = null;
			do{
				int id = r.nextInt(LAST_LAUREATE)+1;
				selectedLaureate = new DownloadLaureateTask().execute(id).get();
				//Prize prize = new Prize();
				/*if (selectedLaureate.getPrizes().size() == 0)
			{
				return null;
				//prize = selectedLaureate.getPrizes().get(0);
			}*/
			}while(selectedLaureate==null || selectedLaureate.getSurname()==null || selectedLaureate.getFirstname()==null || 
					selectedLaureate.getPrizes() == null || selectedLaureate.getPrizes().size() == 0  );

			return selectedLaureate;
		}catch(Exception e){Log.v(TAG,"on a fetché un laureate NULL");
		}

		return null;
	}
	public static int getAmountOfQuestion(){
		return AMOUNT_OF_QUESTIONS;
	}


	private ArrayList <String>  fetchRandomCategories(String answer){
		ArrayList <String> selectedCategories = new ArrayList<String>();

		for (int i = 0; i < 3 ; i++){
			String category = null;
			do{
				int sel = randomMinMax(0, 5);
				switch(sel){
				case 0:
					category = "economics";
					break;
				case 1:
					category = "peace";
					break;
				case 2:
					category = "literature";
					break;
				case 3:
					category = "medicine";
					break;
				case 4:
					category = "chemistry";
					break;
				case 5:
					category = "physics";
					break;
				}
			}while(selectedCategories.contains(category) || category.equals(answer));

			selectedCategories.add(category);
		}
		return selectedCategories;
	}

	/**
	 * utiliser pour un 3eme type de question
	 * @param rightYear
	 * @return
	 */
	private int fetchRandomYear(int rightYear){
		int year = -1;
		int firstYearOfNobelPrize = 1901;
		int thisYear = (Calendar.getInstance().get(Calendar.YEAR)) -1;
		do{
			year = randomMinMax(firstYearOfNobelPrize, thisYear);
		}while(year == rightYear);
		return year;
	}

	public ArrayList<WhoAmIQuestion> getQuestions() {
		return questions;
	}

	/*
	 * Méthodes aléatoires
	 */
	private int randomMinMax(int min, int max){
		Random rand = new Random();
		return rand.nextInt((max-min)+1)+min;
	}

	private boolean randomFiftyPercentChance()
	{   
		return Math.random() < 0.50;
	}
	/**
	 * certaines lsites ont des éléments null qui perturbent le bon fonctionnement de l'app
	 * @param l
	 */
	private boolean removeNull(ArrayList l){
		boolean isNull=false;
		Object o =null;
		for(int i = 0; i < l.size(); i++){
			o = l.get(i);
			if (o == null){
				isNull= true;
				l.remove(i);
			}
		}
		if (isNull)
			Log.v(TAG,"on a enlevé des élements NULL");
		return isNull;

	}

	//NULLPOINTEREXCEPTION
	public void shuffleQuestions(){

		//Log.v(TAG,"AVANT"+questions);
		for(WhoAmIQuestion q : questions){
			if (q != null)
				Collections.shuffle(q.getPrintedAnswers());
		}
		//Log.v(TAG,"APRES"+questions);


	}
}
