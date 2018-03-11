package nl.shanelab.kwetter.dal.dao.impl;

import nl.shanelab.kwetter.dal.dao.UserDao;
import nl.shanelab.kwetter.dal.domain.Kweet;
import nl.shanelab.kwetter.dal.domain.User;
import nl.shanelab.kwetter.dal.qualifiers.JPADao;

import javax.ejb.Stateless;
import java.util.Collection;

@JPADao
@Stateless
public class UserJPADaoImpl implements UserDao {

    public int count() {
        return 0;
    }

    public User create(User user) {
        return null;
    }

    public User edit(User user) {
        return null;
    }

    public User find(Long id) {
        return null;
    }

    public Collection<User> findAll() {
        return null;
    }

    public void remove(User user) {

    }

    public User getByUsername(String name) {
        return null;
    }

    public boolean isFollowing(User a, User b) {
        return false;
    }

    public boolean isFollowedBy(User a, User b) {
        return false;
    }

    public Collection<Kweet> getNthLatestKweets(int nth, User user) {
        return null;
    }

    public void createFollow(User a, User b) {

    }

    public void unFollow(User a, User b) {

    }
}
