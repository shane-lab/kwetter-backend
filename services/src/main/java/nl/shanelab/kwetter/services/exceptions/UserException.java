package nl.shanelab.kwetter.services.exceptions;

public abstract class UserException extends Exception {

    public UserException(String message) {
        super(message);
    }
}
