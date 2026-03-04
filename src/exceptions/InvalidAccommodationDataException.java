package exceptions;

public class InvalidAccommodationDataException extends Exception{

    public InvalidAccommodationDataException() {
        super("Invalid transport data");
    }
    public InvalidAccommodationDataException(String message) {
        super(message);
    }
}
