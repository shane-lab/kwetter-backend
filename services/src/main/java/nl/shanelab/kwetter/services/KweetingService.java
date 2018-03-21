package nl.shanelab.kwetter.services;

import nl.shanelab.kwetter.dal.domain.HashTag;
import nl.shanelab.kwetter.dal.domain.Kweet;
import nl.shanelab.kwetter.dal.domain.User;
import nl.shanelab.kwetter.services.exceptions.KweetException;
import nl.shanelab.kwetter.services.exceptions.UserException;

import java.util.Collection;
import java.util.Date;

public interface KweetingService {

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

    Collection<Kweet> getAllKweets();

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
     * @return Collection<Kweet> Returns a collection of Kweets associcated with the given user name
     */
    Collection<Kweet> getKweetsByUser(User user) throws UserException;

    /**
     * Get a collection of Kweets associated by the given user name
     *
     * @param name The name of the author associated with the Kweet
     * @return Collection<Kweet> Returns a collection of Kweets associcated with the given user name
     */
    Collection<Kweet> getKweetsByUserName(String name) throws UserException;

    /**
     * Get a collection of Kweets associated by the given user identifier
     *
     * @param id The identifier of the author associated with the Kweet
     * @return Collection<Kweet> Returns a collection of Kweets associated with the gieven user identifier
     */
    Collection<Kweet> getKweetsByUserId(long id) throws UserException;

    /**
     * Get a collection of Kweets with the given hashtag
     *
     * @param hashTag The hashtag to match
     * @return Collection<Kweet> Returns a collection of Kweets using the hashtag name in the message
     */
    Collection<Kweet> getKweetsWithHashTag(HashTag hashTag);

    /**
     * Get a collection of Kweets with the given hashtag name
     *
     * @param name The name of the hashtag
     * @return Collection<Kweet> Returns a collection of Kweets using the hashtag name in the message
     */
    Collection<Kweet> getKweetsWithHashTagName(String name);

    /**
     * Get a collection of Kweets with the given hashtag id
     *
     * @param id The identifier of the hashtag
     * @return Collection<Kweet> Returns a collection of Kweets associated with the identifier of the hashtag
     */
    Collection<Kweet> getKweetsWithHashTagId(long id);

    /**
     * Get a collection of Kweets mentioning a user
     *
     * @param user The user mentioned in the message of the Kweet
     * @return Collection<Kweet> Returns a collection of Kweets mentioning the username in the message of the Kweet
     */
    Collection<Kweet> getKweetsWithMention(User user) throws UserException;

    /**
     * Get a collection of Kweets mentioning a username
     *
     * @param name The name of user mentioned in the message of the Kweet
     * @return Collection<Kweet> Returns a collection of Kweets mentioning the username in the message of the Kweet
     */
    Collection<Kweet> getKweetsWithMentionByUserName(String name) throws UserException;

    /**
     * Get a collection of Kweets mentioning a username
     *
     * @param id The identifier of user mentioned in the message of the Kweet
     * @return Collection<Kweet> Returns a collection of Kweets mentioning the username in the message of the Kweet
     */
    Collection<Kweet> getKweetsWithMentionByUserId(long id) throws UserException;

    /**
     * Get a collection of Kweets favorited by a user
     *
     * @param user The the user who has favourited the Kweet
     * @return Collection<Kweet> Returns a collection of Kweets favourited by a user
     */
    Collection<Kweet> getFavouritedKweets(User user) throws UserException;

    /**
     * Get a collection of Kweets favorited by a username
     *
     * @param name The name of the user who has favourited the Kweet
     * @return Collection<Kweet> Returns a collection of Kweets favourited by a user
     */
    Collection<Kweet> getFavouritedKweetsByUserName(String name) throws UserException;

    /**
     * Get a collection of Kweets favorited by a user id
     *
     * @param id The identifier of the user who has favourited the Kweet
     * @return Collection<Kweet> Returns a collection of Kweets favourited by a user
     */
    Collection<Kweet> getFavouritedKweetsByUserId(long id) throws UserException;

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
    Collection<HashTag> getAllHashTags();

    /**
     * Find all trending hashtags by date
     *
     * @param date The date in which the hashtag was trending
     * @return Collection<HashTag> Returns all trending hashtags of the given week
     */
    Collection<HashTag> getTrendingHashTags(Date date);
}
