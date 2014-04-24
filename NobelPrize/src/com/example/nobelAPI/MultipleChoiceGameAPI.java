package com.example.nobelAPI;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import android.util.Log;

import com.example.nobelobjects.Laureate;
import com.example.nobelobjects.MultipleChoiceQuestion;
import com.example.nobelobjects.Prize;
import com.example.nobelobjects.WhoAmIQuestion;

public class MultipleChoiceGameAPI {

	private String prizeURL;
	private String laureateURL;
	private ArrayList<MultipleChoiceQuestion> questions;

	private ArrayList<ArrayList<Laureate>> laureatesList;
	private String erreur;
	private final static int AMOUNT_OF_QUESTIONS = 5;
	private final int LAST_LAUREATE = 896; //dernier lauréat de la liste répertorié Avril 2014
	private final static String TAG = "MultipleChoiceGameAPI";

	/**
	 * créé les questions à partir de la list de laureats donnée
	 * @param laureates
	 */
	public MultipleChoiceGameAPI(ArrayList<ArrayList<Laureate>> laureatesList){
		this.laureatesList = laureatesList;
		questions = new ArrayList<MultipleChoiceQuestion>();
		erreur = null;

		for(int questionNumber = 1 ; questionNumber <= AMOUNT_OF_QUESTIONS ; questionNumber++){
			MultipleChoiceQuestion questionElement = computeRandomQuestion(questionNumber);
			questions.add(questionElement);
			Log.v(TAG,questionElement.toString());
		}
	}
 
	/**
	 * @param questionNumber
	 * @return
	 */
	
	

	private MultipleChoiceQuestion computeRandomQuestion(int questionNumber) {
		ArrayList<Laureate> laureates = laureatesList.get(questionNumber-1); 

		//bonne reponse
		Laureate laureate = laureates.get(0);
		List<Prize> prizes = laureate.getPrizes();

		ArrayList<String> randomAnswers = new ArrayList<String>();
		ArrayList<String> rightAnswers = new ArrayList<String>();
		ArrayList<String> answersToPrint = new ArrayList<String>();

		//on choisit l'un des trois types de question
		int type = randomDifferentTypes(3);

		switch(type){
		case 1 :			
			rightAnswers.add(laureate.getBornCity());
			randomAnswers = randomBornCities(laureate, randomAnswers, laureates);
			break;
			
		case 2 :
			rightAnswers.add(prizes.get(0).getYear());
			randomAnswers = randomYears(randomAnswers);
			break;
		
		case 3 :
			rightAnswers.add(laureate.getFirstname()+" "+laureate.getSurname());
			randomAnswers = randomLaureate(randomAnswers); 
			
			break ;
		}
			
		answersToPrint.addAll(randomAnswers);
		//on ajoute une des reponses possibles au champ, toujours la première...
		answersToPrint.add(rightAnswers.get(0));

		MultipleChoiceQuestion question= null;
		
		switch(type){
		case 1 :
			String laureateName = laureate.getFirstname()+" "+laureate.getSurname() ;
			question = new MultipleChoiceQuestion(questionNumber, type , answersToPrint, rightAnswers, laureateName);
			break;
			
		case 2 :
			String laureateName2 = laureate.getFirstname()+" "+laureate.getSurname() ;
			String category = prizes.get(randomDifferentTypes(prizes.size())).getCategory();
			question = new MultipleChoiceQuestion(questionNumber, type , answersToPrint, rightAnswers, laureateName2, category);
			break;
		
		case 3 :
			int i = randomDifferentTypes(prizes.size());
			String category2 = prizes.get(i).getCategory();
			int year = prizes.get(i).getYear();
			question = new MultipleChoiceQuestion(questionNumber, type , answersToPrint, rightAnswers, category2, year);
			break;
		}
		
		return question;
	}


// 	public void randomAnswers() prendrait dans toutes les villes 3 diff�rentes de laureate.getBornCity()
	
	
	private int randomDifferentTypes(int i) {
		return randomMinMax(1,i);
	}

	private ArrayList<String> getCategoriesWon(Laureate laureate) {
		ArrayList<String> categoriesWon = new ArrayList<String>();
		for(Prize p : laureate.getPrizes())
			categoriesWon.add(p.getCategory());
		return categoriesWon;
	}

	private ArrayList<String> fetchRandomNames( ArrayList<Laureate> randomLaureates){
		ArrayList<String> answers = new ArrayList<String>();  

		//remplacer 4 par "number - od other choices"
		for(int i = 1; i < 4 ; i++){
			Laureate tempLaureate = randomLaureates.get(i);
			answers.add(tempLaureate.getFirstname()+" "+tempLaureate.getSurname());
		}
		return answers;
	}

	public static int getAmountOfQuestion(){
		return AMOUNT_OF_QUESTIONS;
	}


	private ArrayList <String>  fetchRandomCategories(String answer){
		ArrayList <String> selectedCategories = new ArrayList<String>();

		for (int i = 0; i < 3 ; i++){
			String category = null;
			do{
				int sel = randomMinMax(0, 5);
				switch(sel){
				case 0:
					category = "economics";
					break;
				case 1:
					category = "peace";
					break;
				case 2:
					category = "literature";
					break;
				case 3:
					category = "medicine";
					break;
				case 4:
					category = "chemistry";
					break;
				case 5:
					category = "physics";
					break;
				}
			}while(selectedCategories.contains(category) || category.equals(answer));

			selectedCategories.add(category);
		}
		return selectedCategories;
	}

	/**
	 * utiliser pour un 3eme type de question
	 * @param rightYear
	 * @return
	 */
	private int fetchRandomYear(int rightYear){
		int year = -1;
		int firstYearOfNobelPrize = 1901;
		int thisYear = (Calendar.getInstance().get(Calendar.YEAR)) -1;
		do{
			year = randomMinMax(firstYearOfNobelPrize, thisYear);
		}while(year == rightYear);
		return year;
	}

	public ArrayList<MultipleChoiceQuestion> getQuestions() {
		return questions;
	}

	/*
	 * Méthodes aléatoires
	 */
	private int randomMinMax(int min, int max){
		Random rand = new Random();
		return rand.nextInt((max-min)+1)+min;
	}

	public void shuffleQuestions(){
		for(MultipleChoiceQuestion q : questions){
			if (q != null)
				Collections.shuffle(q.getPrintedAnswers());
		}
	}

}

final ArrayList<String> randomBornCities (Laureate laureate2, ArrayList<String> randomAnswers, ArrayList<Laureate> laureates) {
	int i = 0; 
	while (i < 3) {		
		if ( // si c'est les m�mes objets ) {
			break; 
		}
		else {
			randomAnswers.add(laureates.get(i).getBornCity());
			i++;
		}				
	}
	return randomAnswers ;
}

final ArrayList<String> randomYears(ArrayList<String> randomAnswers, Laureate laureate, ArrayList<String> prizes) {
	int i = 0; 
	while (i < 3) {
			randomAnswers.add(laureate.getPrizes().get(0).getYear()+i);
			i++;
		}		
	}
	return randomAnswers ;
}

final ArrayList<String> randomLaureate (ArrayList<String> laureates, ArrayList<String> randomAnswers, Laureate laureate) {
	int i = 0; 
	while (i <3) {
		if (laureate == laureates.get(i)) {
		break;	
		}
		else {
			randomAnswers.add(laureate.getFirstname()+" "+laureate.getSurname());
			i++; 
		}
	}
	return randomAnswers;

}

