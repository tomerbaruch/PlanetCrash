package PlanetCrash.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import PlanetCrash.core.Game.GameUtils;
import PlanetCrash.core.config.Config;
import PlanetCrash.db.Yago.Uploaders.Importer;

public class DatabaseHandler {

	ConnectionPool cPool;
	Connection conn;

	
	private static final String USER = ""; //TODO: change this
	private static final String PASS = ""; //TODO: change this
	
	public static int db_ready = 0;
	
	Config prop = new Config();

	public DatabaseHandler(ConnectionPool connPool) {
		this.cPool=connPool;

		//Try and open connection
		try{
			//while connection pool is active and we can't get a connection, continue trying
			while((!cPool.isDestroyed())&&(this.conn=cPool.getConnection())==null) 
				Thread.sleep(1500);
		} catch (DatabaseException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * 
	 * @param query
	 * @return ResultSet of the query
	 * @throws SQLException
	 */
	public ResultSet executeQuery(String query) throws SQLException {
		Statement stmnt = conn.createStatement();
		ResultSet rs = stmnt.executeQuery(query);
		return rs;
	}

	
	public ResultSet executeFormatQuery(String table, String[] columns, String[] whereCols, Object[] whereValues) throws SQLException {
		if(whereCols!=null&&whereValues!=null&&whereCols.length!=whereValues.length) {
			System.out.println("Mismatching columns-values");
			return null;
		}
		
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT ");
		if(columns.length==0 || columns==null)
			sql.append("*");
		else {
			sql.append("(");
			for(int i=0;i<columns.length;i++)
				sql.append(columns[i]+(i<columns.length-1?",":""));
			sql.append(")");
		}
		sql.append("FROM "+table);
		
		if(whereCols!=null&&whereValues!=null) {
			sql.append(" WHERE");
			for(int i=0;i<whereCols.length;i++)
				sql.append(" " + whereCols[i]+"=?"+(i<whereCols.length-1?" AND":""));
		}
		sql.append(";");

		return genericFormatQuery(sql.toString(), whereValues);
	}

	/**
	 * 
	 * @param table name of table to insert into
	 * @param columns names of columns corresponding with the batch arrays
	 * @param batch arrays of values to insert into the columns
	 * @return array of ints indicating rows affected
	 * @throws SQLException 
	 */
	public int[] batchInsert(String table, String[] columns, List<Object[]> batch) throws SQLException {
		//Create SQL statement
		String sql = "INSERT INTO "+table+" (";
		for(int i=0;i<columns.length;i++) 
			sql+=columns[i]+(i<columns.length-1?",":"");
		sql+=") VALUES(";
		for(int i=0;i<columns.length;i++)
			sql+="?"+(i<columns.length-1?",":"");
		sql+=");";
		//set auto-commit to false
		conn.setAutoCommit(false);

		//Create prepared statement
		PreparedStatement pstmnt = conn.prepareStatement(sql);

		//set the variables
		for(Object[] b: batch) {
			if (b.length!=columns.length) {
				System.out.println("Wrong value array length!");
				continue;
			}
			for(int i=0;i<columns.length;i++) {
				if(b[i]==null)
					pstmnt.setNull(i+1, java.sql.Types.NULL);
				else if(b[i] instanceof String)
					pstmnt.setString(i+1, (String)b[i]);
				else if(b[i] instanceof Integer)
					pstmnt.setInt(i+1, (Integer)b[i]);
			}
			pstmnt.addBatch(); //add to batch
		}

		int[] count = pstmnt.executeBatch(); //TODO: REROLL TRANSACTION IF FAILED
		conn.commit();
		conn.setAutoCommit(true);
		return count;
	}

	public void singleUpdate(String table, String[] columns, Object[] values) throws SQLException {
		if(columns.length!=values.length) {
			System.out.println("Mismatching colums-values");
			return;
		}

		StringBuilder sql = new StringBuilder();
		sql.append("UPDATE "+table+" SET");
		for(int i=0;i<columns.length;i++)
			sql.append(" " + columns[i]+"=?"+(i<columns.length-1?",":""));
		sql.append(";");

		genericFormatUpdate(sql.toString(), values);
	}

	public void singleUpdate(String table, String[] columns, Object[] values, String[] whereCols, Object[] whereValues) throws SQLException {
		if((columns.length!=values.length) || (whereCols!=null&&whereValues!=null&&whereCols.length!=whereValues.length)) {
			System.out.println("Mismatching colums-values");
			return;
		}

		StringBuilder sql = new StringBuilder();
		sql.append("UPDATE "+table+" SET");
		for(int i=0;i<columns.length;i++)
			sql.append(" " + columns[i]+"=?"+(i<columns.length-1?",":""));
		if(whereCols!=null&&whereValues!=null) {
			sql.append(" WHERE");
			for(int i=0;i<whereCols.length;i++)
				sql.append(" " + whereCols[i]+"=?"+(i<whereCols.length-1?",":""));
		}
		sql.append(";");

		Object[] vals;
		if(whereValues==null || whereCols==null)
			vals = values;
		else {
			vals = new Object[values.length+whereValues.length];
			for(int i=0;i<values.length;i++)
				vals[i]=values[i];
			for(int i=0;i<whereValues.length;i++)
				vals[values.length+i]=whereValues[i];
		}

		genericFormatUpdate(sql.toString(), vals);

	}


	public void singleInsert(String table, String[] columns, Object[] values) throws SQLException {
		if((columns.length!=values.length)) {
			System.out.println("Mismatching colums-values");
			return;
		}

		StringBuilder sql = new StringBuilder();
		sql.append("INSERT INTO "+table+" (");
		for(int i=0;i<columns.length;i++)
			sql.append(columns[i]+(i<columns.length-1?",":""));
		sql.append(") VALUES(");
		for(int i=0;i<columns.length;i++) 
			sql.append("?"+(i<columns.length-1?",":""));
		sql.append(");");

		genericFormatUpdate(sql.toString(), values);
	}


	public int truncateTable(String table) throws SQLException {
		conn.createStatement().executeUpdate("SET FOREIGN_KEY_CHECKS = 0;");
		conn.createStatement().executeUpdate("truncate "+table+";");
		conn.createStatement().executeUpdate("SET FOREIGN_KEY_CHECKS = 1;");
		return 0;
	}

	private ResultSet genericFormatQuery(String command, Object[] values) throws SQLException {
		PreparedStatement ps = conn.prepareStatement(command);
		for (int i=0; i<values.length;i++)
			if(values[i]==null)
				ps.setNull(i+1, java.sql.Types.NULL);
			else if(values[i] instanceof String)
				ps.setString(i+1, (String)values[i]);
			else if(values[i] instanceof Integer)
				ps.setInt(i+1, (Integer)values[i]);
		ResultSet rs = ps.executeQuery();
		return rs;
	}
	
	private void genericFormatUpdate(String command, /*String table, String[] columns,*/ Object[] values) throws SQLException {
		PreparedStatement ps = conn.prepareStatement(command);
		for (int i=0; i<values.length;i++)
			if(values[i]==null)
				ps.setNull(i+1, java.sql.Types.NULL);
			else if(values[i] instanceof String)
				ps.setString(i+1, (String)values[i]);
			else if(values[i] instanceof Integer)
				ps.setInt(i+1, (Integer)values[i]);
		ps.executeUpdate();
		ps.close();
	}



	public void close() {
		this.cPool.disposeConnection(conn);
	}
	
	public void set_db_state(){
		ResultSet rs;
		int records_count = 0;
		try{
			rs = executeQuery(String.format("SELECT SUM(TABLE_ROWS)FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_SCHEMA = '%s';", prop.get_db_name()));
			if(rs.first()){
				records_count = rs.getInt(1);
			}
			if (records_count<150000){
				Importer.dbReady = false;
			}
			else{
				Importer.dbReady = true;
			}
			rs.close();
		}
		catch(Exception e){
			Importer.dbReady = false;
		}
	}
}
