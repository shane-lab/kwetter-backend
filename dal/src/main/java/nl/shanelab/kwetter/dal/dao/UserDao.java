package nl.shanelab.kwetter.dal.dao;

import nl.shanelab.kwetter.dal.domain.Kweet;
import nl.shanelab.kwetter.dal.domain.User;

import java.util.Collection;

public interface UserDao extends GenericDao<User, Long> {

    /**
     * Get the amount of followers
     *
     * @param id The user id to check
     * @return int Returns the amount of followers
     */
    int getAmountOfFollowers(long id);

    /**
     * Get the amount of followings
     *
     * @param id The user id to check
     * @return int Returns the amount of followings
     */
    int getAmountOfFollowings(long id);

    /**
     * Find user by its unique username
     *
     * @param username The username to search a user by
     * @return User Returns a User with the given username
     */
    User getByUsername(String username);

    /**
     * Find a list of users by partial username
     *
     * @param name The partia; username of the user to find
     * @return Collection<User> Returns a collection of users starting with the partial username
     */
    default Collection<User> getByPartialUsername(String name) {
        return getByPartialUsername(name, 0).getCollection();
    }

    default Pagination<User> getByPartialUsername(String name, int page) {
        return getByPartialUsername(name, page, defaultResults);
    }

    Pagination<User> getByPartialUsername(String name, int page, int size);

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
    boolean isFollowedBy(User a, User b);

    /**
     * Get a collection of a users latest Kweets
     *
     * @param nth The amount of Kweets to fetch
     * @param user The author the Kweets
     * @return Collection<Kweet> Returns a collection of @{nth} Kweets
     */
    Collection<Kweet> getNthLatestKweets(int nth, User user);

    default Collection<User> getFollowers(User user) {
        return getFollowers(user, 0).getCollection();
    }

    default Pagination<User> getFollowers(User user, int page) {
        return getFollowers(user, page, defaultResults);
    }

    Pagination<User> getFollowers(User user, int page, int size);

    default Collection<User> getFollowing(User user) {
        return getFollowing(user, 0).getCollection();
    }

    default Pagination<User> getFollowing(User user, int page) {
        return getFollowing(user, page, defaultResults);
    }

    Pagination<User> getFollowing(User user, int page, int size);

    /**
     * Sets a following from a user to another user
     *
     * @param a The user to follow
     * @param b The user who is following
     */
    void createFollow(User a, User b);

    /**
     * Unsets a following from a user to another user
     *
     * @param a The user to unfollow
     * @param b The user who is unfollwoing
     */
    void unFollow(User a, User b);

    /**
     * Get the most followed user
     *
     * @return User Returns the most followed user or null
     */
    User getMostFollowed();

    default boolean validateUserPair(User a, User b) {
        if (a == null || b == null) {
            return false;
        }
        if (a.equals(b)) {
            return false; // cant follow same person
        }
        User foundA = this.find(a.getId());
        User foundB = this.find(b.getId());
        if (!a.equals(foundA) || !b.equals(foundB)) {
            return false; // not in repository
        }

        return true;
    }

}
