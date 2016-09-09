package PlanetCrash.db.Yago.Uploaders;
import java.io.FileNotFoundException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import PlanetCrash.core.Game.GameUtils;
import PlanetCrash.core.config.Config;
import PlanetCrash.db.DatabaseHandler;
import PlanetCrash.parsing.Yago.parser_transitive_types;
import PlanetCrash.parsing.Yago.parser_yago_date_facts;
import PlanetCrash.parsing.Yago.parser_yago_facts;
import PlanetCrash.parsing.Yago.parser_yago_literal_facts;
import PlanetCrash.parsing.Yago.parser_yago_wikipedia_info;
import PlanetCrash.parsing.entities.entity_city;
import PlanetCrash.parsing.entities.entity_country;
import PlanetCrash.parsing.entities.entity_person;
import PlanetCrash.parsing.entities.entity_university;


public class Importer {
	
	
	public static boolean dbReady = false;
	
	public static final int BATCHSIZE=1000;
	//private static List<Object[]> batch;
	static Config conf = new Config();
	
	public static int uploading_finished = 0;
	public static int parsing_finished = 0;
	private static DatabaseHandler dbh;
	
	public Importer(DatabaseHandler dbh, Config properties) throws SQLException, FileNotFoundException{		
		
		HashMap<String, entity_country> countries_map = new HashMap<String, entity_country>();
		HashMap<String, entity_city> cities_map = new HashMap<String, entity_city>();
		HashMap<String, entity_person> persons_map = new HashMap<String, entity_person>();
		HashSet<String> currency_set = new HashSet<String>();
		HashSet<String> language_set = new HashSet<String>();
		HashMap<String,Set<String>> countries_cities_map = new HashMap<String,Set<String>>();
		HashMap<String, entity_university> universities_map = new HashMap<String,entity_university>();
		HashMap<String, entity_person> lite_persons_map = new HashMap<String, entity_person>();
		HashSet<String> awards_set = new HashSet<String>();
		Importer.dbh = dbh;
		long start = System.currentTimeMillis();
		insertManualData();
		



		ResultSet rs = null;
		try{
			rs = dbh.executeQuery(String.format("SELECT Name FROM %s.Award;",conf.get_db_name()));
			while (rs.next()) {	        
	            String Name = rs.getString("Name");
	            awards_set.add(Name);
	        }
			rs.close();
		}catch(SQLException e){
			System.out.println("Error");
			rs.close();
		}
		
		System.out.println("parsing transitive types " + (System.currentTimeMillis()-start)/1000f);
		parser_transitive_types c = new parser_transitive_types(properties.get_yago_files_directory()+properties.get_yago_transitive_types_name(), countries_map,
				persons_map, cities_map, currency_set, language_set, countries_cities_map, universities_map);		
		c.populate();
		System.out.println("done " + (System.currentTimeMillis()-start)/1000f);
		parsing_finished+= 6483;	    

		System.out.println("parsing facts " + (System.currentTimeMillis()-start)/1000f);
		parser_yago_facts d = new parser_yago_facts(properties.get_yago_files_directory()+properties.get_yago_facts_name(),  countries_map,  countries_cities_map, cities_map,universities_map,persons_map, lite_persons_map, awards_set);
		d.populate();
		System.out.println("done " + (System.currentTimeMillis()-start)/1000f);
		parsing_finished+= 415;	
		
		System.out.println("parsing wikipedia info " + (System.currentTimeMillis()-start)/1000f);
		parser_yago_wikipedia_info k = new parser_yago_wikipedia_info(properties.get_yago_files_directory()+properties.get_yago_wikipedia_info_name(),persons_map, lite_persons_map);
		k.populate();
		System.out.println("done " + (System.currentTimeMillis()-start)/1000f);
		parsing_finished+= 4085;	
		

		System.out.println("parsing literal facts " + (System.currentTimeMillis()-start)/1000f);
		parser_yago_literal_facts g = new parser_yago_literal_facts(properties.get_yago_files_directory()+properties.get_yago_literal_facts_name(), countries_map);
		g.populate();
		System.out.println("done " + (System.currentTimeMillis()-start)/1000f);
		parsing_finished+= 148;

		System.out.println("parsing date facts " + (System.currentTimeMillis()-start)/1000f);
		parser_yago_date_facts f = new parser_yago_date_facts(properties.get_yago_files_directory()+properties.get_yago_date_facts_name(), persons_map);
		f.populate();
		System.out.println("done " + (System.currentTimeMillis()-start)/1000f);
		parsing_finished+= 305;


		
		parsing_finished=-1;
				
		//CURRENCY
		System.out.println("inserting currency " + (System.currentTimeMillis()-start)/1000f);
		CurrenciesUploader currencyUploader = new CurrenciesUploader(currency_set, dbh);
		currencyUploader.upload();
		currency_set = new HashSet<String>();
		HashMap<String, Integer> currency_id_name_map = get_currency_id_name_map();
		


				
		//LANGUAGE
		System.out.println("inserting languags " + (System.currentTimeMillis()-start)/1000f);
		LanguagesUploader languageUploader = new LanguagesUploader(language_set, dbh);
		languageUploader.upload();
		language_set = new HashSet<String>();
		HashMap<String, Integer> language_id_name_map = get_language_id_name_map();
		
		//COUNTRIES
		System.out.println("inserting countries " + (System.currentTimeMillis()-start)/1000f);
		CountriesUploader countriesUploader = new CountriesUploader(countries_map, language_id_name_map, currency_id_name_map, dbh);
		countriesUploader.upload();
		currency_id_name_map = new HashMap<String, Integer>();
		language_id_name_map = new HashMap<String, Integer>();
		HashMap<String, Integer> country_id_name_map = get_country_id_name_map();
		
			
		//CITIES
		System.out.println("inserting cities " + (System.currentTimeMillis()-start)/1000f);
		CitiesUploader citiesUploader = new CitiesUploader(cities_map, country_id_name_map, dbh);
		citiesUploader.upload();
		cities_map = new HashMap<String, entity_city>();
		HashMap<String, Integer> city_id_name_map =  get_city_id_name_map();
	

		//CAPITALS
		System.out.println("inserting capitals " + (System.currentTimeMillis()-start)/1000f);
		CapitalsUploader capitalsUploader = new CapitalsUploader(countries_map, country_id_name_map, city_id_name_map, dbh);
		capitalsUploader.upload();
		country_id_name_map = new HashMap<String, Integer>();
		countries_map = new HashMap<String,entity_country>();
		
		
		//PERSONS
		System.out.println("inserting persons " + (System.currentTimeMillis()-start)/1000f);
		PersonsUploader pUploader = new PersonsUploader(lite_persons_map, city_id_name_map, dbh);
		pUploader.upload();
		HashMap<String, Integer> persons_id_name_map = get_persons_id_name_map();
		

		//PERSONS_PROFESSION
		System.out.println("inserting person_profession " + (System.currentTimeMillis()-start)/1000f);
		PersonProfessionUploader ppUploader = new PersonProfessionUploader(lite_persons_map, persons_id_name_map, dbh);
		ppUploader.upload();

		//AWARD_WINNERS
		System.out.println("inserting award winners " + (System.currentTimeMillis()-start)/1000f);
		AwardWinnersUploader awUploader = new AwardWinnersUploader(lite_persons_map, persons_id_name_map, dbh);
		awUploader.upload();

		//UNIVERSITIES
		System.out.println("inserting universities  " + (System.currentTimeMillis()-start)/1000f);
		UniversitiesUploader uniUploader = new UniversitiesUploader(universities_map, dbh);
		uniUploader.upload();		
		
		//update dbReady if it is really ready
		dbh.set_db_state();

		int i =0;
		if (i == 0){
			System.out.println("yes " + (System.currentTimeMillis()-start)/1000f);
		}
		
		uploading_finished=-1;
	}
	public static void insertManualData(){
		try {			
			dbh.singleInsert("Continent", new String[]{"Name"}, new String[]{"Europe"});
			dbh.singleInsert("Continent", new String[]{"Name"}, new String[]{"Asia"});
			dbh.singleInsert("Continent", new String[]{"Name"}, new String[]{"Africa"});
			dbh.singleInsert("Continent", new String[]{"Name"}, new String[]{"Australia"});
			dbh.singleInsert("Continent", new String[]{"Name"}, new String[]{"North America"});
			dbh.singleInsert("Continent", new String[]{"Name"}, new String[]{"Central America"});
			dbh.singleInsert("Continent", new String[]{"Name"}, new String[]{"South America"});
			dbh.singleInsert("Continent", new String[]{"Name"}, new String[]{"Oceania"});

			dbh.singleInsert("Profession", new String[]{"Name"}, new String[]{"Musician"});
			dbh.singleInsert("Profession", new String[]{"Name"}, new String[]{"Scientist"});
			dbh.singleInsert("Profession", new String[]{"Name"}, new String[]{"Politician"});
			dbh.singleInsert("Profession", new String[]{"Name"}, new String[]{"Athlete"});
			dbh.singleInsert("Profession", new String[]{"Name"}, new String[]{"Actor"});

			dbh.singleInsert("Award", new String[]{"Name"}, new String[]{"Grammy Award"});
			dbh.singleInsert("Award", new String[]{"Name"}, new String[]{"Grammy Lifetime Achievement Award"});
			dbh.singleInsert("Award", new String[]{"Name"}, new String[]{"Academy Award for Best Actor"});
			dbh.singleInsert("Award", new String[]{"Name"}, new String[]{"Academy Award for Best Actress"});
			dbh.singleInsert("Award", new String[]{"Name"}, new String[]{"Nobel Prize in Physics"});
			dbh.singleInsert("Award", new String[]{"Name"}, new String[]{"Nobel Prize in Chemistry"});
			dbh.singleInsert("Award", new String[]{"Name"}, new String[]{"Nobel Peace Prize"});
			dbh.singleInsert("Award", new String[]{"Name"}, new String[]{"FIFA World Player of the Year"});
			dbh.singleInsert("Award", new String[]{"Name"}, new String[]{"World Music Awards"});
			
		} catch (SQLException e) {

		}
	}
	public HashMap<String, Integer> get_currency_id_name_map(){
		ResultSet currency_rs;
		HashMap<String, Integer> currency_id_name_map = new HashMap<String, Integer>();
		try{
			currency_rs = dbh.executeQuery(String.format("SELECT idCurrency, Name FROM %s.Currency;",conf.get_db_name()));
			while (currency_rs.next()) {	        
	            int idCurrency = currency_rs.getInt("idCurrency");
	            String Name = currency_rs.getString("Name");
	            currency_id_name_map.put(Name, idCurrency);
	        }
		}catch(SQLException e){
			System.out.println("Error");
		}
		return currency_id_name_map;
	}
	public HashMap<String, Integer> get_language_id_name_map(){
		ResultSet language_rs;
		HashMap<String, Integer> language_id_name_map = new HashMap<String, Integer>();

		try{
			language_rs = dbh.executeQuery(String.format("SELECT idLanguage, Name FROM %s.Language;",conf.get_db_name()));
			while (language_rs.next()) {	        
				int idLanguage = language_rs.getInt("idLanguage");
				String Name = language_rs.getString("Name");
				language_id_name_map.put(Name, idLanguage);
			}
		}catch(SQLException e){
			System.out.println("Error");
		}
		return language_id_name_map;
	}
	
	public HashMap<String, Integer> get_country_id_name_map(){
		ResultSet country_rs;
		HashMap<String, Integer> country_id_name_map = new HashMap<String, Integer>();

		try{
			country_rs = dbh.executeQuery(String.format("SELECT idCountry, Name FROM %s.Country;",conf.get_db_name()));
			while (country_rs.next()) {	        
				int idCountry = country_rs.getInt("idCountry");
				String Name = country_rs.getString("Name");
				country_id_name_map.put(Name, idCountry);
			}
		}catch(SQLException e){
			System.out.println("Error");
		}
		return country_id_name_map;
	}
	
	public HashMap<String, Integer> get_city_id_name_map(){
		ResultSet city_rs;
		HashMap<String, Integer> city_id_name_map = new HashMap<String, Integer>();

		try{
			city_rs = dbh.executeQuery(String.format("SELECT idCity, Name FROM %s.City;",conf.get_db_name()));
			while (city_rs.next()) {	        
				int idCity = city_rs.getInt("idCity");
				String Name = city_rs.getString("Name");
				city_id_name_map.put(Name, idCity);
			}
		}catch(SQLException e){
			System.out.println("Error");
		}
		return city_id_name_map;
	}
	
	public HashMap<String, Integer> get_persons_id_name_map(){
		ResultSet persons_rs;
		HashMap<String, Integer> persons_id_name_map = new HashMap<String, Integer>();

		try{
			persons_rs = dbh.executeQuery(String.format("SELECT idPerson, Name FROM %s.Person;",conf.get_db_name()));
			while (persons_rs.next()) {	        
				int idPerson = persons_rs.getInt("idPerson");
				String Name = persons_rs.getString("Name");
				persons_id_name_map.put(Name, idPerson);
			}
		}catch(SQLException e){
			System.out.println("Error");
		}
		return persons_id_name_map;
	}
}
