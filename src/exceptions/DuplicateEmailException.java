package exceptions;

public class DuplicateEmailException extends Exception {
    public DuplicateEmailException() {
        super("Duplicate email address");
    }
    public DuplicateEmailException(String message) {
        super(message);
    }
}
