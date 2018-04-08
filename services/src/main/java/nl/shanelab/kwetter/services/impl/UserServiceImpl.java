package nl.shanelab.kwetter.services.impl;

import lombok.NoArgsConstructor;
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
import java.util.Collection;

import static nl.shanelab.kwetter.services.util.UserFollowHelper.with;

@Stateless
@NoArgsConstructor
//@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class UserServiceImpl implements UserService {

    @Inject
    @JPADao
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

        user.setBio(bio);

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
