package nl.shanelab.kwetter.dal.dao;

import nl.shanelab.kwetter.dal.domain.Kweet;
import nl.shanelab.kwetter.dal.domain.User;

import java.util.Collection;

public interface KweetDao extends GenericDao<Kweet, Long> {

    /**
     * Get the amount of Kweets from a specific user
     *
     * @param user The user to check
     * @return int Returns the amount of Kweets
     */
    int getAmountOfKweets(User user);

    /**
     * Get the amount of favourites of a Kweet
     *
     * @param id The id of the Kweet to fetch
     * @return int Returns the amount of favourites of the given Kweet
     */
    int getAmountOfFavourites(long id);

    /**
     * Get the amount of hashtags of a Kweet
     *
     * @param id The id of the Kweet to fetch
     * @return int Returns the amount of hashtags of the given Kweet
     */
    int getAmountOfHashTags(long id);

    /**
     * Get the amount of mentions of a Kweet
     *
     * @param id The id of the Kweet to fetch
     * @return int Returns the amount of mentions of the given Kweet
     */
    int getAmountOfMentions(long id);

    /**
     * Get a collection of Kweets associated by the given user name
     *
     * @param name The name of the author associated with the Kweet
     * @return Collection<Kweet> Returns a collection of Kweets associcated with the given user name
     */
    default Collection<Kweet> getByUserName(String name) {
        return this.getByUserName(name, 0).getCollection();
    }

    default Pagination<Kweet> getByUserName(String name, int page) {
        return this.getByUserName(name, page, defaultResults);
    }

    Pagination<Kweet> getByUserName(String name, int page, int size);

    /**
     * Get a collection of Kweets associated by the given user identifier
     *
     * @param id The identifier of the author associated with the Kweet
     * @return Collection<Kweet> Returns a collection of Kweets associated with the gieven user identifier
     */
    default Collection<Kweet> getByUserId(Long id) {
        return getByUserId(id, 0).getCollection();
    }

    default Pagination<Kweet> getByUserId(Long id, int page) {
        return getByUserId(id, page, defaultResults);
    }

    Pagination<Kweet> getByUserId(Long id, int page, int size);

    // shouldn't the below rather be in the service layer?
    // -> added to dao, because it is faster to filter the objects with direct db operations

    /**
     * Get a collection of Kweets with the given hashtag name
     *
     * @param name The name of the hashtag
     * @return Collection<Kweet> Returns a collection of Kweets using the hashtag name in the message
     */
    default Collection<Kweet> getByHashTagName(String name) {
        return getByHashTagName(name, 0).getCollection();
    }

    default Pagination<Kweet> getByHashTagName(String name, int page) {
        return getByHashTagName(name, defaultResults);
    }

    Pagination<Kweet> getByHashTagName(String name, int page, int size);

    /**
     * Get a collection of Kweets with the given hashtag id
     *
     * @param id The identifier of the hashtag
     * @return Collection<Kweet> Returns a collection of Kweets associated with the identifier of the hashtag
     */
    default Collection<Kweet> getByHashTagId(Long id) {
        return getByHashTagId(id, 0).getCollection();
    }

    default Pagination<Kweet> getByHashTagId(Long id, int page) {
        return getByUserId(id, page, defaultResults);
    }

    Pagination<Kweet> getByHashTagId(Long id, int page, int size);

    /**
     * Get a collection of Kweets mentioning a username
     *
     * @param name The name of user mentioned in the message of the Kweet
     * @return Collection<Kweet> Returns a collection of Kweets mentioning the username in the message of the Kweet
     */
    default Collection<Kweet> getByMention(String name) {
        return getByMention(name, 0).getCollection();
    }

    default Pagination<Kweet> getByMention(String name, int page) {
        return getByMention(name, page, defaultResults);
    }

    Pagination<Kweet> getByMention(String name, int page, int size);

    /**
     * Get a collection of Kweets favorited by a user
     *
     * @param name The name of the user who has favourited the Kweet
     * @return Collection<Kweet> Returns a collection of Kweets favourited by a user
     */
    default Collection<Kweet> getByFavoritedBy(String name) {
        return getByFavoritedBy(name, 0).getCollection();
    }

    default Pagination<Kweet> getByFavoritedBy(String name, int page) {
        return getByFavoritedBy(name, page, defaultResults);
    }

    Pagination<Kweet> getByFavoritedBy(String name, int page, int size);

    /**
     * Get a collection of Kweets associated by the given user and the followed user
     *
     * @param name The name of the user
     * @return Collection<Kweet> Returns a collection of Kweets associated by the given user and the followed user
     */
    default Collection<Kweet> getTimeline(String name) {
        return this.getTimeline(name, 0).getCollection();
    }

    default Pagination<Kweet> getTimeline(String name, int page) {
        return this.getTimeline(name, page, 0);
    }

    Pagination<Kweet> getTimeline(String name, int page, int size);

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

    /**
     * Get the most favourited Kweet
     *
     * @return Kweet Returns the most favourited Kweet
     */
    Kweet getMostFavourited();
}
