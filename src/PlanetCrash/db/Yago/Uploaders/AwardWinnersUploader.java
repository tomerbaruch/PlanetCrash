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
import PlanetCrash.parsing.entities.entity_person;

public class AwardWinnersUploader extends AbstractUploader{
	HashMap<String, entity_person> pmap;
	HashMap<String, Integer> persons_id_name_map;

	String table = "AwardWinners";
	String[] columns = {"idAward","idPerson"};

	/**
	 * Assumes all relevant data (cities, currencies etc.) is already in the database
	 * @param countries_map
	 */
	public AwardWinnersUploader(HashMap<String, entity_person> persons_map, HashMap<String, Integer> persons_id_name_map, DatabaseHandler dbh) {
		super(dbh);
		this.pmap=persons_map;
		this.persons_id_name_map = persons_id_name_map;
	}

	/**
	 * Upload all country entities to the database
	 */
	public void upload() {
	    Iterator it = pmap.entrySet().iterator();
	    List<Object[]> batch = new ArrayList<Object[]>();
	    while (it.hasNext()) {
	        Map.Entry pair = (Map.Entry)it.next();
	        
	        String person = (String)pair.getKey();
	        entity_person person_details = (entity_person)pair.getValue();
	        
	        ResultSet rs;
	        Integer person_id=0;
	        	        
	        person_id = persons_id_name_map.get(person);
	        
	        if (person_id==null){
	        	continue;
	        }
	        
	        for (String award : person_details.getAwards_list()){
	        	
	        	Object[] values = new Object[columns.length];
	        	values[1] = person_id;
				//Get relevant ids
				ResultSet rs2;
				
				try {
					//idProfession
					rs2=dbh.executeFormatQuery("Award", new String[]{"idAward"},new String[]{"Name"}, new Object[]{award});// "WHERE Name =\""+award+"\"");
					if(rs2.first())
						values[0]=rs2.getInt(1);


				} catch (SQLException e) {
					// TODO Auto-generated catch block
					System.out.println("Error initializing award: "+award);
					e.printStackTrace();
					continue;
				}

				if(batch.size()>=BATCHSIZE) {
					insertBatch(batch, table, columns);
					batch = new ArrayList<Object[]>();
				}
				Importer.uploading_finished++;
				batch.add(values);
			}	        
	    }
		if(batch.size()>0) //empty what's left
			insertBatch(batch, table, columns);

	}
}
