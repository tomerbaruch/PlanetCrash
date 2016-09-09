package PlanetCrash.db.Yago.Uploaders;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import PlanetCrash.db.DatabaseHandler;

public abstract class AbstractUploader {
	public static final int BATCHSIZE=10000;
	
	protected DatabaseHandler dbh;
	
	protected AbstractUploader(DatabaseHandler dbh) {
		this.dbh=dbh;
	}
	
	public abstract void upload();
	
	protected void insertBatch(List<Object[]> batch, String table, String[] columns) {
		try {
			dbh.batchInsert(table, columns, batch);
			batch = new ArrayList<Object[]>();
		} catch (SQLException e) {
			System.out.println("BATCH FAILED.");
			e.printStackTrace();
			for(Object[] arr : batch) { //insert individually
				try { 
					dbh.singleInsert(table, columns, arr);
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			}
		}
		batch = new ArrayList<Object[]>();
	}
}
