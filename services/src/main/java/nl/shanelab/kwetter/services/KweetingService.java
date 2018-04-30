package nl.shanelab.kwetter.services;

import nl.shanelab.kwetter.dal.dao.Pagination;
import nl.shanelab.kwetter.dal.domain.HashTag;
import nl.shanelab.kwetter.dal.domain.Kweet;
import nl.shanelab.kwetter.dal.domain.User;
import nl.shanelab.kwetter.services.exceptions.KweetException;
import nl.shanelab.kwetter.services.exceptions.UserException;

import java.util.Collection;
import java.util.Date;

public interface KweetingService {

    /**
     * Get the amount of Kweets
     *
     * @return int Returns the amount of Kweets
     */
    int getAmountOfKweets();

    /**
     * Get the amount of Kweets from a specific user
     *
     * @param user The user to check
     * @return int Returns the amount of Kweets
     */
    int getAmountOfKweets(User user) throws UserException;

    /**
     * Get the amount of favourites from a specific Kweet
     *
     * @param kweet The Kweet to check
     * @return int Returns the amount of favourites
     */
    int getAmountOfFavourites(Kweet kweet) throws KweetException;

    /**
     * Get the amount of used hashtags in a specific Kweet
     *
     * @param kweet The Kweet to check
     * @return int Returns the amount of hashtags
     */
    int getAmountOfHashtags(Kweet kweet) throws KweetException;

    /**
     * Get the amount of mentions in a specific Kweet
     *
     * @param kweet The Kweet to check
     * @return int Returns the amount of mentions
     */
    int getAmountOfMentions(Kweet kweet) throws KweetException;

    /**
     * Creates a new Kweet from a message
     *
     * @param message The message of the Kweet
     * @param user The author of the Kweet
     * @return Kweet Returns a newly created Kweet
     */
    Kweet createKweet(String message, User user) throws UserException;

    /**
     * Persists changes to a Kweet
     *
     * @param kweet The Kweet to edit
     * @return Kweet Returns the additions made to the Kweet
     */
    Kweet editKweet(Kweet kweet) throws KweetException;

    /**
     * Removes a Kweet
     *
     * @param kweet The Kweet to remove
     */
    void removeKweet(Kweet kweet) throws KweetException;

    /**
     * Removes a Kweet by its discriminator value
     *
     * @param id The discriminator value of the Kweet to remove
     */
    void removeKweet(long id) throws KweetException;

    /**
     * Find a Kweet by its discriminator value
     *
     * @param id The discriminator value
     * @return Kweet Returns the Kweet by the given discriminator value
     */
    Kweet getKweetById(long id);

    /**
     * Get a collection of all Kweets
     *
     * @return Collection<Kweet> Returns a collection of every Kweet
     */

    default Collection<Kweet> getAllKweets() { return this.getAllKweets(0).getCollection(); }

    /**
     * Get a collection of all Kweets with pagination
     *
     * @param page The page index
     * @return Pagination<Kweet> Returns a paginated result set of Kweets
     */
    Pagination<Kweet> getAllKweets(int page);

    /**
     * Get a collection of Kweets with pagination
     *
     * @param page The page index
     * @param size The size of the result set
     * @return Pagination<Kweet> Returns a paginated result set of Kweets
     */
    Pagination<Kweet> getAllKweets(int page, int size);

    /**
     * Get a collection of a users latest Kweets
     *
     * @param nth The amount of Kweets to fetch
     * @param user The author of the Kweets
     * @return Collection<Kweet> Returns a collection of @{nth} Kweets
     */
    Collection<Kweet> getNthLatestKweetsByUser(int nth, User user) throws UserException;

    /**
     * Get a collection of Kweets associated by the given user
     *
     * @param user The author associated with the Kweet
     * @return Collection<Kweet> Returns a collection of Kweets associated with the given user name
     */
    default Collection<Kweet> getKweetsByUser(User user) throws UserException {
        return this.getKweetsByUser(user, 0).getCollection();
    }

    Pagination<Kweet> getKweetsByUser(User user, int page) throws UserException;

    Pagination<Kweet> getKweetsByUser(User user, int page, int size) throws UserException;

    /**
     * Get a collection of Kweets associated by the given user name
     *
     * @param name The name of the author associated with the Kweet
     * @return Collection<Kweet> Returns a collection of Kweets associated with the given user name
     */
    default Collection<Kweet> getKweetsByUserName(String name) throws UserException {
        return this.getKweetsByUserName(name, 0).getCollection();
    }

    /**
     * Get a collection of Kweets associated by the given user name with pagination
     *
     * @param name The name of the author associated with the Kweet
     * @param page The page index
     * @return Pagination<Kweet> Returns a paginated result set of Kweets associated with the given user name
     */
    Pagination<Kweet> getKweetsByUserName(String name, int page) throws UserException;

    /**
     * Get a collection of Kweets associated by the given user name with pagination
     *
     * @param name The name of the author associated with the Kweet
     * @param page The page index
     * @param size The maximum size of the result set
     * @return Pagination<Kweet> Returns a paginated result set of Kweets associated with the given user name
     */
    Pagination<Kweet> getKweetsByUserName(String name, int page, int size) throws UserException;

    /**
     * Get a collection of Kweets associated by the given user identifier
     *
     * @param id The identifier of the author associated with the Kweet
     * @return Collection<Kweet> Returns a collection of Kweets associated with the gieven user identifier
     */
    default Collection<Kweet> getKweetsByUserId(long id) throws UserException {
        return this.getKweetsByUserId(id, 0).getCollection();
    }

    Pagination<Kweet> getKweetsByUserId(long id, int page) throws UserException;

    Pagination<Kweet> getKweetsByUserId(long id, int page, int size) throws UserException;

    /**
     * Get a collection of Kweets with the given hashtag
     *
     * @param hashTag The hashtag to match
     * @return Collection<Kweet> Returns a collection of Kweets using the hashtag name in the message
     */
    default Collection<Kweet> getKweetsWithHashTag(HashTag hashTag) {
        return this.getKweetsWithHashTag(hashTag, 0).getCollection();
    }

    Pagination<Kweet> getKweetsWithHashTag(HashTag hashTag, int page);

    Pagination<Kweet> getKweetsWithHashTag(HashTag hashTag, int page, int size);

    /**
     * Get a collection of Kweets with the given hashtag name
     *
     * @param name The name of the hashtag
     * @return Collection<Kweet> Returns a collection of Kweets using the hashtag name in the message
     */
    default Collection<Kweet> getKweetsWithHashTagName(String name) {
        return getKweetsWithHashTagName(name, 0).getCollection();
    }

    Pagination<Kweet> getKweetsWithHashTagName(String name, int page);

    Pagination<Kweet> getKweetsWithHashTagName(String name, int page, int size);

    /**
     * Get a collection of Kweets with the given hashtag id
     *
     * @param id The identifier of the hashtag
     * @return Collection<Kweet> Returns a collection of Kweets associated with the identifier of the hashtag
     */
    default Collection<Kweet> getKweetsWithHashTagId(long id) {
        return getKweetsWithHashTagId(id, 0).getCollection();
    }

    Pagination<Kweet> getKweetsWithHashTagId(long id, int page);

    Pagination<Kweet> getKweetsWithHashTagId(long id, int page, int size);

    /**
     * Get a collection of Kweets mentioning a user
     *
     * @param user The user mentioned in the message of the Kweet
     * @return Collection<Kweet> Returns a collection of Kweets mentioning the username in the message of the Kweet
     */
    default Collection<Kweet> getKweetsWithMention(User user) throws UserException {
        return getKweetsWithMention(user, 0).getCollection();
    }

    Pagination<Kweet> getKweetsWithMention(User user, int page) throws UserException;

    Pagination<Kweet> getKweetsWithMention(User user, int page, int size) throws UserException;

    /**
     * Get a collection of Kweets mentioning a username
     *
     * @param name The name of user mentioned in the message of the Kweet
     * @return Collection<Kweet> Returns a collection of Kweets mentioning the username in the message of the Kweet
     */
    default Collection<Kweet> getKweetsWithMentionByUserName(String name) throws UserException {
        return getKweetsWithMentionByUserName(name, 0).getCollection();
    }

    Pagination<Kweet> getKweetsWithMentionByUserName(String name, int page) throws UserException;

    Pagination<Kweet> getKweetsWithMentionByUserName(String name, int page, int size) throws UserException;

    /**
     * Get a collection of Kweets mentioning a username
     *
     * @param id The identifier of user mentioned in the message of the Kweet
     * @return Collection<Kweet> Returns a collection of Kweets mentioning the username in the message of the Kweet
     */
    default Collection<Kweet> getKweetsWithMentionByUserId(long id) throws UserException {
        return getKweetsWithMentionByUserId(id, 0).getCollection();
    }

    Pagination<Kweet> getKweetsWithMentionByUserId(long id, int page) throws UserException;

    Pagination<Kweet> getKweetsWithMentionByUserId(long id, int page, int size) throws UserException;

    /**
     * Get a collection of Kweets favorited by a user
     *
     * @param user The the user who has favourited the Kweet
     * @return Collection<Kweet> Returns a collection of Kweets favourited by a user
     */
    default Collection<Kweet> getFavouritedKweets(User user) throws UserException {
        return getFavouritedKweets(user, 0).getCollection();
    }

    Pagination<Kweet> getFavouritedKweets(User user, int page) throws UserException;

    Pagination<Kweet> getFavouritedKweets(User user, int page, int size) throws UserException;

    /**
     * Get a collection of Kweets favorited by a username
     *
     * @param name The name of the user who has favourited the Kweet
     * @return Collection<Kweet> Returns a collection of Kweets favourited by a user
     */
    default Collection<Kweet> getFavouritedKweetsByUserName(String name) throws UserException {
        return getFavouritedKweetsByUserName(name, 0).getCollection();
    }

    Pagination<Kweet> getFavouritedKweetsByUserName(String name, int page) throws UserException;

    Pagination<Kweet> getFavouritedKweetsByUserName(String name, int page, int size) throws UserException;

    /**
     * Get a collection of Kweets favorited by a user id
     *
     * @param id The identifier of the user who has favourited the Kweet
     * @return Collection<Kweet> Returns a collection of Kweets favourited by a user
     */
    default Collection<Kweet> getFavouritedKweetsByUserId(long id) throws UserException {
        return this.getFavouritedKweetsByUserId(id, 0).getCollection();
    }

    Pagination<Kweet> getFavouritedKweetsByUserId(long id, int page) throws UserException;

    Pagination<Kweet> getFavouritedKweetsByUserId(long id, int page, int size) throws UserException;

    /**
     * Get a collection of Kweets posted by a user and mentioned in a Kweet
     *
     * @param id The identifier of the user
     * @return Collection<Kweet> Returns a collection of Kweets posted by a user and mentioned in a Kweet
     */
    default Collection<Kweet> getTimelineByUserId(long id) throws UserException {
        return this.getTimelineByUserId(id, 0).getCollection();
    }

    Pagination<Kweet> getTimelineByUserId(long id, int page) throws UserException;

    Pagination<Kweet> getTimelineByUserId(long id, int page, int size) throws UserException;

    /**
     * Checks if a user is mentioned in the given Kweet
     *
     * @param kweet The Kweet to check against
     * @param user The user to check with
     * @return Boolean Returns true if the user exists and was mentioned in the Kweet
     */
    boolean isUserMentionedInKweet(Kweet kweet, User user) throws KweetException, UserException;

    /**
     * Checks if a user favourited the given Kweet
     *
     * @param kweet The Kweet to check against
     * @param user The user to check with
     * @return Boolean Returns true if the user exists and favourited the Kweet
     */
    boolean isKweetFavoritedByUser(Kweet kweet, User user) throws KweetException, UserException;

    /**
     * Checks if a Kweet belongs to a user
     *
     * @param kweet The Kweet to check
     * @param user The user to check
     * @return Boolean Returns true if the Kweet belongs to the User
     * @throws KweetException Throws an KweetException if the Kweet was not found
     * @throws UserException Throws an UserException if the user is not found or not the owner of the Kweet
     */
    boolean isKweetOwnedByUser(Kweet kweet, User user) throws KweetException, UserException;

    /**
     * Favourites a kweet
     *
     * @param kweet The Kweet to favourite
     * @param user The user who favourites the Kweet
     */
    void favouriteKweet(Kweet kweet, User user) throws KweetException, UserException;

    /**
     * Un-favourites a Kweet
     *
     * @param kweet The Kweet to un-favourite
     * @param user The user who un-favourites the Kweet
     */
    void unFavouriteKweet(Kweet kweet, User user) throws KweetException, UserException;

    /**
     * Get the most favourited Kweet
     *
     * @return Kweet Returns the most favourited Kweet or null
     */
    Kweet getMostFavouritedKweet();

    /**
     * Find a hashtag by its discriminator value
     *
     * @param id The discriminator value to search a hashtag with
     * @return HashTag Returns the hashtag with the given name
     */
    HashTag getHashTagById(long id);

    /**
     * Find a hashtag by its uniuqe name
     *
     * @param name The name to search a hashtag for
     * @return HashTag Returns the hashtag with the given name
     */
    HashTag getHashTagByName(String name);

    /**
     * Find all created hashtags
     *
     * @return Collection<HashTag> Returns all hashtags
     */
    default Collection<HashTag> getAllHashTags() {
        return this.getAllHashTags(0).getCollection();
    }

    /**
     * Find all created hashtags with pagination
     *
     * @param page The page index
     * @return Pagination<HashTag> Returns a paginated result set of hashtags
     */
    Pagination<HashTag> getAllHashTags(int page);

    /**
     * Find all created hashtags with pagination
     *
     * @param page The page index
     * @param size The maximum size of the result set
     * @return Pagination<HashTag> Returns a paginated result set of hashtags
     */
    Pagination<HashTag> getAllHashTags(int page, int size);

    /**
     * Find all trending hashtags by date
     *
     * @param date The date in which the hashtag was trending
     * @return Collection<HashTag> Returns all trending hashtags of the given week
     */
    Collection<HashTag> getTrendingHashTags(Date date);
}
