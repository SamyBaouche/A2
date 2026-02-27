package exceptions;

public class InvalidTransportDataException extends Exception {

    public InvalidTransportDataException() {
        super("Error: Invalid transport data provided.");
    }
    public InvalidTransportDataException(String message) {
        super(message);
    }
}
