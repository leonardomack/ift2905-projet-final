package com.example.nobelAPI;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Random;

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

import com.example.nobelobjects.Laureate;
import com.example.nobelobjects.Prize;
import com.example.nobelobjects.TrueFalseQuestion;
import com.example.nobelobjects.WhoAmIQuestion;
import com.example.tasks.DownloadLaureateTask;
/**
 * NON TESTÉ
 * @author locust
 *
 */
public class WhoAmIGameAPI {
	private String prizeURL;
	private String laureateURL;
	private ArrayList<WhoAmIQuestion> questions;
	private String erreur;
	private final static int AMOUNT_OF_QUESTIONS = 5;
	private final int LAST_LAUREATE = 896; //dernier laur�at de la liste r�pertori� Avril 2014
	private int questionNumber;
	private final String TAG = "WhoAmIAPI";

	public WhoAmIGameAPI(){
		prizeURL = "http://api.nobelprize.org/v1/prize.json";
		laureateURL = "http://api.nobelprize.org/v1/laureate.json";
		questions = new ArrayList<WhoAmIQuestion>();
		erreur = null;
		//champ utile ?
		questionNumber=1;

		while(questions.size()<AMOUNT_OF_QUESTIONS){
			questions.add(computeRandomQuestion(questionNumber));
			questionNumber++;
		}
	}
/**
 * en théorie on peut générer des doubles de questions... mauvais, ou alors mettre la liste de qquestions
 *  en attribut et comparer apres chque creation de quqestion avec la liste de questions deja générées =
 * attention à bien redéfinir le equals dans ce type de question, 2 questions sont égales si meme type 
 * et si meme rightAnswers... (égalité ArrayList rend true si dans le même ORDRE !! ?? ok I guess)
 * @param questionMumber
 * @return
 */
	private WhoAmIQuestion computeRandomQuestion(int questionMumber) {
		Laureate laureate = fetchRandomLaureate();		
		//3 fake answer

		ArrayList<String> answers = new ArrayList<String>(); //vérifier qu'elles sont différenetes 
		//de la right answer, et différenetes des prizes des autres
		ArrayList<String> rightAnswers ;//on ajoute 1 right answer
		//dans la printed answer a la fin, lors de la creation de la question

		//on choisit l'un des deux types de question
		boolean type = randomFiftyPercentChance();
		if(type){
			rightAnswers=getCategoriesWon(laureate);
			answers=fetchRandomCategories();
		}
		else{
			rightAnswers = new ArrayList<String>();
			rightAnswers.add(laureate.getFirstname()+" "+laureate.getSurname());
			answers =fetchRandomNames(laureate);
		}

		ArrayList<String> answersToPrint = new ArrayList<String>();
		answersToPrint.addAll(answers);
		answersToPrint.add(rightAnswers.get(randomMinMax(0, rightAnswers.size()-1)));

		WhoAmIQuestion question = new WhoAmIQuestion(questionNumber, type, answersToPrint, rightAnswers);	

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
			if ( !tempLaureate.equals (laureate) && !laureateList.contains(tempLaureate)){
				laureateList.add (tempLaureate);
				answers.add(tempLaureate.getFirstname()+" "+tempLaureate.getSurname());
			}
		}
		return answers;
	}

	private Laureate fetchRandomLaureate() {
		int id = (new Random()).nextInt(LAST_LAUREATE)+1;
		//soit appeler json unique = je pense que ca fetch la totalité des laureats 
		// soit initialiser une liste locale dans constructeur et chercher le laureat dedans
		try
		{
			Laureate selectedLaureate = new DownloadLaureateTask().execute(id).get();
			Prize prize = new Prize();
			/*if (selectedLaureate.getPrizes().size() > 0)
			{
				prize = selectedLaureate.getPrizes().get(0);
			}*/
			return selectedLaureate;
		}catch(Exception e){
		}

		return null;
	}
	public static int getAmountOfQuestion(){
		return AMOUNT_OF_QUESTIONS;
	}


	private ArrayList <String>  fetchRandomCategories(){
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
			}while(selectedCategories.contains(category));

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
	 * M�thode de MeteoWebAPI
	 * 
	 */
	private HttpEntity getHttp(String url) throws ClientProtocolException, IOException {
		HttpClient httpClient = new DefaultHttpClient();
		HttpGet http = new HttpGet(url);
		HttpResponse response = httpClient.execute(http);
		return response.getEntity();    		
	}

	/*
	 * M�thodes al�atoires
	 */
	private int randomMinMax(int min, int max){
		Random rand = new Random();
		return rand.nextInt((max-min)+1)+min;
	}

	private boolean randomFiftyPercentChance()
	{   
		return Math.random() < 0.50;
	}
}
