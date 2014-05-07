package com.example.nobelobjects;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import com.example.tasks.DownloadHtmlPageTask;
import com.example.tasks.DownloadLaureateTask;

public class Laureate
{
	private int id;
	private String firstname;
	private String surname;
	private String bornCountry;
	private String bornCity;
	private String dateBorn;
	private String dateDied;
	private List<Prize> prizes;

	private String imageUrl = null;
	
	private boolean hasNewTrophees = false;

	public Laureate()
	{
	}

	public Laureate(int id, String firstname, String surname)
	{
		this.id = id;
		this.firstname = firstname;
		this.surname = surname;

		prizes = new ArrayList<Prize>();
	}

	/**
	 * nécessaire pour avoir un bon random des laureates = pas de doublons
	 */
	@Override
	public boolean equals(Object o) {
		if(o instanceof Laureate && ((Laureate)o).getId() == this.getId()){
			return true;
		} else {
			return false;
		}
	}

	public String getDateBorn()
	{
		return dateBorn;
	}

	public void setDateBorn(String dateBorn)
	{
		this.dateBorn = dateBorn;
	}

	public String getDateDied()
	{
		return dateDied;
	}

	public void setDateDied(String dateDied)
	{
		this.dateDied = dateDied;
	}

	public String getBornCountry()
	{
		return bornCountry;
	}

	public void setBornCountry(String bornCountry)
	{
		this.bornCountry = bornCountry;
	}
	
	public String getBornCity()
	{
		return bornCity;
	}

	public void setBornCity(String bornCity)
	{
		this.bornCity = bornCity;
	}

	public int getId()
	{
		return id;
	}

	public List<Prize> getPrizes()
	{
		return prizes;
	} 

	public void setPrizes(List<Prize> prizes)
	{
		this.prizes = prizes;
	}

	public void setId(int id)
	{
		this.id = id;
	}

	public String getFirstname()
	{
		return firstname;
	}

	public void setFirstname(String firstname)
	{
		this.firstname = firstname;
	}

	public String getSurname()
	{
		return surname;
	}

	public void setSurname(String surname)
	{
		this.surname = surname;
	}

	public String toString()
	{
		return "id:" + id + " name: " + firstname + " " + surname;
	}

	/*
	 * The Prize parameter must indicate the year and category
	 */
	public String getImageUrl(Laureate laureate) throws InterruptedException, ExecutionException
	{
		//pour éviter les calculs et appels a l'API redondants
		if(imageUrl != null && !imageUrl.equals(""))
			return imageUrl;

		// Find the laureate and his prizes
		Laureate laureateSearch = new DownloadLaureateTask().execute(laureate.getId()).get();
		List<Prize> prizes = laureateSearch.getPrizes();

		// Build the url from default pattern
		String htmlToDownload = "http://www.nobelprize.org/nobel_prizes/" + prizes.get(0).getCategory() + "/laureates/" + prizes.get(0).getYear() + "/";// "http://www.nobelprize.org/nobel_prizes/physics/laureates/2012/"
		String winnerImageUrl = "";
		try
		{
			String htmlResult = new DownloadHtmlPageTask().execute(htmlToDownload).get().toLowerCase();

			if (htmlResult.equals("") == false)
			{
				List<String> imageHtmls = new ArrayList<String>();

				int divPrizeWrapperEnd = 0;
				String divPrizeWrapper = "";

				// Getting just the html that have image urls
				// Start html part = <div class=\"prize_wrapper\">
				divPrizeWrapper = htmlResult.substring(htmlResult.indexOf("<div class=\"prize_wrapper\">"));

				// End html part = <div style=\"clear:both;\"></div>
				divPrizeWrapperEnd = divPrizeWrapper.indexOf("<div style=\"clear:both;\"></div>");

				// Just the html that contais all image urls
				divPrizeWrapper = divPrizeWrapper.substring(0, divPrizeWrapperEnd);

				// Getting all the urls in divPrizeWrapper
				int imgStart = divPrizeWrapper.indexOf("<img src=\"");
				int imgEnd = 0;

				while (imgStart != -1)
				{
					String imageUrlTemp = "";

					imgStart += 10;
					imageUrlTemp = divPrizeWrapper.substring(imgStart);
					imgEnd = imageUrlTemp.indexOf("border=\"0\"></a>");
					imageUrlTemp = imageUrlTemp.substring(0, imgEnd);
					imageHtmls.add(imageUrlTemp);

					divPrizeWrapper = divPrizeWrapper.substring(imgStart + imgEnd);

					imgStart = divPrizeWrapper.indexOf("<img src=\"");
				}

				// Need to check if the URL contains laureate's name
				String finalImageUrl = "";
				for (String url : imageHtmls)
				{
					if (url.contains(laureateSearch.getFirstname().toLowerCase()))
					{
						// We found the good url
						finalImageUrl = url;

						// Removing alt attribute
						finalImageUrl = finalImageUrl.substring(0, finalImageUrl.indexOf("\" alt=\""));

						// Replacing with a bigger image
						finalImageUrl = finalImageUrl.replace(".jpg", "_postcard.jpg");
					}
				}

				winnerImageUrl = finalImageUrl;
			}
		}
		catch (Exception e)
		{
		}
		
		this.imageUrl = winnerImageUrl;
		return winnerImageUrl;
	}
}
