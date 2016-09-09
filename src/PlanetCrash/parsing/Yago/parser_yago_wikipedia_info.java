package PlanetCrash.parsing.Yago;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import PlanetCrash.parsing.entities.entity_city;
import PlanetCrash.parsing.entities.entity_country;
import PlanetCrash.parsing.entities.entity_person;
import PlanetCrash.parsing.entities.entity_university;

public class parser_yago_wikipedia_info extends AbstractYagoParser{

	HashMap<String, entity_person> persons_map;
	HashMap<String, entity_person> lite_persons_map = new HashMap<String, entity_person>();




	
	public parser_yago_wikipedia_info(String filepath, HashMap<String, entity_person> persons_map, HashMap<String, entity_person> lite_persons_map) {		
		super(filepath);

		this.persons_map = persons_map;
		this.lite_persons_map = lite_persons_map;

	}

	@Override
	public boolean parse(YagoEntry toParse) {
		boolean flag=false;
		
		String clean_lentity = entity_cleaner(toParse.lentity);
		if (clean_lentity==null){
			return false;
		}
		
		entity_person per = persons_map.get(clean_lentity);
		if (per != null){
			if (toParse.relation.equals(properties.get_yago_tag_wiki_len())) {
				int len = 0;
				Pattern p = Pattern.compile("\"([^\"]*)\"");
				Matcher m = p.matcher(toParse.rentity);
				while (m.find()) {
					len = Integer.parseInt((m.group(1)));
				}
				if (len > properties.get_min_wiki_len()){
					lite_persons_map.put(clean_lentity,per);
					flag=true;
				}
				
			}
		}
		
		return flag;
		
	}
	

}
