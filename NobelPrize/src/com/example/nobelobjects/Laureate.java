package com.example.nobelobjects;

public class Laureate {
	private int id;
	private String firstname;
	private String surname;
	private String motivation;
	private int share;
	
	
	public Laureate(int id,String firstname,String surname){
		this.id = id;
		this.firstname=firstname;
		this.surname=surname;
	}


	public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
	}


	public String getFirstname() {
		return firstname;
	}


	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}


	public String getSurname() {
		return surname;
	}


	public void setSurname(String surname) {
		this.surname = surname;
	}


	public String getMotivation() {
		return motivation;
	}


	public void setMotivation(String motivation) {
		this.motivation = motivation;
	}


	public int getShare() {
		return share;
	}


	public void setShare(int share) {
		this.share = share;
	}
	
	public String toString(){
		return "id:"+id+" name: "+ firstname +" "+ surname;
	}
	
	
}
