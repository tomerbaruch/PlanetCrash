package PlanetCrash.db.Users;

public class UserException extends Exception{
	String msg;
	
	public UserException(String msg) {
		this.msg=msg;
	}
	
	public String getMessage() {
		return msg;
	}
}
