package exceptions;

public class InvalidClientDataException extends Exception {

    public InvalidClientDataException() {
        super("Invalid client data");
    }

    public InvalidClientDataException(String message) {
        super(message);
    }
}
