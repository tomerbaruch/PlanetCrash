package PlanetCrash.core.Exceptions;

public class NotFoundException extends Exception {
	 //Parameterless Constructor
    public NotFoundException() {}

    //Constructor that accepts a message
    public NotFoundException(String message)
    {
       super(message);
    }
	
}
