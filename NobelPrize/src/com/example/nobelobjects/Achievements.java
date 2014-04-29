package com.example.nobelobjects;

import java.util.ArrayList;

public class Achievements {
	private ArrayList<Trophy> trophies;
	
	public Achievements(){
		trophies.add(new Trophy("Use a tip to solve a question"));
		trophies.add(new Trophy("Answer correctly a question without using a tip"));
		trophies.add(new Trophy("Answer 3 question correctly consecutively"));
		trophies.add(new Trophy("Answer correctly 5 questions from each game"));
		trophies.add(new Trophy("Obtain every other trophies"));
	}
	
	public ArrayList<Trophy> getTrophies(){
		return trophies;
	}
}
