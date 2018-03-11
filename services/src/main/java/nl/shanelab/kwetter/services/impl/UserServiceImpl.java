package nl.shanelab.kwetter.services.impl;

import lombok.NoArgsConstructor;
import nl.shanelab.kwetter.dal.dao.UserDao;
import nl.shanelab.kwetter.dal.domain.Role;
import nl.shanelab.kwetter.dal.domain.User;
import nl.shanelab.kwetter.dal.qualifiers.InMemoryDao;
import nl.shanelab.kwetter.services.UserService;
import nl.shanelab.kwetter.services.exceptions.UserAlreadyExistsException;
import nl.shanelab.kwetter.services.exceptions.UserException;
import nl.shanelab.kwetter.services.exceptions.UserIncorrectCredentialsException;
import nl.shanelab.kwetter.services.exceptions.UserNotFoundException;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.Collection;

@Stateless
@NoArgsConstructor
public class UserServiceImpl implements UserService {

    @Inject
    @InMemoryDao
    UserDao userDao;

    public User register(String name, String password) throws UserException {
        return this.register(name, password, Role.USER);
    }

    public User register(String name, String password, Role role) throws UserException {
        if (name == null || password == null) {
            throw new IllegalArgumentException();
        }
        if (this.getByUserName(name) != null) {
            throw new UserAlreadyExistsException(name);
        }

        return userDao.create(new User(name, password, role));
    }

    public User signIn(String name, String password) throws UserException {
        if (name == null || password == null) {
            throw new IllegalArgumentException();
        }

        User user = this.getByUserName(name);
        if (user == null) {
            throw new UserNotFoundException(name);
        }
        if (!user.getPassword().equals(password)) {
            throw new UserIncorrectCredentialsException(name);
        }

        return user;
    }

    public User rename(String name, User user) throws UserException {
        if (name == null || user == null) {
            throw new IllegalArgumentException();
        }
        if (this.getByUserName(name) != null) {
            throw new UserAlreadyExistsException(name);
        }

        user.setUsername(name);

        return userDao.edit(user);
    }

    public User setBiography(String bio, User user) throws UserException {
        if (bio == null || user == null) {
            throw new IllegalArgumentException();
        }
        if (this.getById(user.getId()) == null) {
            throw new UserNotFoundException(user.getId());
        }

        return userDao.edit(user);
    }

    public void remove(User user) throws UserException {
        if (user == null) {
            throw new IllegalArgumentException();
        }
        if (this.getById(user.getId()) == null) {
            throw new UserNotFoundException(user.getId());
        }

        userDao.remove(user);
    }

    public User getById(long id) {
        if (id < 0) {
            throw new IllegalArgumentException();
        }

        return userDao.find(id);
    }

    public User getByUserName(String name) {
        if (name == null) {
            throw new IllegalArgumentException();
        }

        return userDao.getByUsername(name);
    }

    public Collection<User> getAllUsers() {
        return userDao.findAll();
    }
}
