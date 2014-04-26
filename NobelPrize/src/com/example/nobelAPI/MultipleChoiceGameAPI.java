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
	private final static int AMOUNT_OF_ANSWERS = 4;
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
			randomAnswers = distinctBornCities(laureate, laureates);
			break;

		case 2 :
			rightAnswers.add(String.valueOf(prizes.get(0).getYear()));
			randomAnswers = distinctYears(laureate);
			break;

		case 3 :
			rightAnswers.add(laureate.getFirstname()+" "+laureate.getSurname());
			randomAnswers = distinctLaureates(laureates); 
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
			String category = prizes.get(randomMinMax(0, prizes.size()-1)).getCategory();
			question = new MultipleChoiceQuestion(questionNumber, type , answersToPrint, rightAnswers, laureateName2, category);
			break;

		case 3 :
			String category2 = prizes.get(0).getCategory();
			int year = prizes.get(0).getYear();
			question = new MultipleChoiceQuestion(questionNumber, type , answersToPrint, rightAnswers, category2, year);
			break;
		}

		return question;
	}

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



	private ArrayList<String> distinctBornCities (Laureate laureate, ArrayList<Laureate> laureates) {
		ArrayList<String> distinctAnswers = new ArrayList<String>();
		for(int i=1; i < laureates.size(); i++){
			if(!laureates.get(i).getBornCity().equals(laureate.getBornCity())  && !distinctAnswers.contains(laureates.get(i).getBornCity()))
				distinctAnswers.add(laureates.get(i).getBornCity());
		}
		
		if(distinctAnswers.size()<AMOUNT_OF_ANSWERS-1)
			distinctAnswers.add("Mantes-la-jolie");

		if(distinctAnswers.size()<AMOUNT_OF_ANSWERS-1)
			distinctAnswers.add("Clermont-Ferrand");

		if(distinctAnswers.size()<AMOUNT_OF_ANSWERS-1)
			distinctAnswers.add("Digne-les-bains");
		
		return distinctAnswers ;
	}

	private ArrayList<String> distinctYears(Laureate laureate) {
		ArrayList<String> distinctAnswers = new ArrayList<String>();
		for(int i = 1 ; i < AMOUNT_OF_ANSWERS; i++ )
			distinctAnswers.add(String.valueOf(laureate.getPrizes().get(0).getYear()+i));

		return distinctAnswers ;
	}

	final ArrayList<String> distinctLaureates (ArrayList<Laureate> laureates) {
		ArrayList<String> distinctAnswers = new ArrayList<String>();
		for(int i = 1 ; i < laureates.size();i++)
			distinctAnswers.add(laureates.get(i).getFirstname()+" "+laureates.get(i).getSurname());

		return distinctAnswers;
	}
}

