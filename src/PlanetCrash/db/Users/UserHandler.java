package PlanetCrash.db.Users;
import java.sql.ResultSet;
import java.sql.SQLException;

import PlanetCrash.db.DatabaseHandler;


public class UserHandler {
	
	public static User validate_user(String username, String password, DatabaseHandler dbh) throws SQLException, UserException{
		ResultSet rs;
		rs=dbh.executeFormatQuery("Users", new String[]{"idUser"},new String[]{"Username", "Password"}, new Object[]{username,password});//"WHERE Username = \""+username+"\" AND Password = \""+password+"\"");
		if(rs.first()) {
			User user = new User(rs.getInt(1),username);
			return user;
		}
			
		throw new UserException("Incorrect credentials.");
	}
	
	public static User add_new_user(String username, String password, DatabaseHandler dbh) throws SQLException, UserException{
		ResultSet rs;

		rs=dbh.executeFormatQuery("Users", new String[]{"idUser"}, new String[]{"Username"},new Object[]{username});//"WHERE Username = \""+username+"\"");
		if(rs.first()){
			throw new UserException("User already exists!");
		}
		
		dbh.singleInsert("Users", new String[]{"Username", "password", "isManual"}, new Object[]{username,password,new Integer(1)});
		return validate_user(username, password, dbh);
	}
}
