package PlanetCrash.parsing.entities;

public class entity_city {
	private String name;
	private String country;
	
	public entity_city(String name){
		this.name = name;
	}
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
}
