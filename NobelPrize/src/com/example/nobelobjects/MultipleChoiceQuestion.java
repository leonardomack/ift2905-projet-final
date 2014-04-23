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
	/**
	 * remplacer boolean type par un enum... pour l'instant 2 type de questions
	 * faire factory de question = après avoir testé la fonctionnalité
	 * @param num
	 * @param laureateName
	 * @param year
	 * @param category
	 * @param answer
	 */
	public MultipleChoiceQuestion(int questionNumber,int typeQuestion,ArrayList<String> printedAnswers, ArrayList<String> rightAnswers) {
		super();

		//ou passer 3 autres string...
		this.questionNumber = questionNumber;

		this.type = typeQuestion;

		this.printedAnswers=printedAnswers;
		this.rightAnswers=rightAnswers;
		
		generateQuestionDependingType(typeQuestion);
		
		this.isAnswered=false;
		this.isAnsweredCorrectly=false;
	}

	/**
	 * dans les descendants, i lfaudra overrider cette methode
	 * @param typeQuestion
	 */
private void generateQuestionDependingType(int typeQuestion) {
	switch(typeQuestion){
	case 1 :
		this.questionString = "Question1?";
		break;
	case 2 :
		this.questionString = "Question2";
		break;
	case 3 :
		this.questionString = "question3";
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
