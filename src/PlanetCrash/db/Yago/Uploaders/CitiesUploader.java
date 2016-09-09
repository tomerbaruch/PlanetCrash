package PlanetCrash.db.Yago.Uploaders;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import PlanetCrash.core.config.Config;
import PlanetCrash.db.DatabaseHandler;
import PlanetCrash.parsing.entities.entity_city;


public class CitiesUploader extends AbstractUploader{
	Map<String, entity_city> cmap;
	Config conf = new Config();
	String table = "City";
	String[] columns = {"Name","idCountry"};
	HashMap<String, Integer> country_id_name_map;

	/**
	 * Assumes all relevant data (cities, currencies etc.) is already in the database
	 * @param countries_map
	 */
	public CitiesUploader(Map<String, entity_city> cities_map, HashMap<String, Integer> country_id_name_map, DatabaseHandler dbh) {
		super(dbh);
		this.cmap=cities_map;
		this.country_id_name_map = country_id_name_map; 
	}

	/**
	 * Upload all country entities to the database
	 */
	public void upload() {
		int c=0;

		Collection<entity_city> cities = cmap.values();
		List<Object[]> batch = new ArrayList<Object[]>();
		for(entity_city city : cities) {

			//Get relevant ids
			ResultSet rs;
			Object[] values = new Object[columns.length];
			values[0] = city.getName();
			values[1] = country_id_name_map.get(city.getCountry());

			if(batch.size()>=BATCHSIZE) {
				insertBatch(batch, table, columns);
				c+=BATCHSIZE;
				batch = new ArrayList<Object[]>();
			}
			Importer.uploading_finished++;
			batch.add(values);
		}
		if(batch.size()>0) //empty what's left
			insertBatch(batch, table, columns);

	}
}
