package PlanetCrash.db.Yago.Uploaders;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import PlanetCrash.db.DatabaseHandler;
import PlanetCrash.parsing.entities.entity_country;

public class CountriesCitiesUploader extends AbstractUploader{
	HashMap<String, Set<String>> ccmap;

	String table = "Country_City";
	String[] columns = {"idCountry","idCity"};

	/**
	 * Assumes all relevant data (cities, currencies etc.) is already in the database
	 * @param countries_map
	 */
	public CountriesCitiesUploader(HashMap<String, Set<String>> countries_cities_map, DatabaseHandler dbh) {
		super(dbh);
		this.ccmap=countries_cities_map;
	}

	/**
	 * Upload all country entities to the database
	 */
	public void upload() {
	    Iterator it = ccmap.entrySet().iterator();
	    List<Object[]> batch = new ArrayList<Object[]>();
	    while (it.hasNext()) {
	        Map.Entry pair = (Map.Entry)it.next();
	        
	        String country = (String)pair.getKey();
	        Set<String> cities = (Set<String>)pair.getValue();
	        
	        ResultSet rs;
	        Object[] values = new Object[columns.length];
	        
	        try{
				//idCountry
				rs=dbh.executeFormatQuery("Country", new String[]{"idCountry"}, new String[]{"Name"},new Object[]{country});// "WHERE Name = \""+country+"\"");
				if(rs.first())
					values[0] = rs.getInt(1);
	        }catch(SQLException e) {
				// TODO Auto-generated catch block
				System.out.println("Error initializing country: "+country);
				e.printStackTrace();
				continue;        	
	        }
	        
	        for (String city : cities){
				//Get relevant ids
				ResultSet rs2;
				
				try {
					//idCity
					rs2=dbh.executeFormatQuery("City", new String[]{"idCity"}, new String[]{"Name"}, new Object[]{city});//"WHERE Name =\""+city+"\"");
					if(rs2.first())
						values[1]=rs2.getInt(1);


				} catch (SQLException e) {
					// TODO Auto-generated catch block
					System.out.println("Error initializing city: "+city);
					e.printStackTrace();
					continue;
				}

				if(batch.size()>=BATCHSIZE) {
					insertBatch(batch, table, columns);
				}
				Importer.uploading_finished++;
				batch.add(values);
			}	        
	    }
		if(batch.size()>0) //empty what's left
			insertBatch(batch, table, columns);

	}
}
