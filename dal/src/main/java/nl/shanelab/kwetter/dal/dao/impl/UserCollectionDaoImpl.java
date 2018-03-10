package nl.shanelab.kwetter.dal.dao.impl;

import lombok.NoArgsConstructor;
import nl.shanelab.kwetter.dal.dao.UserDao;
import nl.shanelab.kwetter.dal.domain.Kweet;
import nl.shanelab.kwetter.dal.domain.User;
import nl.shanelab.kwetter.dal.ejb.DummyData;
import nl.shanelab.kwetter.dal.qualifiers.InMemoryDao;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.Collection;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

@InMemoryDao
@Stateless
@NoArgsConstructor
public class UserCollectionDaoImpl extends BaseCollectionDao implements UserDao {

    @Inject
    public UserCollectionDaoImpl(DummyData data) {
        this.data = data;
    }

    public int count() {
        return data.getUsers().size();
    }

    public User create(User user) {
        if (this.getByUsername(user.getUsername()) != null) {
            return null;
        }

        long nextId = (long) this.count();

        user.setId(nextId);

        return data.getUsers().put(nextId, user);
    }

    public User edit(User user) {
        return data.getUsers().put(user.getId(), user);
    }

    public User find(Long id) {
        return data.getUsers().get(id);
    }

    public Collection<User> findAll() {
        return data.getUsers().values();
    }

    public void remove(User user) {
        data.getUsers().remove(user.getId());
    }

    public User getByUsername(String username) {
        AtomicReference<User> userRef = null;

        this.findAll().forEach(user -> {
            if (user.getUsername().equals(username)) {
                userRef.set(user);
            }
        });

        return userRef.get();
    }

    public boolean isFollowing(User a, User b) {
        if (a.getFollowers() == null) {
            return false;
        }
        if (b.getFollowing() == null) {
            return false;
        }

        return a.getFollowers().contains(b);
    }

    public boolean isFollower(User a, User b) {
        if (a.getFollowing() == null) {
            return false;
        }
        if (b.getFollowers() == null) {
            return false;
        }

        return a.getFollowing().contains(b);
    }

    public Set<Kweet> getNthLatestKweets(int nth) {
        return null;
    }
}
