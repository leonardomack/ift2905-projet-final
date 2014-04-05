package com.example.nobelobjects;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import android.util.SparseArray;

public class Prize
{
	private int year;
	private String category;
	private String motivation;
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

		motivation = "";
	}

	public String getMotivation()
	{
		return motivation;
	}

	public void setMotivation(String motivation)
	{
		this.motivation = motivation;
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

	public enum PrizeCategories
	{
		physics, chemistry, medicine, literature, peace, economics;

		private static final List<PrizeCategories> VALUES = Collections.unmodifiableList(Arrays.asList(values()));
		private static final int SIZE = VALUES.size();
		private static final Random RANDOM = new Random();

		public static PrizeCategories getRandomCategory()
		{
			return VALUES.get(RANDOM.nextInt(SIZE));
		}
	}
}
