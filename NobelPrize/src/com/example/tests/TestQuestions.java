package com.example.tests;

import java.util.ArrayList;

import com.example.nobelAPI.WhoAmIGameAPI;
import com.example.nobelobjects.WhoAmIQuestion;

public class TestQuestions {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		WhoAmIGameAPI game = new WhoAmIGameAPI();

		ArrayList<WhoAmIQuestion> questions = game.getQuestions();

		System.out.println(questions.toString());
	}

}
