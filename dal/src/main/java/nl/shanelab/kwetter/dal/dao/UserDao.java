package nl.shanelab.kwetter.dal.dao;

import nl.shanelab.kwetter.dal.domain.Kweet;
import nl.shanelab.kwetter.dal.domain.User;

import java.util.Collection;

public interface UserDao extends GenericDao<User, Long> {

    /**
     * Find user by its unique username
     *
     * @param username The username to search a user by
     * @return User Returns a User with the given username
     */
    User getByUsername(String username);

    /**
     * Checks if a user is following another user
     *
     * @param a The user who is following
     * @param b The user who is being followed
     * @return boolean Returns true if User A is following User B
     */
    boolean isFollowing(User a, User b);

    /**
     * Checks if a user is followed by another user
     *
     * @param a The user who is being followed
     * @param b The user who is following
     * @return boolean Returns true if User A is being followed by User B
     */
    boolean isFollower(User a, User b);

    /**
     * Get a collection of a users latest Kweets
     *
     * @param nth The amount of Kweets to fetch
     * @return Collection<Kweet> Returns a collection of @{nth} Kweets
     */
    Collection<Kweet> getNthLatestKweets(int nth);
}
