package nl.shanelab.kwetter.services;

import nl.shanelab.kwetter.dal.domain.Role;
import nl.shanelab.kwetter.dal.domain.User;
import nl.shanelab.kwetter.services.exceptions.UserException;

import java.util.Collection;

public interface UserService {

    /**
     * Creates a new user with default role
     *
     * @param name The name of the user
     * @param password The password of the user
     * @return User Returns the created user
     * @throws UserException Throws an exception if a user with the given username already exists
     */
    User register(String name, String password) throws UserException;

    /**
     * Creates a new user
     *
     * @param name The name of the user
     * @param password The password of the user
     * @param role The role of the user
     * @return User Returns the created user
     * @throws UserException Throws an exception if a user with the given username already exists
     */
    User register(String name, String password, Role role) throws UserException;

    /**
     * Checks the given user credentials to sign the user in
     *
     * @param name The username of the user
     * @param password The associated password of the user
     * @return User Returns the user matching the given credentials
     * @throws UserException Throws an exception when no user was found with the given name or credentials mismatched
     */
    User authenticate(String name, String password) throws UserException;

    /**
     * Sets a new username for the user
     *
     * @param name The new username of the user
     * @param user The user to rename
     * @return User Returns the user with the new name
     * @throws UserException Throws an exception when no user was found with the given name or the desired name is already taken
     */
    User rename(String name, User user) throws UserException;

    /**
     * Sets a new biography for the user
     *
     * @param bio The biography
     * @param user The user to set the biography for
     * @return User Returns the edited user
     * @throws UserException Throws an exception when no user was found
     */
    User setBiography(String bio, User user) throws UserException;

    /**
     * Removes a user
     *
     * @param user The user to remove
     * @throws UserException Throws an exception when no user was found
     */
    void remove(User user) throws UserException;

    /**
     * Checks if a user is followed by another user
     *
     * @param a The user who is being followed
     * @param b The user who is following
     * @return boolean Returns true if User A is being followed by User B
     * @throws UserException Throws an exception when either user was not found or User A is not followed by User B
     */
    boolean isUserFollowedBy(User a, User b) throws UserException;

    /**
     * Checks if a user is following another user
     *
     * @param a The user who is following
     * @param b The user who is being followed
     * @return boolean Returns true if User A is following User B
     * @throws UserException Throws an exception when either user was not found or User A is not following User B
     */
    boolean isUserFollowing(User a, User b) throws UserException;

    /**
     * Makes a user follow another user
     *
     * @param a The user who is following
     * @param b The user to follow
     * @throws UserException Throws an exception when either user was not found, User A is already followed by User B or attempting to follow itself
     */
    void followUser(User a, User b) throws UserException;

    /**
     * Makes a user undo the created following of another user
     *
     * @param a The user to un-follow
     * @param b The user who is un-following
     * @throws UserException Throws an exception when either user was not found or User A is not followed by User B
     */
    void unFollowUser(User a, User b) throws UserException;

    /**
     * Find a user by its discriminator value
     *
     * @param id The discriminator value of the user
     * @return User Returns a user with the matching discriminator value
     */
    User getById(long id);

    /**
     * Find a user by its username
     *
     * @param name The username of the user to find
     * @return User Returns a user with the matching username
     */
    User getByUserName(String name);

    /**
     * Get all users
     *
     * @return Collection<User> Returns a collection of all users
     */
    Collection<User> getAllUsers();
}
