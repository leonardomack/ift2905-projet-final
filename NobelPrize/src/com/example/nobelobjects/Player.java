package com.example.nobelobjects;

import android.content.Context;
import android.util.Log;

import com.example.db.DBHelperNobelPrize;

public class Player {
	DBHelperNobelPrize dbHelper;
	String username;
	int scoreTrueFalse;
	int totalTrueFalse;
	int scoreQCM;
	int totaQCM;
	int scorePicture;
	int totalPicture;
	Achievements trophies;

	//rb on utilise un int...
	//on remet a zero quand l'user lcicque sur le buton trophees pour voir...
	private int hasNewTrophies;
	//modifie a true qud ajoute trophe
	//modifie a false qd on regarde si a nouveau trophees

	public Player(Context context, String username){
		dbHelper = new DBHelperNobelPrize(context);
		if(dbHelper.playerExists(username)){
			Log.d("DB","Player : "+ username + " exists");
			this.username=username;
			trophies = dbHelper.getTrophies(this.username);
		}
		else{
			Log.d("DB","Player : "+ username + " doesn't exists");
			dbHelper.createNewPlayer(username);
			this.username=username;
			trophies=new Achievements();
		}
	}

	public String getUsername() {
		return username;
	}

	public int getScoreTrueFalse() {
		return dbHelper.getScoreOfTrueFalseGame(username);
	}
	public void setScoreTrueFalse(int score){
		dbHelper.modifyScoreTrueFalseGame(username, score);
	}
	public int getTotalTrueFalse() {
		return dbHelper.getTotalOfTrueFalseGame(username);
	}
	public void setTotalTrueFalse(int total){
		dbHelper.modifyTotalTrueFalseGame(username, total);
	}

	public int getScoreQCM() {
		return dbHelper.getScoreOfQCMGame(username);
	}
	public void setScoreQCM(int score){
		dbHelper.modifyScoreQCMGame(username, score);
	}
	public int getTotalQCM() {
		return dbHelper.getTotalOfQCMGame(username);
	}
	public void setTotalQCM(int total){
		dbHelper.modifyTotalQCMGame(username, total);
	}

	public int getScorePicture() {
		return dbHelper.getScoreOfPictureGame(username);
	}
	public void setScorePicture(int score){
		dbHelper.modifyScorePictureGame(username, score);
	}
	public int getTotalPicture() {
		return dbHelper.getTotalOfPictureGame(username);
	}
	public void setTotalPicture(int total){
		dbHelper.modifyTotalPictureGame(username, total);
	}

	public void addScoreTrueFalse(int score, int gamePlayed){
		int currentScore = getScoreTrueFalse();
		int currentTotal = getTotalTrueFalse();
		setScoreTrueFalse(currentScore+score);
		setTotalTrueFalse(currentTotal+gamePlayed);
	}
	public void addScoreQCM(int score, int gamePlayed){
		int currentScore = getScoreQCM();
		int currentTotal = getTotalQCM();
		setScoreQCM(currentScore+score);
		setTotalQCM(currentTotal+gamePlayed);
	}
	public void addScorePicture(int score, int gamePlayed){
		int currentScore = getScorePicture();
		int currentTotal = getTotalPicture();
		setScorePicture(currentScore+score);
		setTotalPicture(currentTotal+gamePlayed);
	}
	public void activateTrophyUseATips(){
		if(!trophies.get(0).hasTrophy()){
			dbHelper.activateTrophyUseATips(username);
			dbHelper.modifyHasNewTrophies(username, 1);
		}
	}
	public void activateTrophyNoTips(){
		if(!trophies.get(1).hasTrophy()){
			dbHelper.activateTrophyNoTips(username);
			dbHelper.modifyHasNewTrophies(username, 1);
		}
	}
	public void activateTrophy3Consecutive(){
		if(!trophies.get(2).hasTrophy()){
			dbHelper.activateTrophy3Consecutive(username);
			dbHelper.modifyHasNewTrophies(username, 1);
		}
	}
	public void activateTrophy5TrueFromEveryGame(){
		if(!trophies.get(3).hasTrophy()){
			dbHelper.activateTrophy5TrueFromEveryGame(username);
			dbHelper.modifyHasNewTrophies(username, 1);
		}
	}
	public void activateTrophyMyNobelPrize(){
		if(!trophies.get(4).hasTrophy()){
			dbHelper.activateTrophyMyNobelPrize(username);
			dbHelper.modifyHasNewTrophies(username, 1);
		}
	}
	public Achievements getTrophies(){
		if(!trophies.get(3).hasTrophy()){
			int correctQCM= getScoreQCM();
			int correctTrueFalse = getScoreTrueFalse();
			int correctPicture = getScorePicture();
			if(correctQCM >=5 && correctTrueFalse >= 5 && correctPicture >=5)
				activateTrophy5TrueFromEveryGame();
		}
		if(trophies.get(0).hasTrophy() &&
				trophies.get(1).hasTrophy() &&
				trophies.get(2).hasTrophy() &&
				trophies.get(3).hasTrophy()){
			activateTrophyMyNobelPrize();
		}
		return dbHelper.getTrophies(this.username);
	}


	public int getTotalGames(){
		return getTotalPicture()+getTotalQCM()+getTotalTrueFalse();
	}

	public int getScoreGames(){
		return getScorePicture()+getScoreQCM()+getScoreTrueFalse();
	}

	public int getHasNewTrophies(){
		return dbHelper.getHasNewTrophiesFromDB(username);
	}

	public void resetHasNewTrophies(){
		dbHelper.modifyHasNewTrophies(username, 0);
	}

	public String toString(){
		return username + " : " + getScoreTrueFalse()+"/"+getTotalTrueFalse() + " | "
				+ getScoreQCM()+"/"+getTotalQCM() + " | "
				+ getScorePicture()+"/"+getTotalPicture()
				+getScoreGames()+"/"+getTotalGames();
	}
}
