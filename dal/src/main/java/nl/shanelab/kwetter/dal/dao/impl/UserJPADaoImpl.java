package nl.shanelab.kwetter.dal.dao.impl;


import nl.shanelab.kwetter.dal.dao.Pagination;
import nl.shanelab.kwetter.dal.dao.UserDao;
import nl.shanelab.kwetter.dal.domain.Kweet;
import nl.shanelab.kwetter.dal.domain.User;
import nl.shanelab.kwetter.dal.qualifiers.JPADao;
import nl.shanelab.kwetter.util.Sha256;

import javax.ejb.Stateless;
import javax.persistence.Query;
import java.util.Collection;
import java.util.Collections;

@JPADao
@Stateless
//@TransactionManagement(TransactionManagementType.CONTAINER)
//@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class UserJPADaoImpl extends BaseJPADao<User, Long> implements UserDao {

    public User create(User user) {
        if (this.getByUsername(user.getUsername()) != null) {
            return null;
        }

        user.setPassword(Sha256.hash(user.getPassword()));

        return super.create(user);
    }

    public User edit(User user) {
        User found = this.find(user.getId());
        if (found != null) {
            if (user.getPassword() != found.getPassword()) {
                user.setPassword(Sha256.hash(user.getPassword()));
            }
            return super.edit(user);
        }

        return this.create(user);
    }

    @Override
    public void remove(Long id) {
        Query query = manager.createQuery("DELETE FROM Kweet k WHERE k.author.id = :id");
        query.setParameter("id", id).executeUpdate();

        User user = find(id);
        user.setKweets(Collections.EMPTY_SET);

        super.remove(id);
    }

    public int getAmountOfFollowers(long id) {
        Query query = manager.createNamedQuery("User.getAmountOfFollowers", User.class);
        query.setParameter("id", id);

        Object size = JPAResult.getSingleResultOrNull(query);
        if (size == null) {
            return 0;
        }

        return (Integer) size;
    }

    public int getAmountOfFollowings(long id) {
        Query query = manager.createNamedQuery("User.getAmountOfFollowings", User.class);
        query.setParameter("id", id);

        Object size = JPAResult.getSingleResultOrNull(query);
        if (size == null) {
            return 0;
        }

        return (Integer) size;
    }

    public User getByUsername(String name) {
        Query query = manager.createNamedQuery("User.findByName", User.class);
        query.setParameter("username", name);

        return JPAResult.getSingleResultOrNull(query);
    }

    public boolean isFollowing(User a, User b) {
        if (!validateUserPair(a, b)) {
            return false;
        }

//        Query query = manager.createNamedQuery("User.followedBy");
        Query query = manager.createNativeQuery(String.format("SELECT COUNT(*) FROM follower_following ff WHERE ff.follower_id = %1s AND ff.following_id = %2s", a.getId(), b.getId()));
//        query.setParameter("id", a.getId());
//        query.setParameter("follower_id", b.getId());

        return (Long) JPAResult.getSingleResultOrNull(query) > 0;
    }

    public boolean isFollowedBy(User a, User b) {
        return this.isFollowing(b, a);
    }

    public Collection<Kweet> getNthLatestKweets(int nth, User user) {
        Query query = manager.createNamedQuery("Kweet.findByUserName", Kweet.class);
        query.setParameter("username", user.getUsername());
        query.setMaxResults(nth > 0 ? nth : 1);

        return query.getResultList();
    }

    public Pagination<User> getFollowers(User user, int page, int size) {
        int count = this.getAmountOfFollowers(user.getId());

        Query query = manager.createNamedQuery("User.findFollowers", User.class);
        query.setParameter("username", user.getUsername());

        return fromQuery(page, size, count, query);
    }

    public Pagination<User> getFollowing(User user, int page, int size) {
        int count = this.getAmountOfFollowings(user.getId());

        Query query = manager.createNamedQuery("User.findFollowing", User.class);
        query.setParameter("username", user.getUsername());

        return fromQuery(page, size, count, query);
    }

    public void createFollow(User a, User b) {
        if (isFollowing(a, b)) {
            return;
        }

        a.getFollowing().add(b);
        b.getFollowers().add(a);

        manager.merge(a);
        manager.merge(b);
    }

    public void unFollow(User a, User b) {
        if (a.getFollowing().contains(b)) {
            a.getFollowing().remove(b);
        }
        if (b.getFollowers().contains(a)) {
            b.getFollowers().remove(a);
        }

        manager.merge(a);
        manager.merge(b);
    }

    @Override
    public User getMostFollowed() {
        Query query = manager.createNamedQuery("User.getMostFollowed", User.class);
        query.setMaxResults(1);

        return JPAResult.getSingleResultOrNull(query);
    }
}
