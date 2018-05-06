package nl.shanelab.kwetter.services.exceptions.kweet;

import nl.shanelab.kwetter.services.exceptions.KweetException;

public class KweetNotOwnedException extends KweetException {

    public KweetNotOwnedException() {
        super("The Kweet does not belong to the user");
    }

    public KweetNotOwnedException(long kweetId) {
        super(String.format("The Kweet with id %d does not belong to the user", kweetId));
    }

    public KweetNotOwnedException(long kweetId, long userId) {
        super(String.format("The Kweet with id %d does not belong to the user with id %d", kweetId, userId));
    }
}
