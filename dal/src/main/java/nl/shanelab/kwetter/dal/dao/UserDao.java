package nl.shanelab.kwetter.dal.dao;

import nl.shanelab.kwetter.dal.domain.User;

public interface UserDao extends GenericDao<User, Long> {

    User getByUsername(String username);
}
