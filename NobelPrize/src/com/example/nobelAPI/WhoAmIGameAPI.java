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
import android.util.SparseArray;

import com.example.nobelobjects.Laureate;
import com.example.nobelobjects.Prize;
import com.example.nobelobjects.TrueFalseQuestion;
import com.example.nobelobjects.WhoAmIQuestion;
import com.example.tasks.DownloadLaureateTask;
/**
 * 2types de questions = "qui suis je" et "quel prix nobel ai-je gagné"
 * rq : on utilisera pas forcémenet tout les candidats dans l'arraylist
 * @author locust
 *
 */

public class WhoAmIGameAPI implements GlobalConstants{
	private String prizeURL;
	private String laureateURL;
	private ArrayList<WhoAmIQuestion> questions;
	private ArrayList<ArrayList<Laureate>> laureatesList;
	private String erreur;
	private final static String TAG = "WhoAmIAPI";

	/**
	 * créé les questions à partir de la list de laureats donnée
	 * @param laureates
	 */
	public WhoAmIGameAPI(ArrayList<ArrayList<Laureate>> laureatesList){
		this.laureatesList = laureatesList;
		questions = new ArrayList<WhoAmIQuestion>();
		erreur = null;

		for(int questionNumber = 1 ; questionNumber <= AMOUNT_OF_QUESTIONS ; questionNumber++){
			WhoAmIQuestion questionElement = computeRandomQuestion(questionNumber);
			questions.add(questionElement);

			Log.v(TAG,questionElement.toString());
		}
	}

	/**
	 * @param questionNumber
	 * @return
	 */
	private WhoAmIQuestion computeRandomQuestion(int questionNumber) {
		ArrayList<Laureate> laureates = laureatesList.get(questionNumber-1); 

		//bonne reponse
		Laureate laureate = laureates.get(0);		

		ArrayList<String> randomAnswers = new ArrayList<String>();
		ArrayList<String> rightAnswers = new ArrayList<String>();
		ArrayList<String> answersToPrint = new ArrayList<String>();

		//on choisit l'un des deux types de question
		int type = randomDifferentTypes(2);

		switch(type){
		case 1 :			
			rightAnswers=getCategoriesWon(laureate);
			randomAnswers=fetchRandomCategories(rightAnswers.get(0));
			break;
		case 2 :
			rightAnswers.add(laureate.getFirstname()+" "+laureate.getSurname());
			randomAnswers =fetchRandomNames(laureates);
			break;
		}

		answersToPrint.addAll(randomAnswers);
		//on ajoute une des reponses possibles au champ, toujours la première...
		answersToPrint.add(rightAnswers.get(0));

		WhoAmIQuestion question= null;
		try {
			question = new WhoAmIQuestion(questionNumber, type, answersToPrint, rightAnswers,laureate.getImageUrl(laureate));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			//on a un laureat avec une image nulle... pas bon du tout... on ne paut pas gérer ce cas à ce niveau...
			//on peut mettre une url basic vers une photo anonyme qui est signe dde bug... pour renseigner l'utilisateur
			Log.v(TAG,"Laureate avec PHOTO NULL !!!! Nothing we can do for now");
			e.printStackTrace();
			question = new WhoAmIQuestion(questionNumber, type, answersToPrint, rightAnswers,"pas d image");
		}

		return question;
	}


	private int randomDifferentTypes(int i) {
		return randomMinMax(1,i);
	}

	private ArrayList<String> getCategoriesWon(Laureate laureate) {
		ArrayList<String> categoriesWon = new ArrayList<String>();
		for(Prize p : laureate.getPrizes())
			categoriesWon.add(p.getCategory());
		return categoriesWon;
	}

	private ArrayList<String> fetchRandomNames( ArrayList<Laureate> randomLaureates){
		ArrayList<String> answers = new ArrayList<String>();  

		//remplacer 4 par "number - od other choices"
		for(int i = 1; i < 4 ; i++){
			Laureate tempLaureate = randomLaureates.get(i);
			answers.add(tempLaureate.getFirstname()+" "+tempLaureate.getSurname());
		}
		return answers;
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


	public void shuffleQuestions(){
		for(WhoAmIQuestion q : questions){
			if (q != null)
				Collections.shuffle(q.getPrintedAnswers());
		}
	}

}
