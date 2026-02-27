package exceptions;

public class InvalidAccommodationDataException extends Exception{

    public InvalidAccommodationDataException() {
        super("Error: Invalid transport data provided.");
    }
    public InvalidAccommodationDataException(String message) {
        super(message);
    }
}
