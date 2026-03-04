package exceptions;

public class InvalidTransportDataException extends Exception {

    public InvalidTransportDataException() {
        super("Invalid transport data");
    }
    public InvalidTransportDataException(String message) {
        super(message);
    }
}
