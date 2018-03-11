package nl.shanelab.kwetter.services.impl;

import nl.shanelab.kwetter.dal.dao.UserDao;
import nl.shanelab.kwetter.dal.qualifiers.InMemoryDao;
import nl.shanelab.kwetter.services.UserService;

import javax.inject.Inject;

public class UserServiceImpl implements UserService {
    
    @InMemoryDao
    @Inject
    UserDao userDao;
}
