package nl.shanelab.kwetter.services.ejb;

import nl.shanelab.kwetter.dal.dao.HashTagDao;
import nl.shanelab.kwetter.dal.dao.KweetDao;
import nl.shanelab.kwetter.dal.dao.UserDao;
import nl.shanelab.kwetter.dal.domain.Kweet;
import nl.shanelab.kwetter.dal.domain.Role;
import nl.shanelab.kwetter.dal.domain.User;
import nl.shanelab.kwetter.dal.qualifiers.InMemoryDao;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;

@Startup
@Singleton
public class ServicesStartup {

    @Inject
    @InMemoryDao
    UserDao userDao;

    @Inject
    @InMemoryDao
    KweetDao kweetDao;

    @Inject
    @InMemoryDao
    HashTagDao hashTagDao;

    @PostConstruct
    private void onPostConstruct() {
        User userShane = userDao.create(new User("shane", "password", Role.USER));
        User userMod = userDao.create(new User("mod", "password", Role.MODERATOR));
        User userAdmin = userDao.create(new User("admin", "password", Role.ADMINISTRATOR));

        userDao.createFollow(userShane, userAdmin);
        userDao.createFollow(userShane, userMod);

        kweetDao.create(new Kweet("First Kweet, Hello World!", userAdmin));
        kweetDao.create(new Kweet("Anyone else on this platform? @admin", userShane));
    }
}
