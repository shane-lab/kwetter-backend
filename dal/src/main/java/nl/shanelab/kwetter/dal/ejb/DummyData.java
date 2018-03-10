package nl.shanelab.kwetter.dal.ejb;

import lombok.Getter;
import nl.shanelab.kwetter.dal.domain.HashTag;
import nl.shanelab.kwetter.dal.domain.Kweet;
import nl.shanelab.kwetter.dal.domain.User;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import java.util.HashMap;
import java.util.Map;

@Singleton
@Getter
/**
 * An injectable wrapper class (singleton instance) for collections of initial test data, used to mock and test the expected behaviours of the to-be-validated entities
 */
public class DummyData {

    /**
     * A collection of User entities, mapped by their serializable identifier
     */
    private final Map<Long, User> users = new HashMap<>();

    /**
     * A collection of Kweet entities, mapped by their serializable identifier
     */
    private final Map<Long, Kweet> kweets = new HashMap<>();

    /**
     * A collection of HashTag entities, mapped by their serializable identifier
     */
    private final Map<Long, HashTag> hashTags = new HashMap<>();

    @PostConstruct
    /**
     * Initializes the local collections when invoked/injected
     */
    private void onPostConstruct()
    {
        // TODO mock entity data
    }
}