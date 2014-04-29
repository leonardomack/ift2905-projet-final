package com.example.nobelobjects;

public class Trophy {
	private boolean hasIt;
	private String trophyName;
	
	public Trophy(String trophyName){
		this.trophyName=trophyName;
		this.hasIt = false;
	}
	public Trophy(String trophyName, int hasIt){
		this.trophyName=trophyName;
		if(hasIt==1)
			this.hasIt = true;
		else
			this.hasIt=false;
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
