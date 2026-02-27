package exceptions;

public class InvalidClientDataException extends Exception {

    public InvalidClientDataException() {
        super("Error: Invalid client data provided.");
    }

    public InvalidClientDataException(String message) {
        super(message);
    }
}
