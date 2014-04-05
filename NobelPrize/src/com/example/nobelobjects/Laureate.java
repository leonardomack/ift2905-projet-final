package com.example.nobelobjects;

import com.example.tools.DownloadHtmlPageTask;

public class Laureate
{
	private int id;
	private String firstname;
	private String surname;
	private String motivation;
	private int share;

	public Laureate(int id, String firstname, String surname)
	{
		this.id = id;
		this.firstname = firstname;
		this.surname = surname;
	}

	public int getId()
	{
		return id;
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

	public String getMotivation()
	{
		return motivation;
	}

	public void setMotivation(String motivation)
	{
		this.motivation = motivation;
	}

	public int getShare()
	{
		return share;
	}

	public void setShare(int share)
	{
		this.share = share;
	}

	public String toString()
	{
		return "id:" + id + " name: " + firstname + " " + surname;
	}

	public String getImageUrl(Prize prize)
	{
		// Build the url from default pattern
		String htmlToDownload = "http://www.nobelprize.org/nobel_prizes/" + prize.getCategory() + "/laureates/" + prize.getYear() + "/";// "http://www.nobelprize.org/nobel_prizes/physics/laureates/2012/"
		String winnerImageUrl = "";
		try
		{
			String htmlResult = new DownloadHtmlPageTask().execute(htmlToDownload).get();

			if (htmlResult.equals("") == false)
			{
				int divPrizeWrapperStart = htmlResult.indexOf("<div class=\"prize_wrapper\">");
				int divPrizeWrapperEnd = 0;
				String divPrizeWrapper = "";
				String imgSrc = "";

				divPrizeWrapper = htmlResult.substring(divPrizeWrapperStart);
				divPrizeWrapperEnd = divPrizeWrapper.indexOf("<div style=\"clear:both;\"></div>");
				divPrizeWrapper = divPrizeWrapper.substring(0, divPrizeWrapperEnd);

				int imgStart = divPrizeWrapper.indexOf("<img src=\"") + 10;
				int imgEnd = 0;

				imgSrc = divPrizeWrapper.substring(imgStart);
				imgEnd = imgSrc.indexOf("\" alt=\"");
				imgSrc = imgSrc.substring(0, imgEnd);

				imgSrc = imgSrc.replace(".jpg", "_postcard.jpg");

				winnerImageUrl = imgSrc;
			}
		}
		catch (Exception e)
		{
		}

		return winnerImageUrl;
	}
}
