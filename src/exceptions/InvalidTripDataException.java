package exceptions;

public class InvalidTripDataException extends Exception {

    public InvalidTripDataException() {
        super("Invalid trip data");
    }

    public InvalidTripDataException(String message) {
        super(message);
    }

}
