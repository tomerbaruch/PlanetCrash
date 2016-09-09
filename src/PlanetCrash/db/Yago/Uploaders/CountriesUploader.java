package PlanetCrash.db.Yago.Uploaders;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import PlanetCrash.db.DatabaseHandler;
import PlanetCrash.parsing.entities.entity_country;

public class CountriesUploader extends AbstractUploader{
	Map<String, entity_country> cmap;
	HashMap<String, Integer> language_id_name_map;
	HashMap<String, Integer> currency_id_name_map;
	

	String table = "Country";
	String[] columns = {"Name","idContinent","idCurrency","idLanguage","PopulationSize"};

	/**
	 * Assumes all relevant data (cities, currencies etc.) is already in the database
	 * @param countries_map
	 */
	public CountriesUploader(Map<String, entity_country> countries_map, HashMap<String, Integer> language_id_name_map, HashMap<String, Integer> currency_id_name_map, DatabaseHandler dbh) {
		super(dbh);
		this.cmap=countries_map;
		this.currency_id_name_map = currency_id_name_map;
		this.language_id_name_map = language_id_name_map;
	}

	/**
	 * Upload all country entities to the database
	 */
	public void upload() {
		Collection<entity_country> countries = cmap.values();

		List<Object[]> batch = new ArrayList<Object[]>();
		for(entity_country country : countries) {

			//Get relevant ids
			ResultSet rs;
			Object[] values = new Object[columns.length];
			try {
				//Name
				values[0] = country.getYagoName();

				//idContinent
				rs=dbh.executeFormatQuery("Continent", new String[]{"idContinent"},new String[]{"Name"}, new Object[]{country.getContinent()});// "WHERE Name = \""+country.getContinent()+"\"");
				if(rs.first())
					values[1] = rs.getInt(1);

				values[2] = currency_id_name_map.get(country.getCurrency());
				values[3] = language_id_name_map.get(country.getLanguage());

				//PopulationSize
				values[4]=country.getPopulation_size();

				Importer.uploading_finished++;

			} catch (SQLException e) {
				// TODO Auto-generated catch block
				System.out.println("Error initializing country: "+country.getYagoName());
				e.printStackTrace();
				continue;
			}

			if(batch.size()>=BATCHSIZE) {
				insertBatch(batch, table, columns);
			}
			batch.add(values);
		}
		if(batch.size()>0) //empty what's left
			insertBatch(batch, table, columns);

	}
}
