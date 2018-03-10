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

        data.getUsers().put(nextId, user);

        return user;
    }

    public User edit(User user) {
        if (this.find(user.getId()) != null) {
            data.getUsers().replace(user.getId(), user);

            return user;
        }

        return this.create(user);
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
        AtomicReference<User> userRef = new AtomicReference<>();

        this.findAll().forEach(user -> {
            if (user.getUsername().equals(username)) {
                userRef.set(user);
            }
        });

        return userRef.get();
    }

    public boolean isFollowing(User a, User b) {
        if (!this.validateUserPair(a, b)) {
            return false;
        }
        if (a.getFollowers() == null) {
            return false; // no followers
        }
        if (b.getFollowing() == null) {
            return false; // not following anyone
        }

        return a.getFollowers().contains(b);
    }

    public boolean isFollowedBy(User a, User b) {
        return isFollowing(b, a);
    }

    public Set<Kweet> getNthLatestKweets(int nth) {
        return null;
    }

    public void createFollow(User a, User b) {
        if (!this.validateUserPair(a, b)) {
            return;
        }
        if (this.isFollowing(a, b)) {
            return; // 'a' is already following 'b'
        }

        this.data.createFollow(a, b);
    }

    public void unFollow(User a, User b) {
        if (!this.validateUserPair(a, b)) {
            return;
        }
        if (this.isFollowedBy(b, a)) { // when 'b' is followed by 'a' continue
            if (a.getFollowers() != null)
            {
                a.getFollowers().remove(b);
                edit(a);
            }

            if (b.getFollowing() != null) {
                b.getFollowing().remove(a);
                edit(b);
            }
        }
    }

    private boolean validateUserPair(User a, User b) {
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
