package PlanetCrash.parsing.entities;

public class entity_country {
	/*private members*/
	private String yago_name;
	private String label; 
	private String continent; 
	private String currency;
	private String language;
	private String capital; 
	private int population_size; 
	
	public entity_country(){
		
	}
	
	public entity_country(String yago_name,String label,String continent,String currency,String language,String capital,int population_size,String leader){
		this.yago_name = yago_name;
		this.label = label;
		this.continent = continent;
		this.currency = currency;
		this.language = language;
		this.capital = capital;
		this.population_size = population_size;
	}
	public entity_country(String yago_name,String continent,String currency,String language,String capital,int population_size){
		this.yago_name = yago_name;
		this.continent = continent;
		this.currency = currency;
		this.language = language;
		this.capital = capital;
		this.population_size = population_size;
	}

	public String getYagoName() {
		return yago_name;
	}
	public void setYagoName(String name) {
		this.yago_name = name;
	}
	public String getContinent() {
		return continent;
	}
	public void setContinent(String continent) {
		this.continent = continent;
	}
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	public String getLanguage() {
		return language;
	}
	public void setLanguage(String language) {
		this.language = language;
	}
	public String getCapital() {
		return capital;
	}
	public void setCapital(String capital) {
		this.capital = capital;
	}
	public int getPopulation_size() {
		return population_size;
	}
	public void setPopulation_size(int population_size) {
		this.population_size = population_size;
	}
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}

}
