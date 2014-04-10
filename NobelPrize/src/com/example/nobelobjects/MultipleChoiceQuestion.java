package com.example.nobelobjects;

public class MultipleChoiceQuestion {

	public boolean answer;
	public boolean isAnsweredCorrectly;
	public boolean isAnswered;
	public String questionString;
	public int questionNumber; 
	
	
	// Born city
	public MultipleChoiceQuestion newMultipleChoiceQuestionType1(int num, String laureateName, boolean answer){
		this.questionString = laureateName + "'s born city was :";
		this.questionNumber = num;
		this.isAnswered=false;
		this.isAnsweredCorrectly=false;
		this.answer=answer;
		return this;
	}
	
	// Category of Nobel Prize
	public MultipleChoiceQuestion newMultipleChoiceQuestionType2(int num, String laureateName, String category, int share,boolean answer){
		this.questionString = laureateName + "won his "+category+"Nobel prize in :" ; 
		this.questionNumber = num;
		this.isAnswered=false;
		this.isAnsweredCorrectly=false;
		this.answer=answer;
		return this;
	}
	
	//Laureate who won
	public MultipleChoiceQuestion newMultipleChoiceQuestionType3(int num, int year, String category, int share,boolean answer){
		this.questionString = "In"+year+","+category+"Nobel prize was discerned to :" ; 
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
