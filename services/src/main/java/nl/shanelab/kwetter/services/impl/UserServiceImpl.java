package nl.shanelab.kwetter.services.impl;

import lombok.NoArgsConstructor;
import nl.shanelab.kwetter.dal.dao.Pagination;
import nl.shanelab.kwetter.dal.dao.UserDao;
import nl.shanelab.kwetter.dal.domain.Role;
import nl.shanelab.kwetter.dal.domain.User;
import nl.shanelab.kwetter.dal.qualifiers.JPADao;
import nl.shanelab.kwetter.services.UserService;
import nl.shanelab.kwetter.services.exceptions.UserException;
import nl.shanelab.kwetter.services.exceptions.user.UserAlreadyExistsException;
import nl.shanelab.kwetter.services.exceptions.user.UserFollowException;
import nl.shanelab.kwetter.services.exceptions.user.UserIncorrectCredentialsException;
import nl.shanelab.kwetter.services.exceptions.user.UserNotFoundException;
import nl.shanelab.kwetter.util.Sha256;

import javax.ejb.Stateless;
import javax.inject.Inject;

import static nl.shanelab.kwetter.services.util.UserFollowHelper.with;

@Stateless
@NoArgsConstructor
//@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class UserServiceImpl implements UserService {

    @Inject
    @JPADao
    UserDao userDao;

    public int count() {
        return userDao.count();
    }

    public int getAmountOfFollowers(User user) throws UserException {
        if (user == null) {
            throw new IllegalArgumentException();
        }
        if (this.getById(user.getId()) == null) {
            throw new UserNotFoundException(user.getId());
        }

        return userDao.getAmountOfFollowers(user.getId());
    }

    public int getAmountOfFollowings(User user) throws UserException {
        if (user == null) {
            throw new IllegalArgumentException();
        }
        if (this.getById(user.getId()) == null) {
            throw new UserNotFoundException(user.getId());
        }

        return userDao.getAmountOfFollowings(user.getId());
    }

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

    public User authenticate(String name, String password) throws UserException {
        if (name == null || password == null) {
            throw new IllegalArgumentException();
        }

        User user = this.getByUserName(name);
        if (user == null) {
            throw new UserNotFoundException(name);
        }
        if (!user.getPassword().equals(Sha256.hash(password))) {
            throw new UserIncorrectCredentialsException(name);
        }

        return user;
    }

    public User rename(String name, User user) throws UserException {
        validateUser(user);

        user.setUsername(name);

        return userDao.edit(user);
    }

    @Override
    public User setPassword(String password, User user) throws UserException {
        validateUser(user);

        user.setPassword(password);

        return userDao.edit(user);
    }

    public User setBiography(String bio, User user) throws UserException {
        validateUser(user);

        user.setBio(bio);

        return userDao.edit(user);
    }

    @Override
    public User setLocation(String location, User user) throws UserException {
        validateUser(user);

        user.setLocation(location);

        return userDao.edit(user);
    }

    @Override
    public User setWebsite(String website, User user) throws UserException {
        validateUser(user);

        user.setWebsite(website);

        return userDao.edit(user);
    }

    public User setRole(Role role, User user) throws UserException {
        validateUser(user);

        user.setRole(role);

        return userDao.edit(user);
    }

    public void remove(User user) throws UserException {
        if (user == null) {
            throw new IllegalArgumentException();
        }
        if (this.getById(user.getId()) == null) {
            throw new UserNotFoundException(user.getId());
        }

        userDao.remove(user.getId());
    }

    public boolean isUserFollowedBy(User a, User b) throws UserException {
        validateUserFollowPair(a, b);

        if (!with(userDao).is(a).followedBy(b)) {
            throw new UserFollowException(UserFollowException.FollowViolationType.NOT_FOLLOWED_BY);
        }

        return true;
    }

    public boolean isUserFollowing(User a, User b) throws UserException {
        validateUserFollowPair(a, b);

        if (!with(userDao).is(a).following(b)) {
            throw new UserFollowException(UserFollowException.FollowViolationType.NOT_FOLLOWING);
        }

        return true;
    }

    public void followUser(User a, User b) throws UserException {
        validateUserFollowPair(a, b);

        if (a.equals(b)) {
            throw new UserFollowException(UserFollowException.FollowViolationType.SELF_FOLLOWING);
        }

        with(userDao).make(a).follow(b);
    }

    public void unFollowUser(User a, User b) throws UserException {
        validateUserFollowPair(a, b);

        with(userDao).make(a).unFollow(b);
    }

    public User getMostFollowed() {
        return userDao.getMostFollowed();
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

    public Pagination<User> getAllUsers(int page) {
        return userDao.findAll(page);
    }

    public Pagination<User> getAllUsers(int page, int size) {
        return userDao.findAll(page, size);
    }

    private void validateUserFollowPair(User a, User b) throws UserException {
        if (a == null || b == null) {
            throw new IllegalArgumentException();
        }
        if (this.getById(a.getId()) == null) {
            throw new UserNotFoundException(a.getId());
        }
        if (this.getById(b.getId()) == null) {
            throw new UserNotFoundException(b.getId());
        }
    }
}
