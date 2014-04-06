package com.example.nobelobjects;

public class TrueFalseQuestion {
	
	public boolean answer;
	public boolean isAnsweredCorrectly;
	public boolean isAnswered;
	public String questionString;
	public int questionNumber;
	
	
	public TrueFalseQuestion newTrueFalseQuestionType1(int num, String laureateName, int year, String category, boolean answer){
		this.questionString = laureateName + " was awarded the Nobel "+category+" Prize in " + year;
		this.questionNumber = num;
		this.isAnswered=false;
		this.isAnsweredCorrectly=false;
		this.answer=answer;
		return this;
	}
	
	public TrueFalseQuestion newTrueFalseQuestionType2(int num, int year, String category, int share,boolean answer){
		this.questionString = "The Nobel " +category + " prize of "+year+" was shared among "+ share;
		this.questionNumber = num;
		this.isAnswered=false;
		this.isAnsweredCorrectly=false;
		this.answer=answer;
		return this;
	}

	public boolean getAnswer() {
		return answer;
	}

	public void setAnswer(boolean answer) {
		this.answer = answer;
	}

	public boolean isAnsweredCorrectly() {
		return isAnsweredCorrectly;
	}

	public void setAnsweredCorrectly(boolean isAnsweredCorrectly) {
		this.isAnsweredCorrectly = isAnsweredCorrectly;
	}

	public boolean isAnswered() {
		return isAnswered;
	}

	public void setAnswered(boolean isAnswered) {
		this.isAnswered = isAnswered;
	}

	public String getQuestionString() {
		return questionString;
	}

	public void setQuestionString(String questionString) {
		this.questionString = questionString;
	}

	public int getQuestionNumber() {
		return questionNumber;
	}

	public void setQuestionNumber(int questionNumber) {
		this.questionNumber = questionNumber;
	}
	
	boolean randomFiftyPercentChance()
	{   
	   return Math.random() < 0.50;
	}
	
	public String toString(){
		return questionString;
	}
	
}
