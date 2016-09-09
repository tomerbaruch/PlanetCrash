package PlanetCrash.db.Yago.Uploaders;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import PlanetCrash.db.DatabaseHandler;


public class CurrenciesUploader extends AbstractUploader{
	HashSet<String> cset;

	String table = "Currency";
	String[] columns = {"Name"};

	/**
	 * Assumes all relevant data (cities, currencies etc.) is already in the database
	 * @param countries_map
	 */
	public CurrenciesUploader(HashSet<String> currency_set, DatabaseHandler dbh) {
		super(dbh);
		this.cset=currency_set;
	}

	/**
	 * Upload all country entities to the database
	 */
	public void upload() {
		int c=0;
		List<Object[]> batch = new ArrayList<Object[]>();
		for(String curr : cset) {

			//Get relevant ids

			Object[] values = new Object[columns.length];
			values[0] = curr;



			if(batch.size()>=BATCHSIZE) {
				insertBatch(batch, table, columns);
				c+=BATCHSIZE;
				//System.out.println("total "+c);
				batch = new ArrayList<Object[]>();
			}
			Importer.uploading_finished++;
			batch.add(values);
		}
		if(batch.size()>0) //empty what's left
			insertBatch(batch, table, columns);

	}
}
