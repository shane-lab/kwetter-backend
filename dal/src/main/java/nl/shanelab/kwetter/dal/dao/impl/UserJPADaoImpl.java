package nl.shanelab.kwetter.dal.dao.impl;

import nl.shanelab.kwetter.dal.dao.UserDao;
import nl.shanelab.kwetter.dal.domain.Kweet;
import nl.shanelab.kwetter.dal.domain.User;
import nl.shanelab.kwetter.dal.qualifiers.JPADao;

import javax.ejb.*;
import javax.persistence.Query;
import java.util.Collection;

@JPADao
@Stateless
//@TransactionManagement(TransactionManagementType.CONTAINER)
//@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class UserJPADaoImpl extends BaseJPADao<User, Long> implements UserDao {

    public User create(User user) {
        if (this.getByUsername(user.getUsername()) != null) {
            return null;
        }

        return super.create(user);
    }

    public User edit(User user) {
        if (this.find(user.getId()) != null) {
            return super.edit(user);
        }

        return this.create(user);
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
        return null;
//        Query query = manager.createNamedQuery("Kweet.findByUserName", Kweet.class);
//        query.setParameter("username", user.getUsername());
//        query.setMaxResults(nth > 0 ? nth : 1);
//
//        return JPAResult.getSingleResultOrNull(query);
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
}
