package PlanetCrash.db.Yago.Uploaders;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import PlanetCrash.core.Game.GameUtils;
import PlanetCrash.core.config.Config;
import PlanetCrash.db.DatabaseHandler;
import PlanetCrash.parsing.entities.entity_university;

public class UniversitiesUploader extends AbstractUploader{
	Map<String, entity_university> umap;

	String table = "University";
	String[] columns = {"Name","idCountry"};
	Config conf = new Config();
	/**
	 * Assumes all relevant data (cities, currencies etc.) is already in the database
	 * @param countries_map
	 */
	public UniversitiesUploader(Map<String, entity_university> universities_map, DatabaseHandler dbh) {
		super(dbh);
		this.umap=universities_map;
	}

	/**
	 * Upload all country entities to the database
	 */
	public void upload() {
		int c=0;
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

		Collection<entity_university> universities = umap.values();
		List<Object[]> batch = new ArrayList<Object[]>();
		for(entity_university university : universities) {

			ResultSet rs;
			Object[] values = new Object[columns.length];
			values[0] = university.getName();
			values[1] = country_id_name_map.get(university.getCountry());
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
