package exceptions;

public class InvalidTripDataException extends Exception {

    public InvalidTripDataException() {
        super("Error: Invalid trip data provided.");
    }

    public InvalidTripDataException(String message) {
        super(message);
    }

}
