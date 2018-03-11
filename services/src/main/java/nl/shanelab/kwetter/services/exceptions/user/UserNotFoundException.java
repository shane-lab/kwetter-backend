package nl.shanelab.kwetter.services.exceptions.user;

import nl.shanelab.kwetter.services.exceptions.UserException;

public class UserNotFoundException extends UserException {

    public UserNotFoundException() {
        super("User not found");
    }

    public UserNotFoundException(String name) {
        super(String.format("User with username '%s' was not found", name));
    }

    public UserNotFoundException(long id) {
        super(String.format("User with id %d was not found", id));
    }
}
