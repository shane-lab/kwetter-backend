package nl.shanelab.kwetter.services.exceptions;

public abstract class KweetException extends Exception {

    public KweetException(String message) {
        super(message);
    }
}
