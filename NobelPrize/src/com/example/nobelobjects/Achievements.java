package com.example.nobelobjects;

import java.util.ArrayList;

public class Achievements {
	private ArrayList<Trophy> trophies;
	
	public Achievements(){
		trophies = new ArrayList<Trophy>();
		trophies.add(new Trophy("Use a tip to solve a question"));
		trophies.add(new Trophy("Answer correctly a question without using a tip"));
		trophies.add(new Trophy("Answer 3 question correctly consecutively"));
		trophies.add(new Trophy("Answer correctly 5 questions from each game"));
		trophies.add(new Trophy("Obtain every other trophies"));
	}
	public Achievements(int[] dbTrophies){
		trophies = new ArrayList<Trophy>();
		trophies.add(new Trophy("Use a tip to solve a question", dbTrophies[0]));
		trophies.add(new Trophy("Answer correctly a question without using a tip", dbTrophies[1]));
		trophies.add(new Trophy("Answer 3 question correctly consecutively", dbTrophies[2]));
		trophies.add(new Trophy("Answer correctly 5 questions from each game", dbTrophies[3]));
		trophies.add(new Trophy("Obtain every other trophies", dbTrophies[4]));
	}
	
	public ArrayList<Trophy> getTrophies(){
		return trophies;
	}
	
	public Trophy get(int index){
		return trophies.get(index);
	}
}
