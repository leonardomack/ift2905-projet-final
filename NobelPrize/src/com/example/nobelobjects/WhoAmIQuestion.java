package com.example.nobelobjects;

import java.util.ArrayList;

import com.example.nobelprize.GlobalConstants;

/**
 * variante de QCM, réfléchir à les faire hériter d'un ancêtre commun
 * @author locust
 *
 */
public class WhoAmIQuestion extends MultipleChoiceQuestion implements GlobalConstants{
	private String urlImage;

	/**
	 * remplacer boolean type par un enum... pour l'instant 2 type de questions
	 * faire factory de question = après avoir testé la fonctionnalité
	 * @param num
	 * @param laureateName
	 * @param year
	 * @param category
	 * @param answer
	 */
	public WhoAmIQuestion(int questionNumber,int typeQuestion,ArrayList<String> printedAnswers, ArrayList<String> rightAnswers,String urlImage) {
		super(questionNumber,typeQuestion,printedAnswers,rightAnswers);
		this.urlImage = urlImage;
	}
	/**
	 * dans les descendants, i lfaudra overrider cette methode
	 * @param typeQuestion
	 */
	@Override
	protected void generateQuestionDependingType(int typeQuestion) {
		switch(typeQuestion){
		case 1 :
			this.questionString = "Which Nobel Prize have I won ?";
			break;
		case 2 :
			this.questionString = "Who Am I ?";
			break;
		}		
	}

	public String getUrlImage() {
		return urlImage;
	}

	public void setUrlImage(String urlImage) {
		this.urlImage = urlImage;
	}	
	
public ArrayList<Integer> getIndexRightAnswersInPrinted(){
		
		ArrayList<Integer> indexRightAnswers = new ArrayList<Integer>();
		
		for(int i = 0 ; i < AMOUNT_OF_ANSWERS ; i++){
			if(this.getRightAnswers().contains(getPrintedAnswers().get(i))){
				indexRightAnswers.add(i);
			}
		}
		return indexRightAnswers;
	}

	/**
	 * on considère deux quqestions egales si elles sont du même type et si elles ont les mêmes réponses (dans le même ordre)
 Non testé... easy coder ^^
	 */
	@Override
	public boolean equals(Object o) {

		if(o instanceof WhoAmIQuestion && 

				((WhoAmIQuestion)o).getType() ==  this.getType() 

				&& ((WhoAmIQuestion)o).getRightAnswers().equals(this.getRightAnswers()) 
				)
		{
			return true;
		} else 
		{
			return false;
		}
	}
	
}
