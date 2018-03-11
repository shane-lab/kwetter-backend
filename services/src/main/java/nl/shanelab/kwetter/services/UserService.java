package nl.shanelab.kwetter.services;

import nl.shanelab.kwetter.dal.domain.Role;
import nl.shanelab.kwetter.dal.domain.User;

import java.util.Collection;

public interface UserService {

    User register(String name, String password);

    User register(String name, String password, Role role);

    User edit(User user);

    void remove(User user);

    User getById(long id);

    User getByUserName(String name);

    Collection<User> getAllUsers();
}
