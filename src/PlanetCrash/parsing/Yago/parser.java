package PlanetCrash.parsing.Yago;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.Map;

import PlanetCrash.parsing.entities.entity_country;
import PlanetCrash.parsing.entities.entity_person;

public class parser {
	
	Map<String, entity_country> countries_map = Collections.synchronizedMap(new HashMap<String, entity_country>());
	Map<String, entity_person> persons_map = Collections.synchronizedMap(new HashMap<String, entity_person>());
	Set<String> currency_set = Collections.synchronizedSet(new HashSet<String>());
	Set<String> language_set = new HashSet<String>();
	Set<String> cities_set = new HashSet<String>();
	Map<String,Set<String>> countries_cities_map = Collections.synchronizedMap(new HashMap<String,Set<String>>());
	public static void yagoParse(){
		/* transitive  */
	}
}
