package com.example.nobelobjects;

import android.util.SparseArray;

public class Prize
{
	private int year;
	private String category;
	private SparseArray<Laureate> laureates;

	/*
	 * int key = 0; for(int i = 0; i < sparseArray.size(); i++) { key =
	 * sparseArray.keyAt(i); Object obj = sparseArray.get(key); }
	 */

	public Prize(int year, String category, SparseArray<Laureate> laureates)
	{
		this.year = year;
		this.category = category;
		this.laureates = laureates;
	}

	public int getYear()
	{
		return year;
	}

	public void setYear(int year)
	{
		this.year = year;
	}

	public String getCategory()
	{
		return category;
	}

	public void setCategory(String category)
	{
		this.category = category;
	}

	public SparseArray<Laureate> getLaureates()
	{
		return laureates;
	}

	public void setLaureates(SparseArray<Laureate> laureates)
	{
		this.laureates = laureates;
	}

	public void addLaureate(int id, Laureate l)
	{
		this.laureates.put(id, l);
	}

	public void removeLaureate(int id)
	{
		this.laureates.remove(id);
	}
}
