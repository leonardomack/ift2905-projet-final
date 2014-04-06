package com.example.nobelprize;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedList;
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

import com.example.nobelobjects.TrueFalseQuestion;

public class TrueFalseGameAPI {

	private String prizeURL;
	private String laureateURL;
	private ArrayList<TrueFalseQuestion> questions;
	private String erreur;
	private final int AMOUNT_OF_QUESTIONS = 5;
	private final int LAST_LAUREATE = 896;
	private int questionNumber;
	private final String TAG = "TrueFalseGameAPI";
	
	public TrueFalseGameAPI(){
		prizeURL = "http://api.nobelprize.org/v1/prize.json";
		laureateURL = "http://api.nobelprize.org/v1/laureate.json";
		questions = new ArrayList<TrueFalseQuestion>();
		erreur = null;
		questionNumber=1;
		
		while(questions.size()<AMOUNT_OF_QUESTIONS){
			getInfoForQuestionType1();
		}
	}
	
	private void getInfoForQuestionType1(){
		String searchURL = laureateURL+"?id=";
		int id = randomMinMax(1, LAST_LAUREATE);
		searchURL+=id;
		try{
			HttpEntity page = getHttp(searchURL);
			JSONObject js = new JSONObject(EntityUtils.toString(page, HTTP.UTF_8));
			JSONArray laureateList = js.getJSONArray("laureates");
			if(laureateList.length()>=1){
				JSONObject laureate = laureateList.getJSONObject(0);
				JSONArray prizesList = laureate.getJSONArray("prizes");
				JSONObject prize = prizesList.getJSONObject(0);
				String name = laureate.getString("firstname") + " " + laureate.getString("surname");
				int year = prize.getInt("year");
				String category = prize.getString("category");
				boolean answer = true;
				int chooseAField = 0;
				chooseAField=randomMinMax(0,1);
				switch(chooseAField){
				case 0:
					if(randomFiftyPercentChance()){
						category = fetchRandomCategory();
						answer=false;
					}
					break;
				case 1:
					if(randomFiftyPercentChance()){
						year = fetchRandomYear();
						answer=false;
					}
					break;
				default:
					break;
				}
				TrueFalseQuestion q = new TrueFalseQuestion().newTrueFalseQuestionType1(questionNumber, 
						name,year,category, answer);
				questions.add(q);
			}
			
		} catch (ClientProtocolException e) {
			erreur = "Erreur HTTP (protocole) :"+e.getMessage();
		} catch (IOException e) {
			erreur = "Erreur HTTP (IO) :"+e.getMessage();
		} catch (ParseException e) {
			erreur = "Erreur JSON (parse) :"+e.getMessage();
		} catch (JSONException e) {
			erreur = "Erreur JSON :"+e.getMessage();
		}
	}
	
	public ArrayList<TrueFalseQuestion> getQuestions() {
		return questions;
	}

	/*
	 * Méthode de MeteoWebAPI
	 * 
	 */
	private HttpEntity getHttp(String url) throws ClientProtocolException, IOException {
		HttpClient httpClient = new DefaultHttpClient();
		HttpGet http = new HttpGet(url);
		HttpResponse response = httpClient.execute(http);
		return response.getEntity();    		
	}
	
	private int randomMinMax(int min, int max){
		Random rand = new Random();
		return rand.nextInt((max-min)+1)+min;
	}
	
	private boolean randomFiftyPercentChance()
	{   
	   return Math.random() < 0.50;
	}
	
	private String fetchRandomCategory(){
		int i = randomMinMax(0, 5);
		String cat = null;
		switch(i){
		case 0:
			cat = "Economics";
			break;
		case 1:
			cat = "Peace";
			break;
		case 2:
			cat = "Literature";
			break;
		case 3:
			cat = "Medicine";
			break;
		case 4:
			cat = "Chemistry";
			break;
		case 5:
			cat = "Physics";
			break;
		}
		return cat;
	}
	
	private int fetchRandomYear(){
		int year = -1;
		int firstYearOfNobelPrize = 1901;
		int thisYear = (Calendar.getInstance().get(Calendar.YEAR)) -1;
		year = randomMinMax(firstYearOfNobelPrize, thisYear);
		return year;
	}
}
