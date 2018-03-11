package nl.shanelab.kwetter.services.exceptions.user;

import nl.shanelab.kwetter.services.exceptions.UserException;

public class UserAlreadyExistsException extends UserException {

    public UserAlreadyExistsException() {
        super("User already exists");
    }

    public UserAlreadyExistsException(String name) {
        super(String.format("User with username '%s' already exists", name));
    }

    public UserAlreadyExistsException(long id) {
        super(String.format("User with id %d already exists", id));
    }
}
