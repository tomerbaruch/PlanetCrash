package PlanetCrash.parsing.Yago;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


import java.util.regex.Matcher;
import java.util.regex.Pattern;

import PlanetCrash.parsing.entities.entity_person;
public class parser_yago_date_facts extends AbstractYagoParser{
	
	
	HashMap<String, entity_person> persons_map;
	
	public parser_yago_date_facts(String filepath, HashMap<String, entity_person> persons_map) {		
		super(filepath);
		
		this.persons_map = persons_map;
	}

	@Override
	public boolean parse(YagoEntry toParse) {
		
		String clean_lentity = entity_cleaner(toParse.lentity);
		if (clean_lentity==null){
			return false;
		}
		
		entity_person person = persons_map.get(clean_lentity);
		if (person!=null){
			if (toParse.relation.equals(properties.get_yago_tag_birth_date())){
				person.setYearOfBirth(getYearFromYagoDate(toParse.rentity));
			}
			else if (toParse.relation.equals(properties.get_yago_tag_death_date())){
				person.setYearOfDeath(getYearFromYagoDate(toParse.rentity));
			}
			return true;
		}
		return false;
	}
	
	public int getYearFromYagoDate(String toParse){
		int year = 0;
		String date = "";
		Pattern p = Pattern.compile("\"([^\"]*)\"");
		Matcher m = p.matcher(toParse);
		while (m.find()) {
			date = m.group(1);
		}
		
		try{
		year = Integer.parseInt(date.split("-")[0]);
		} catch(NumberFormatException e) {
			return -1;
		}
		return year;
	}
	

}
