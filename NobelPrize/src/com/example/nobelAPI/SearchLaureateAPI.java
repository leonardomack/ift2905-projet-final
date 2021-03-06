package com.example.nobelAPI;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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

public class SearchLaureateAPI
{
	public SparseArray<Laureate> laureatesL;
	public SparseArray<Laureate> laureatesP;
	SparseArray<Laureate> finalArray;
	public int numberOfRequestSent;
	final String TAG = "SearchLaureateAPI";
	public String prizeURL;
	public String laureateURL;

	String erreur;

	public SearchLaureateAPI(String nameSearched, int year, String gender, String category)
	{
		laureatesL = new SparseArray<Laureate>();
		laureatesP = new SparseArray<Laureate>();
		finalArray = new SparseArray<Laureate>();
		erreur = null;
		prizeURL = "http://api.nobelprize.org/v1/prize.json";
		laureateURL = "http://api.nobelprize.org/v1/laureate.json";
		String searchURL = "";
		/*
		 * L'API ne supporte pas directement la recherche de laur�at avec la
		 * cat�gorie ou l'ann�e. Si c'est param�tres sont pr�sent, il faut faire
		 * 2 requ�tes � l'API.
		 */
		boolean hasYear = year != -1;
		boolean hasName = nameSearched.length() > 0;
		boolean hasGender = !gender.equals("all");
		boolean hasCategory = !category.equals("all");
		boolean needToMakePrizeRequest = (hasYear || hasCategory);

		Log.d(TAG, "Starting request with param" + nameSearched + " " + year + " " + category + " " + gender);

		if (!hasYear && !hasName && !hasGender && !hasCategory)
		{
			searchURL = laureateURL;
			getLaureatesFromLaureatesList(searchURL);
		}

		if (needToMakePrizeRequest)
		{
			numberOfRequestSent++;
			searchURL = prizeURL + "?";
			if (hasYear)
				searchURL += "year=" + year + "&";
			if (hasCategory)
				searchURL += "category=" + category + "&";

			getLaureatesFromPrizeList(searchURL);

		}

		if (hasName || hasGender)
		{
			numberOfRequestSent++;
			searchURL = laureateURL + "?";
			if (hasName)
				searchURL += "name=" + nameSearched + "&";
			if (hasGender)
			{
				if (gender.equals("organization"))
					gender = "org";
				searchURL += "gender=" + gender + "&";
			}

			getLaureatesFromLaureatesList(searchURL);
		}

		if (numberOfRequestSent > 1)
		{
			Log.w(TAG, "Had 2 request to web page ");
			int key1 = 0;
			SparseArray<Laureate> bigArray = new SparseArray<Laureate>();
			SparseArray<Laureate> smallArray = new SparseArray<Laureate>();
			if (laureatesL.size() >= laureatesP.size())
			{
				bigArray = laureatesL;
				smallArray = laureatesP;
			}
			else
			{
				bigArray = laureatesP;
				smallArray = laureatesL;
			}
			Log.d(TAG, "bigArray : " + bigArray.size());
			Log.d(TAG, "smallArray : " + smallArray.size());
			for (int i = 0; i < smallArray.size(); i++)
			{
				key1 = smallArray.keyAt(i);
				Laureate l1 = smallArray.get(key1);
				if (bigArray.indexOfKey(key1) >= 0)
					finalArray.put(key1, l1);
			}
		}
		else
		{
			Log.d(TAG, "laureatesL has " + laureatesL.size());
			int key = 0;
			for (int i = 0; i < laureatesL.size(); i++)
			{
				key = laureatesL.keyAt(i);
				Laureate l = laureatesL.get(key);
				finalArray.put(l.getId(), l);
			}
			key = 0;
			Log.d(TAG, "laureatesP has " + laureatesP.size());
			for (int i = 0; i < laureatesP.size(); i++)
			{
				key = laureatesP.keyAt(i);
				Laureate l = laureatesP.get(key);
				finalArray.put(l.getId(), l);
			}
		}

		int key = 0;
		Log.d(TAG, "finalArray has " + finalArray.size());
		for (int i = 0; i < finalArray.size(); i++)
		{
			key = finalArray.keyAt(i);
			Laureate l = finalArray.get(key);
			Log.d(TAG, "Laureate #" + i + " : " + l.toString());
		}

	}

	public SearchLaureateAPI(int id)
	{
		laureatesL = new SparseArray<Laureate>();
		laureatesP = new SparseArray<Laureate>();
		finalArray = new SparseArray<Laureate>();
		erreur = null;

		getLaureatesFromLaureatesList("http://api.nobelprize.org/v1/laureate.json?id=" + id);

		int key = 0;
		for (int i = 0; i < laureatesL.size(); i++)
		{
			key = laureatesL.keyAt(i);
			Laureate l = laureatesL.get(key);
			finalArray.put(l.getId(), l);
		}
	}

	private void getLaureatesFromPrizeList(String searchURL)
	{
		try
		{
			HttpEntity page = getHttp(searchURL);
			JSONObject js = new JSONObject(EntityUtils.toString(page, HTTP.UTF_8));
			JSONArray prizes = js.getJSONArray("prizes");
			for (int i = 0; i < prizes.length(); i++)
			{
				JSONObject prize = prizes.getJSONObject(i);
				JSONArray laurArray = prize.getJSONArray("laureates");
				for (int j = 0; j < laurArray.length(); j++)
				{
					JSONObject laur = laurArray.getJSONObject(j);
					int id = -1;
					String firstname = "";
					String surname = "";
					if (laur.has("id"))
						id = laur.getInt("id");
					if (laur.has("firstname"))
						firstname = laur.getString("firstname");
					if (laur.has("surname"))
						surname = laur.getString("surname");
					Laureate l = new Laureate(id, firstname, surname);
					laureatesP.put(id, l);
				}
			}
		}
		catch (ClientProtocolException e)
		{
			erreur = "Erreur HTTP (protocole) :" + e.getMessage();
		}
		catch (IOException e)
		{
			erreur = "Erreur HTTP (IO) :" + e.getMessage();
		}
		catch (ParseException e)
		{
			erreur = "Erreur JSON (parse) :" + e.getMessage();
		}
		catch (JSONException e)
		{
			erreur = "Erreur JSON :" + e.getMessage();
		}
	}

	private void getLaureatesFromLaureatesList(String searchURL)
	{
		try
		{
			HttpEntity page2 = getHttp(searchURL);
			JSONObject js2 = new JSONObject(EntityUtils.toString(page2, HTTP.UTF_8));
			JSONArray laureatesArray = js2.getJSONArray("laureates");
			for (int i = 0; i < laureatesArray.length(); i++)
			{
				JSONObject laureate = laureatesArray.getJSONObject(i);
				int id = -1;
				String firstname = "";
				String surname = "";
				String bornCountry = "";
				String bornCity = "";
				String dateBorn = "";
				String dateDied = "";

				List<Prize> prizes = new ArrayList<Prize>();

				if (laureate.has("id"))
				{
					id = laureate.getInt("id");
				}
				if (laureate.has("firstname"))
				{
					firstname = laureate.getString("firstname");
				}
				if (laureate.has("surname"))
				{
					surname = laureate.getString("surname");
				}
				if (laureate.has("bornCountry"))
				{
					bornCountry = laureate.getString("bornCountry");
				}
				if (laureate.has("bornCity"))
				{
					bornCity = laureate.getString("bornCity");
				}
				if (laureate.has("born"))
				{
					dateBorn = laureate.getString("born");
				}
				if (laureate.has("died"))
				{
					dateDied = laureate.getString("died");
				}

				if (laureate.has("prizes"))
				{
					JSONArray prizesArray = laureate.getJSONArray("prizes");

					for (int j = 0; j < prizesArray.length(); j++)
					{
						JSONObject prize = prizesArray.getJSONObject(j);

						int year = 0;
						String category = "";
						String motivation = "";

						if (prize.has("year"))
						{
							year = prize.getInt("year");
						}
						if (prize.has("category"))
						{
							category = prize.getString("category");
						}
						if (prize.has("motivation"))
						{
							motivation = prize.getString("motivation");
						}

						Prize prizeTemp = new Prize(year, category, null);
						prizeTemp.setMotivation(motivation);

						prizes.add(prizeTemp);
					}
				}

				Laureate l = new Laureate(id, firstname, surname);
				l.setPrizes(prizes);
				l.setBornCountry(bornCountry);
				l.setBornCity(bornCity);
				l.setDateBorn(dateBorn);
				l.setDateDied(dateDied);

				laureatesL.put(id, l);
			}

		}
		catch (ClientProtocolException e)
		{
			erreur = "Erreur HTTP (protocole) :" + e.getMessage();
		}
		catch (IOException e)
		{
			erreur = "Erreur HTTP (IO) :" + e.getMessage();
		}
		catch (ParseException e)
		{
			erreur = "Erreur JSON (parse) :" + e.getMessage();
		}
		catch (JSONException e)
		{
			erreur = "Erreur JSON :" + e.getMessage();
		}
	}

	public SparseArray<Laureate> getFinalArray()
	{
		return finalArray;
	}

	/*
	 * M�thode de MeteoWebAPI
	 */
	private HttpEntity getHttp(String url) throws ClientProtocolException, IOException
	{
		HttpClient httpClient = new DefaultHttpClient();
		HttpGet http = new HttpGet(url);
		HttpResponse response = httpClient.execute(http);
		return response.getEntity();
	}
}
