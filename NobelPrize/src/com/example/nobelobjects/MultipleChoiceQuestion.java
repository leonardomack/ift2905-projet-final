package com.example.nobelobjects;

import java.util.ArrayList;

import com.example.nobelprize.R;

public class MultipleChoiceQuestion {

	/**
	 * il faudra les shuffler pour l'affichage
	 * il faudra aussi afficher l'image...
	 */
	public ArrayList<String> printedAnswers;

	/**contient les bonnes reponses = peut y en avoir plusieurs
	 * ex = qlqun qui a gagné plsrs prix nobels*/
	public ArrayList<String> rightAnswers;

	public boolean isAnsweredCorrectly;
	public boolean isAnswered;
	public String questionString;
	public int questionNumber;
	public int type;

	//specific questions
	private String laureateName; 
	private String category ; 
	private int year; 


	public MultipleChoiceQuestion (int questionNumber,int typeQuestion,ArrayList<String> printedAnswers, ArrayList<String> rightAnswers){
		this.questionNumber = questionNumber;
		this.type = typeQuestion;
		this.printedAnswers=printedAnswers;
		this.rightAnswers=rightAnswers;						
		this.isAnswered=false;
		this.isAnsweredCorrectly=false;
		generateQuestionDependingType(typeQuestion);

	}
	// Born city
	public MultipleChoiceQuestion (int questionNumber,int typeQuestion,ArrayList<String> printedAnswers, ArrayList<String> rightAnswers, String laureateName){
		this(questionNumber, typeQuestion, printedAnswers, rightAnswers);
		this.laureateName= laureateName;
		generateQuestionDependingType(typeQuestion);
	}

	// Category of Nobel Prize
	public MultipleChoiceQuestion (int questionNumber,int typeQuestion,ArrayList<String> printedAnswers, ArrayList<String> rightAnswers, String laureateName, String category){
		this(questionNumber, typeQuestion, printedAnswers, rightAnswers, laureateName);
		this.category = category;

		generateQuestionDependingType(typeQuestion);
	}

	//Laureate who won
	public MultipleChoiceQuestion (int questionNumber,int typeQuestion,ArrayList<String> printedAnswers, ArrayList<String> rightAnswers, String category, int year){
		this(questionNumber, typeQuestion, printedAnswers, rightAnswers);
		this.category = category;
		this.year = year;

		generateQuestionDependingType(typeQuestion);
	}


	/**
	 * dans les descendants, i lfaudra overrider cette methode
	 * @param typeQuestion
	 */
	protected void generateQuestionDependingType(int typeQuestion) {
		switch(typeQuestion){
		case 1 :
			this.questionString = laureateName + "'s city of birth was :";
			break;
		case 2 :
			this.questionString = laureateName + " won his "+category+"Nobel prize in :" ; 
			break;
		case 3 :
			this.questionString = "In "+year+", "+category+" Nobel prize was discerned to :" ;
			break;
		}		
	}

	/**
	 * on considère deux quqestions egales si elles sont du même type et si elles ont les mêmes réponses (dans le même ordre)
 Non testé... easy coder ^^
	 */
	@Override
	public boolean equals(Object o) {

		if(o instanceof MultipleChoiceQuestion && 				
				((MultipleChoiceQuestion)o).getType() ==  this.getType() 						
				&& ((MultipleChoiceQuestion)o).getRightAnswers().equals(this.getRightAnswers()) 				)
		{
			return true;
		} else 
		{
			return false;
		}
	}


	public ArrayList<String> getPrintedAnswers() {
		return printedAnswers;
	}


	public void setPrintedAnswers(ArrayList<String> printedAnswers) {
		this.printedAnswers = printedAnswers;
	}


	public ArrayList<String> getRightAnswers() {
		return rightAnswers;
	}


	public void setRightAnswers(ArrayList<String> rightAnswers) {
		this.rightAnswers = rightAnswers;
	}


	boolean randomFiftyPercentChance()
	{   
		return Math.random() < 0.50;
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




	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String toString(){
		return "\n\nLa question n°"+questionNumber+" est : "+ questionString 
				+"\nLes réponses affichées sont : "+ printedAnswers.toString()
				+"\nLes réponses correctes sont : "+ rightAnswers.toString();
	}
}
