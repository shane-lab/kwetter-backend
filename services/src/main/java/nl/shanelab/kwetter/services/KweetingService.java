package nl.shanelab.kwetter.services;

import nl.shanelab.kwetter.dal.domain.HashTag;
import nl.shanelab.kwetter.dal.domain.Kweet;
import nl.shanelab.kwetter.dal.domain.User;

import java.util.Collection;

public interface KweetingService {

    /**
     * Checks if a user is following another user
     *
     * @param a The user who is following
     * @param b The user who is being followed
     * @return boolean Returns true if User A is following User B
     */
    boolean isUserFollowedBy(User a, User b);

    /**
     * Checks if a user is following another user
     *
     * @param a The user who is following
     * @param b The user who is being followed
     * @return boolean Returns true if User A is following User B
     */
    boolean isUserFollowing(User a, User b);

    /**
     * Makes a user follow another user
     *
     * @param a The user to follow
     * @param b The user who is following
     */
    void followUser(User a, User b);

    /**
     * Makes a user undo the created following of another user
     *
     * @param a The user to un-follow
     * @param b The user who is un-following
     */
    void unFollowUser(User a, User b);

    /**
     * Creates a new Kweet from a message
     *
     * @param message The message of the Kweet
     * @param user The author of the Kweet
     * @return Kweet Returns a newly created Kweet
     */
    Kweet createKweet(String message, User user);

    /**
     * Persists changes to a Kweet
     *
     * @param kweet The Kweet to edit
     * @return Kweet Returns the additions made to the Kweet
     */
    Kweet editKweet(Kweet kweet);

    /**
     * Removes a Kweet
     *
     * @param kweet The Kweet to remove
     */
    void removeKweet(Kweet kweet);

    /**
     * Removes a Kweet by its discriminator value
     *
     * @param id The discriminator value of the Kweet to remove
     */
    void removeKweet(long id);

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
    Collection<Kweet> getNthLatestKweetsByUser(int nth, User user);

    /**
     * Get a collection of Kweets associated by the given user
     *
     * @param user The author associated with the Kweet
     * @return Collection<Kweet> Returns a collection of Kweets associcated with the given user name
     */
    Collection<Kweet> getKweetsByUser(User user);

    /**
     * Get a collection of Kweets associated by the given user name
     *
     * @param name The name of the author associated with the Kweet
     * @return Collection<Kweet> Returns a collection of Kweets associcated with the given user name
     */
    Collection<Kweet> getKweetsByUserName(String name);

    /**
     * Get a collection of Kweets associated by the given user identifier
     *
     * @param id The identifier of the author associated with the Kweet
     * @return Collection<Kweet> Returns a collection of Kweets associated with the gieven user identifier
     */
    Collection<Kweet> getKweetsByUserId(long id);

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
    Collection<Kweet> getKweetsWithMention(User user);

    /**
     * Get a collection of Kweets mentioning a username
     *
     * @param name The name of user mentioned in the message of the Kweet
     * @return Collection<Kweet> Returns a collection of Kweets mentioning the username in the message of the Kweet
     */
    Collection<Kweet> getKweetsWithMentionByUserName(String name);

    /**
     * Get a collection of Kweets mentioning a username
     *
     * @param id The identifier of user mentioned in the message of the Kweet
     * @return Collection<Kweet> Returns a collection of Kweets mentioning the username in the message of the Kweet
     */
    Collection<Kweet> getKweetsWithMentionByUserId(long id);

    /**
     * Get a collection of Kweets favorited by a user
     *
     * @param user The the user who has favourited the Kweet
     * @return Collection<Kweet> Returns a collection of Kweets favourited by a user
     */
    Collection<Kweet> getFavouritedKweets(User user);

    /**
     * Get a collection of Kweets favorited by a username
     *
     * @param name The name of the user who has favourited the Kweet
     * @return Collection<Kweet> Returns a collection of Kweets favourited by a user
     */
    Collection<Kweet> getFavouritedKweetsByUserName(String name);

    /**
     * Get a collection of Kweets favorited by a user id
     *
     * @param id The identifier of the user who has favourited the Kweet
     * @return Collection<Kweet> Returns a collection of Kweets favourited by a user
     */
    Collection<Kweet> getFavouritedKweetsByUserId(long id);

    /**
     * Checks if a user is mentioned in the given Kweet
     *
     * @param kweet The Kweet to check against
     * @param user The user to check with
     * @return Boolean Returns true if the user exists and was mentioned in the Kweet
     */
    boolean isUserMentionedInKweet(Kweet kweet, User user);

    /**
     * Checks if a user favourited the given Kweet
     *
     * @param kweet The Kweet to check against
     * @param user The user to check with
     * @return Boolean Returns true if the user exists and favourited the Kweet
     */
    boolean isKweetFavoritedByUser(Kweet kweet, User user);

    /**
     * Favourites a kweet
     *
     * @param kweet The Kweet to favourite
     * @param user The user who favourites the Kweet
     */
    void favouriteKweet(Kweet kweet, User user);

    /**
     * Un-favourites a Kweet
     *
     * @param kweet The Kweet to un-favourite
     * @param user The user who un-favourites the Kweet
     */
    void unFavouriteKweet(Kweet kweet, User user);

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
}
