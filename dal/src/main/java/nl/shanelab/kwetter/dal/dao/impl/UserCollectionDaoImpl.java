package nl.shanelab.kwetter.dal.dao.impl;

import nl.shanelab.kwetter.dal.dao.UserDao;
import nl.shanelab.kwetter.dal.domain.User;
import nl.shanelab.kwetter.dal.qualifiers.InMemoryDao;

import javax.ejb.Stateless;
import java.util.Collection;

@InMemoryDao
@Stateless
public class UserCollectionDaoImpl extends BaseCollectionDao implements UserDao {

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

    public User getByUsername(String username) {
        return null;
    }
}
