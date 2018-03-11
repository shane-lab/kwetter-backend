package nl.shanelab.kwetter.services.exceptions.kweet;

import nl.shanelab.kwetter.services.exceptions.KweetException;

public class KweetNotFoundException extends KweetException {

    public KweetNotFoundException() {
        super("Kweet was not found");
    }

    public KweetNotFoundException(long id) {
        super(String.format("Kweet with id %d was not found", id));
    }
}
