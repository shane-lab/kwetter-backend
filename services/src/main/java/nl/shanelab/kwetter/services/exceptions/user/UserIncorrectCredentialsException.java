package nl.shanelab.kwetter.services.exceptions.user;

import nl.shanelab.kwetter.services.exceptions.UserException;

public class UserIncorrectCredentialsException extends UserException {

    public UserIncorrectCredentialsException() {
        super("Incorrect credentials for user");
    }

    public UserIncorrectCredentialsException(String name) {
        super(String.format("Incorrect credentials for user with username '%s'", name));
    }

    public UserIncorrectCredentialsException(long id) {
        super(String.format("Incorrect credentials for user with id %d", id));
    }
}
