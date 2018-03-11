package nl.shanelab.kwetter.services;

import nl.shanelab.kwetter.dal.domain.Role;
import nl.shanelab.kwetter.dal.domain.User;

import java.util.Collection;

public interface UserService {

    /**
     * Creates a new user with default role
     *
     * @param name The name of the user
     * @param password The password of the user
     * @return User Returns the created user
     */
    User register(String name, String password);

    /**
     * Creates a new user
     *
     * @param name The name of the user
     * @param password The password of the user
     * @param role The role of the user
     * @return User Returns the created user
     */
    User register(String name, String password, Role role);

    /**
     * Checks the given user credentials to sign the user in
     *
     * @param name The username of the user
     * @param password The associated password of the user
     * @return User Returns the user matching the given credentials
     */
    User signIn(String name, String password);

    /**
     * Persists changes to the user
     *
     * @param user The user to edit
     * @return User Returns the edited user
     */
    User edit(User user);

    /**
     * Removes a user
     *
     * @param user The user to remove
     */
    void remove(User user);

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
