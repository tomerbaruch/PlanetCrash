package PlanetCrash.parsing.Yago;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import PlanetCrash.parsing.entities.entity_country;

public class parser_yago_literal_facts extends AbstractYagoParser{
	
	
	HashMap<String, entity_country> countries_map;
	
	public parser_yago_literal_facts(String filepath, HashMap<String, entity_country> countries_map) {		
		super(filepath);
		
		this.countries_map = countries_map;
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean parse(YagoEntry toParse) {
		
		String clean_lentity = entity_cleaner(toParse.lentity);
		if (clean_lentity==null){
			return false;
		}
		
		entity_country country = countries_map.get(clean_lentity);
		if (country!=null){
			if (toParse.relation.equals(properties.get_yago_tag_population())){
				int population = 0;
				Pattern p = Pattern.compile("\"([^\"]*)\"");
				Matcher m = p.matcher(toParse.rentity);
				while (m.find()) {
				  population = Integer.parseInt((m.group(1)));
				}
				if (population > 3000){
					country.setPopulation_size(population);
					return true;
				}
			}
		}	
		return false;
	}
	

}
