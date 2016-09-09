package PlanetCrash.db;

import java.sql.*;
import java.util.Stack;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import PlanetCrash.core.Game.GameUtils;
import PlanetCrash.core.config.Config;


/*
 * Pools DB connections
 */
public class ConnectionPool {
	Config conf = new Config();
	
	private static String CONNPATH; //"jdbc:mysql://localhost:3306/dbexample"; //TODO: change this
	private static String USER; //TODO: change this
	private static String PASS; //TODO: change this

	private AtomicInteger POOLMAX = new AtomicInteger(5);
	private AtomicInteger POOLMIN = new AtomicInteger(3);

	private AtomicInteger count = new AtomicInteger(); //how many connections have been spawned overall

	private Stack<Connection> cStack;
	
	private AtomicBoolean destroyed = new AtomicBoolean(false); //was this pool assigned destruction?

	public ConnectionPool() {

	}
	
	public void init() throws DatabaseException {
		CONNPATH = String.format("jdbc:mysql://%s:%s/%s",conf.get_host_address(),conf.get_port(),conf.get_db_name());
		USER = conf.get_user_name();
		PASS = conf.get_password();
		this.cStack = new Stack<Connection>();
		this.count.set(0);

		//populate stack
		while(cStack.size()<POOLMIN.get())
				cStack.push(createConnection());
	}

	private synchronized Connection createConnection() throws DatabaseException {
		Connection newConn;

		// loading the driver
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			throw new DatabaseException("Unable to load the MySQL JDBC driver..\n"+e.getMessage());
		}
		//System.out.println("Driver loaded successfully");

		// creating the connection
		System.out.print("Trying to connect... ");
		try {
			newConn = DriverManager.getConnection(CONNPATH,USER,PASS);
		} catch (SQLException e) {
			throw new DatabaseException("Unable to connect - " + e.getMessage());
		}

		if(newConn!=null) {
			System.out.println("Connected!");
			count.incrementAndGet();
		}
		return newConn;
	}

	public synchronized Connection getConnection() throws DatabaseException {
		if(destroyed.get()) //if pool was destroyed, no new connections allowed!
			return null;
		if(cStack.size()==0 && count.get()<POOLMAX.get()) { //stack is empty but we can still spawn more
			cStack.push(createConnection());
			return cStack.pop();
		} else if (cStack.size()>0) { //we have ready connectons
			return cStack.pop();
		} else { //stack is empty and all pools full
			return null; //TODO: should we wait for a new one instead?
		}
	}

	public synchronized void disposeConnection(Connection connection) {
		if(count.get()<POOLMAX.get())
			cStack.push(connection);
		else
			try {
				connection.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}

	/*
	 * Destroys all current connections and makes sure all returned ones are destroyed
	 */
	public synchronized void destroyPool() {
		if(destroyed.get()) //was already issued destruction
			return;
		destroyed.set(true);
		POOLMAX.set(0);
		POOLMIN.set(0);

		while(cStack.size()>0) //empty stack
			try {
				destroyConnection(cStack.pop());
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}

	private synchronized void destroyConnection(Connection connection) throws SQLException {
		connection.close();
		count.decrementAndGet();
	}
	
	public synchronized boolean isDestroyed() {
		return destroyed.get();
	}
}
