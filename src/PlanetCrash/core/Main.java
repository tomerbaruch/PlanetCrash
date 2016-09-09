package PlanetCrash.core;

import PlanetCrash.db.ConnectionPool;
import PlanetCrash.db.DatabaseHandler;
import PlanetCrash.ui.GameGUI;



public class Main {

	public static ConnectionPool mConnPool;


	
	public static void main(String args[]) {
		mConnPool = new ConnectionPool();
		
		//Game GUI
		GameGUI gg = new GameGUI(mConnPool);
		gg.start();

	
	}


}
