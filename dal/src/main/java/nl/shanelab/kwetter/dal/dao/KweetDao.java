package nl.shanelab.kwetter.dal.dao;

import nl.shanelab.kwetter.dal.domain.Kweet;
import nl.shanelab.kwetter.dal.domain.User;

import java.util.Collection;

public interface KweetDao extends GenericDao<Kweet, Long> {

    /**
     * Get a collection of Kweets associated by the given user name
     *
     * @param name The name of the author associated with the Kweet
     * @return Collection<Kweet> Returns a collection of Kweets associcated with the given user name
     */
    Collection<Kweet> getByUserName(String name);

    /**
     * Get a collection of Kweets associated by the given user identifier
     *
     * @param id The identifier of the author associated with the Kweet
     * @return Collection<Kweet> Returns a collection of Kweets associated with the gieven user identifier
     */
    Collection<Kweet> getByUserId(Long id);

    // shouldn't the below rather be in the service layer?
    // -> added to dao, because it is faster to filter the objects with direct db operations

    /**
     * Get a collection of Kweets with the given hashtag name
     *
     * @param name The name of the hashtag
     * @return Collection<Kweet> Returns a collection of Kweets using the hashtag name in the message
     */
    Collection<Kweet> getByHashTagName(String name);

    /**
     * Get a collection of Kweets with the given hashtag id
     *
     * @param id The identifier of the hashtag
     * @return Collection<Kweet> Returns a collection of Kweets associated with the identifier of the hashtag
     */
    Collection<Kweet> getByHashTagId(Long id);

    /**
     * Get a collection of Kweets mentioning a username
     *
     * @param name The name of user mentioned in the message of the Kweet
     * @return Collection<Kweet> Returns a collection of Kweets mentioning the username in the message of the Kweet
     */
    Collection<Kweet> getByMention(String name);

    /**
     * Get a collection of Kweets favorited by a user
     *
     * @param name The name of the user who has favourited the Kweet
     * @return Collection<Kweet> Returns a collection of Kweets favourited by a user
     */
    Collection<Kweet> getByFavoritedBy(String name);

    /**
     * Checks if a user favourited the given Kweet
     *
     * @param kweet The Kweet to check against
     * @param user The user to check with
     * @return Boolean Returns true if the user exists and favourited the Kweet
     */
    boolean isFavoritedBy(Kweet kweet, User user);

    /**
     * Checks if a user is mentioned in the given Kweet
     *
     * @param kweet The Kweet to check against
     * @param user The user to check with
     * @return Boolean Returns true if the user exists and was mentioned in the Kweet
     */
    boolean isMentionedIn(Kweet kweet, User user);

    /**
     * Favourites a kweet
     *
     * @param kweet The Kweet to favourite
     * @param user The user who favourites the Kweet
     */
    void favourite(Kweet kweet, User user);

    /**
     * Un-favourites a Kweet
     *
     * @param kweet The Kweet to un-favourite
     * @param user The user who un-favourites the Kweet
     */
    void unFavourite(Kweet kweet, User user);
}
