package com.example.nobelobjects;

public class Trophy {
	private boolean hasIt;
	private String trophyName;
	
	public Trophy(String trophyName){
		this.trophyName=trophyName;
		boolean hasIt = false;
	}
	
	public String getTrophyName(){
		return trophyName;
	}
	
	public void activateTrophy(){
		this.hasIt=true;
	}
	
	public boolean hasTrophy(){
		return hasIt;
	}
}
