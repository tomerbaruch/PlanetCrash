package PlanetCrash.core.Game;

import java.awt.Color;
import java.io.File;
import java.util.Collections;
import java.util.List;

import PlanetCrash.db.Users.User;

public class Game {
	User user;
	int difficulty, lives;
	Color backdrop,land;
	String country;
	List<Question> questions=null;
	int currentQuestion,countryId;
	String planet;

	File soldier;
	
	public static final Color[] BACKDROPS = {Color.decode("#CDE1C4"),Color.decode("#8EA5FF"),Color.decode("#9B9EAF"),Color.decode("#8EAAA5"),Color.CYAN, Color.pink, Color.gray, Color.blue, Color.decode("#F9F3DF")};
	public static final Color[] LANDS = {Color.decode("#2B1D3E"),Color.decode("#A7E755"), Color.magenta, Color.decode("#002D4E"), Color.decode("#F9F0C7"), Color.decode("#FDB9B0"), Color.decode("#EFCDB2"),Color.decode("#A78404")};
	
	public Game() {
		
	}
	
	public void setUser(User user) {
		this.user=user;
	}
	
	public User getUser() {
		return user;
	}
	
	public void setDifficulty(int difficulty) {
		this.difficulty=difficulty;
	}
	
	public int getDifficulty() {
		return difficulty;
	}
	
	public void setBackdrop(Color color) {
		this.backdrop=color;
	}
	
	public void setLand(Color color) {
		this.land=color;
	}
	
	public Color getBackdrop() {
		return backdrop;
	}
	
	public Color getLand() {
		return land;
	}
	
	public void setQuestions(List<Question> questions) {
		this.questions=questions;
		Collections.shuffle(this.questions);
	}
	
	public List<Question> getQuestions() {
		return questions;
	}
	
	public Question getQuestion(int i) {
		return questions.get(i);
	}
	
	public void setCountry(String countryName) {
		country=countryName;
	}
	
	public String getCountry() {
		return country;
	}
	
	public void setSoldier(File soldier) {
		this.soldier=soldier;
	}
	
	public File getSoldier() {
		return soldier;
	}
	
	public void setLives(int lives) {
		this.lives=lives;
	}
	
	public int getLives() {
		return lives;
	}
	
	public void decLives() {
		this.lives--;
	}
	
	public void setCurrentQuestion(int crnt) {
		currentQuestion = crnt;
	}
	
	public int getCurrentQuestion() {
		return currentQuestion;
	}
	
	public void setPlanetName(String name) {
		this.planet=name;
	}
	
	public String getPlanetName() {
		return planet;
	}
	
	public void advanceQuestion() {
		currentQuestion++;
	}
	
	public void setCountryId(int id) {
		countryId=id;
	}
	
	public int getCountryId() {
		return countryId;
	}
}
