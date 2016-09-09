package PlanetCrash.db.Yago.Uploaders;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import PlanetCrash.core.Game.GameUtils;
import PlanetCrash.core.config.Config;
import PlanetCrash.db.DatabaseHandler;
import PlanetCrash.parsing.entities.entity_country;

public class CapitalsUploader extends AbstractUploader{
	Map<String, entity_country> cmap;
	HashMap<String, Integer> country_id_name_map;
	HashMap<String, Integer> city_id_name_map;

	String table = "Country";
	String[] columns = {"Name","idContinent","idCurrency","idLanguage","idCapital","PopulationSize"};
	Config conf = new Config();
	/**
	 * Assumes all relevant data (cities, currencies etc.) is already in the database
	 * @param countries_map
	 */
	public CapitalsUploader(Map<String, entity_country> countries_map, HashMap<String, Integer> country_id_name_map, HashMap<String, Integer> city_id_name_map, DatabaseHandler dbh) {
		super(dbh);
		this.cmap=countries_map;
		this.country_id_name_map = country_id_name_map;
		this.city_id_name_map = city_id_name_map;

	}

	/**
	 * Upload all country entities to the database
	 */
	public void upload() {
		Collection<entity_country> countries = cmap.values();
		for(entity_country country : countries) {

			//Get relevant ids
			Integer idCity, idCountry;
			try {

				idCity = city_id_name_map.get(country.getCapital());
				idCountry = country_id_name_map.get(country.getYagoName());

				Importer.uploading_finished++;
				dbh.singleUpdate(conf.get_db_name()+".Country",new String[]{"idCapital"},new Integer[]{idCity}, new String[]{"idCountry"},new Integer[]{idCountry});

			} catch (SQLException e) {
				// TODO Auto-generated catch block
				System.out.println("Error initializing country: "+country.getYagoName());
				e.printStackTrace();
				continue;
			}

		}


	}
}
