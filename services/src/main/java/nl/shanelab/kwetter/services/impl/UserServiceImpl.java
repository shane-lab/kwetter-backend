package nl.shanelab.kwetter.services.impl;

import nl.shanelab.kwetter.dal.dao.UserDao;
import nl.shanelab.kwetter.dal.domain.Role;
import nl.shanelab.kwetter.dal.domain.User;
import nl.shanelab.kwetter.dal.qualifiers.InMemoryDao;
import nl.shanelab.kwetter.services.UserService;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.Collection;

@Stateless
public class UserServiceImpl implements UserService {
    
    @InMemoryDao
    @Inject
    UserDao userDao;

    public User register(String name, String password) {
        return this.register(name, password, Role.USER);
    }

    public User register(String name, String password, Role role) {
//        User user = this.getByUserName(name); throw (custom?) error when username already exist
        return userDao.create(new User(name, password, role));
    }

    public User signIn(String name, String password) {
        User user = this.getByUserName(name); // throw (custom?) error when no such user exists

        return user != null && user.getPassword().equals(password) ? user : null;
    }

    public User edit(User user) {
        return userDao.edit(user);
    }

    public void remove(User user) {
        userDao.remove(user);
    }

    public User getById(long id) {
        return userDao.find(id);
    }

    public User getByUserName(String name) {
        return userDao.getByUsername(name);
    }

    public Collection<User> getAllUsers() {
        return userDao.findAll();
    }
}
