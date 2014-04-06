package com.example.nobelprize;

import java.io.IOException;
import java.util.ArrayList;
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
	private final int LAST_LAUREATE = 896;
	private int questionNumber;
	
	public TrueFalseGameAPI(){
		prizeURL = "http://api.nobelprize.org/v1/prize.json";
		laureateURL = "http://api.nobelprize.org/v1/laureate.json";
		questions = new ArrayList<TrueFalseQuestion>();
		erreur = null;
		questionNumber=1;
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
				switch(chooseAField){
				case 0:
					if(randomFiftyPercentChance()){
						
					}
					break;
				default:
					break;
				}
				TrueFalseQuestion q = new TrueFalseQuestion().newTrueFalseQuestionType1(questionNumber, 
						name,year,category, answer);
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
}
