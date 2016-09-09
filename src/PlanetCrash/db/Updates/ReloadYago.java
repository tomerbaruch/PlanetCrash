package PlanetCrash.db.Updates;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;

import PlanetCrash.core.config.Config;
import PlanetCrash.db.ConnectionPool;
import PlanetCrash.db.DatabaseHandler;
import PlanetCrash.db.Yago.Uploaders.Importer;
import PlanetCrash.parsing.entities.entity_city;
import PlanetCrash.parsing.entities.entity_country;
import PlanetCrash.parsing.entities.entity_person;


public class ReloadYago {
	private static HashMap<String, entity_country> countries_map_bck = new HashMap<String, entity_country>();
	private static HashMap<String, entity_city> cities_map_bck = new HashMap<String, entity_city>();
	private static HashMap<String, entity_person> persons_map_bck = new HashMap<String, entity_person>();
	private static HashMap<Integer, HashMap<String, Integer>> scores_bck = new HashMap<Integer, HashMap<String, Integer>>();
	private static HashSet<String> currency_set_bck = new HashSet<String>();
	private static HashSet<String> language_set_bck = new HashSet<String>();
	private static HashSet<String> continent_set_bck = new HashSet<String>();
	private static DatabaseHandler dbh;
	
	public static void updateFromYago(ConnectionPool pool,Config conf) throws FileNotFoundException{
		dbh =new DatabaseHandler(pool);
		backupManualUpdates(conf.get_db_name());
		deleteAllData(conf.get_db_name(), dbh);
		try {
			Importer i = new Importer(dbh,conf);
		} catch (SQLException e) {
			
			e.printStackTrace();
		}
		insertManualBackupData(conf.get_db_name());
		dbh.close();

	}
	private static void insertManualBackupData(String dbname){
		//insert manual updates about currency
		for (String currency:currency_set_bck){
			try {
				dbh.singleInsert(dbname+".Currency",
						new String[]{"Name","isManual"},
						new Object[]{currency,1});
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		//insert manual updates about languages
		for (String language:language_set_bck){
			try {
				dbh.singleInsert(dbname+".Language",
						new String[]{"Name","isManual"},
						new Object[]{language,1});
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		//insert manual updates about continents
		for (String continent:continent_set_bck){
			try {
				dbh.singleInsert(dbname+".Continent",
						new String[]{"Name","isManual"},
						new Object[]{continent,1});
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		//insert manual updates about cities(just name)
		for(String cityName:cities_map_bck.keySet()){
			try {
				dbh.singleInsert(dbname+".City",
						new String[]{"Name","isManual"},
						new Object[]{cityName,1});
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		//insert manual updates about persons
		for(String personName:persons_map_bck.keySet()){
			entity_person person = persons_map_bck.get(personName);
			String existsQuery = String.format("SELECT * FROM %s.Person WHERE Name=\"%s\";", dbname,personName);
			ResultSet rs;
			try {
				rs = dbh.executeQuery(existsQuery);
				if(rs.first()){
					rs.close();
					dbh.singleUpdate(dbname+".Person", 
							new String[]{"idPlaceOfBirth","yearOfBirth","yearOfDeath","isManual"},
							new Object[]{getIdFromDB("City","idCity",person.getPlaceOfBirth()),
							person.getYearOfBirth(), person.getYearOfDeath(),1},
							new String[]{"Person.Name"}, 
							new Object[]{personName});
				}
				else{
					dbh.singleInsert(dbname+".Person",
							new String[]{"Name", "yearOfBirth", "yearOfDeath", "idPlaceOfBirth", "isManual"},
							new Object[]{personName,person.getYearOfBirth(),person.getYearOfDeath(),
									getIdFromDB("City","idCity",person.getPlaceOfBirth()),1});
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		//insert manual updates about countries
		for (String countryName:countries_map_bck.keySet()){
			entity_country country = countries_map_bck.get(countryName);
			String existsQuery = String.format("SELECT * FROM %s.Person WHERE Name=\"%s\";", dbname,countryName);
			ResultSet rs;
			try {
				rs = dbh.executeQuery(existsQuery);
				if(rs.first()){
					dbh.singleUpdate(dbname+".Country",
							new String[]{"idContinent","idCurrency","idLanguage","idCapital","PopulationSize","isManual"},
							new Object[]{getIdFromDB("Continent","idContinent",country.getContinent()),
							getIdFromDB("Currency","idCurrency",country.getCurrency()),
							getIdFromDB("Language","idLanguage",country.getLanguage()),
							getIdFromDB("City","idCity",country.getCapital()),
							country.getPopulation_size(),
							1},
							new String[]{"Name"},
							new Object[]{countryName});
				}
				else{
					dbh.singleInsert(dbname+".Country", 
							new String[]{"Name", "idContinent", "idCurrency", "idLanguage", "idCapital", "PopulationSize", "isManual"}, 
							new Object[]{countryName,getIdFromDB("Continent","idContinent",country.getContinent())
							,getIdFromDB("Currency","idCurrency",country.getCurrency())
							,getIdFromDB("Language","idLanguage",country.getLanguage()),
							getIdFromDB("City","idCity",country.getCapital()),
							country.getPopulation_size(), 1});
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		//insert manual updates about cities(just idCountry)
		for(String cityName:cities_map_bck.keySet()){
			entity_city city = cities_map_bck.get(cityName);
			
			try {
				dbh.singleUpdate(dbname+".City",
						new String[]{"idCountry"},
						new Object[]{getIdFromDB("Country","idCountry",city.getCountry())},
						new String[]{"Name"},
						new Object[]{cityName});
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		//insert scores
		for(Integer idUser:scores_bck.keySet()){
			for (String countryName:scores_bck.get(idUser).keySet()){
				try {
					dbh.singleInsert(dbname+".user_country_completed",
							new String[]{"idUser","idCountry","Level"},
							new Object[]{idUser,getIdFromDB("Country","idCountry",countryName),scores_bck.get(idUser).get(countryName)});
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
	}
	private static int getIdFromDB(String tableName, String column, String valueToSearch) {
		ResultSet rs;
		int retId = 0;
		try {
			rs=dbh.executeFormatQuery(tableName, new String[]{column}, new String[]{"Name"}, new Object[]{valueToSearch});//"WHERE Name = \""+valueToSearch+"\"");
			if(rs.first()){
				retId = rs.getInt(1);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return retId;
		
	}
	private static String getNameFromDB(String tableName, String column, int valueToSearch){
		ResultSet rs;
		String retString = "";
		try {
			rs=dbh.executeFormatQuery(tableName, new String[]{column}, new String[]{"id"+tableName}, new Object[]{valueToSearch});//"WHERE id"+tableName+" ='"+valueToSearch+"'");
			if(rs.first()){
				retString = rs.getString(1);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return retString;
		
	}
	private static void backupManualUpdates(String dbname){
		/*backup person manual updates*/
		String personQuery = "SELECT * FROM "+dbname+".Person WHERE Person.isManual=1";
		try {
			ResultSet rs = dbh.executeQuery(personQuery);
			while(rs.next()){
				entity_person person = new entity_person();
				int idPerson = rs.getInt("idPerson");
				person.setYearOfBirth(rs.getInt("yearOfBirth"));
				person.setYearOfDeath(rs.getInt("yearOfDeath"));
				String name = rs.getString("Name");
				person.setName(name);
				person.setPlaceOfBirth(getNameFromDB("City", "Name", rs.getInt("idPlaceOfBirth")));
				String professionQuery = "SELECT Profession.Name "+
										"FROM "+dbname+".Person_Profession, "+dbname+".Profession"+
										" WHERE Profession.idProfession = Person_Profession.idProfession and"+
										" Person_Profession.idProfession ='"+idPerson+"';";
				
				ResultSet prof = dbh.executeQuery(professionQuery);
				while(prof.next()){
					person.addProfession(prof.getString("Name"));
				}
				persons_map_bck.put(name, person);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		/*backup countries manual updates*/
		String countryQuery = "SELECT * FROM "+dbname+".Country WHERE Country.isManual=1";
		try {
			ResultSet rs = dbh.executeQuery(countryQuery);
			while(rs.next()){
				entity_country country = new entity_country();
				String countryName = rs.getString("Name");
				country.setYagoName(countryName);
				country.setPopulation_size(rs.getInt("PopulationSize"));
				country.setCapital(getNameFromDB("City", "Name", rs.getInt("idCapital")));
				country.setContinent(getNameFromDB("Continent", "Name", rs.getInt("idContinent")));
				country.setCurrency(getNameFromDB("Currency", "Name", rs.getInt("idCurrency")));
				country.setLanguage(getNameFromDB("Language", "Name", rs.getInt("idLanguage")));
				countries_map_bck.put(countryName, country);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		/*backup cities manual updates*/
		String cityQuery = "SELECT * FROM "+dbname+".City WHERE City.isManual=1";
		try {
			ResultSet rs = dbh.executeQuery(cityQuery);
			while(rs.next()){				
				String cityName = rs.getString("Name");
				entity_city city = new entity_city(cityName);
				city.setCountry(getNameFromDB("City", "Name", rs.getInt("idCity")));
				cities_map_bck.put(cityName, city);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		/*backup continents manual updates*/
		String continentQuery = "SELECT * FROM "+dbname+".Continent WHERE Continent.isManual=1";
		try {
			ResultSet rs = dbh.executeQuery(cityQuery);
			while(rs.next()){				
				String continentName = rs.getString("Name");
				continent_set_bck.add(continentName);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		/*backup languages manual updates*/
		String languageQuery = "SELECT * FROM "+dbname+".Language WHERE Language.isManual=1";
		try {
			ResultSet rs = dbh.executeQuery(languageQuery);
			while(rs.next()){				
				String languageName = rs.getString("Name");
				language_set_bck.add(languageName);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		/*backup currency manual updates*/
		String currencyQuery = "SELECT * FROM "+dbname+".Currency WHERE Currency.isManual=1";
		try {
			ResultSet rs = dbh.executeQuery(currencyQuery);
			while(rs.next()){				
				String currencyName = rs.getString("Name");
				currency_set_bck.add(currencyName);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		/*backup scores*/
		String scoreQuery = "SELECT * FROM "+dbname+".user_country_completed";
		try {
			ResultSet rs = dbh.executeQuery(scoreQuery);
			while(rs.next()){
				Integer userId = rs.getInt("idUser");
				Integer countryId = rs.getInt("idCountry");
				Integer level = rs.getInt("Level");
				HashMap<String, Integer> levelCountry = new HashMap<String, Integer>();
				levelCountry.put(getNameFromDB("Country", "Name", countryId),level);
				scores_bck.put(userId, levelCountry);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static void deleteAllData(String dbname, DatabaseHandler dbh){
		
		String tablesQuery = "SHOW TABLES FROM "+dbname+";";
		try {
			ResultSet rs = dbh.executeQuery(tablesQuery);
			while(rs.next()){
				String tableName = rs.getString(1);
				if(tableName.equals("users")){
					continue;
				}
				dbh.truncateTable(dbname+"."+tableName);
				
			}
			rs.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void deleteLiteFiles(Config conf) {
		File dir = new File(conf.get_yago_files_directory());
		if(dir==null ||!dir.exists() || !dir.isDirectory()) {
			System.out.println("Bad yago directory: "+conf.get_yago_files_directory());
			return;
		}
		
		File[] files = dir.listFiles();
		for(int i=0;i<files.length;i++)
			if(files[i].getName().endsWith(".lite"))
				files[i].delete();
		
	}
}
