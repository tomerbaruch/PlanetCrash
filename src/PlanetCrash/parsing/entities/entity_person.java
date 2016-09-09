package PlanetCrash.parsing.entities;

import java.util.ArrayList;

public class entity_person {
	private String name;
	private int yearOfBirth;
	private int yearOfDeath;
	private String placeOfBirth;
	private ArrayList<String> profession_list = new ArrayList<String>();
	private ArrayList<String> awards_list = new ArrayList<String>();
	
	public entity_person(){
	
	}
	
	public entity_person(String name,int yearOfBirth,int yearOfDeath,String placeOfBirth){
		this.name = name;
		this.yearOfBirth = yearOfBirth;
		this.yearOfDeath = yearOfDeath;
		this.placeOfBirth = placeOfBirth;
	}
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getYearOfBirth() {
		return yearOfBirth;
	}

	public void setYearOfBirth(int yearOfBirth) {
		this.yearOfBirth = yearOfBirth;
	}

	public int getYearOfDeath() {
		return yearOfDeath;
	}

	public void setYearOfDeath(int yearOfDeath) {
		this.yearOfDeath = yearOfDeath;
	}

	public String getPlaceOfBirth() {
		return placeOfBirth;
	}

	public void setPlaceOfBirth(String placeOfBirth) {
		this.placeOfBirth = placeOfBirth;
	}

	public ArrayList<String> getProfession_list() {
		return profession_list;
	}
	
	public ArrayList<String> getAwards_list() {
		return awards_list;
	}

	public void addProfession(String profession) {
		this.profession_list.add(profession);
	}
	
	public void addAward(String award) {
		this.awards_list.add(award);
	}
}
