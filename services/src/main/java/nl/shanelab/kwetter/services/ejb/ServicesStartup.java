package nl.shanelab.kwetter.services.ejb;

import nl.shanelab.kwetter.dal.dao.KweetDao;
import nl.shanelab.kwetter.dal.dao.UserDao;
import nl.shanelab.kwetter.dal.domain.Kweet;
import nl.shanelab.kwetter.dal.domain.Role;
import nl.shanelab.kwetter.dal.domain.User;
import nl.shanelab.kwetter.dal.qualifiers.JPADao;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@Startup
@Singleton
@ApplicationScoped
public class ServicesStartup {

    @Inject
    @JPADao
    UserDao userDao;

    @Inject
    @JPADao
    KweetDao kweetDao;

    @PostConstruct
    private void onPostConstruct() {
        try {
            this.createInitialEntities();
        } catch (Exception e) {
//            e.printStackTrace();
        }
    }

    private void createInitialEntities() throws Exception {
        User user = userDao.create(new User("der_benutzer", "password", Role.USER));
        User mod = userDao.create(new User("das_moderator", "password", Role.MODERATOR));
        User admin = userDao.create(new User("die_administrator", "password", Role.ADMINISTRATOR));

        userDao.create(new User("a", "password", Role.USER));
        userDao.create(new User("b", "password", Role.USER));
        userDao.create(new User("c", "password", Role.USER));
        userDao.create(new User("d", "password", Role.USER));
        userDao.create(new User("e", "password", Role.USER));
        userDao.create(new User("f", "password", Role.USER));
        userDao.create(new User("g", "password", Role.USER));
        userDao.create(new User("h", "password", Role.USER));
        userDao.create(new User("i", "password", Role.USER));
        userDao.create(new User("j", "password", Role.USER));
        userDao.create(new User("k", "password", Role.USER));
        userDao.create(new User("l", "password", Role.USER));
        userDao.create(new User("m", "password", Role.USER));
        userDao.create(new User("n", "password", Role.USER));
        userDao.create(new User("o", "password", Role.USER));
        userDao.create(new User("p", "password", Role.USER));
        userDao.create(new User("q", "password", Role.USER));
        userDao.create(new User("r", "password", Role.USER));
        userDao.create(new User("s", "password", Role.USER));
        userDao.create(new User("t", "password", Role.USER));
        userDao.create(new User("u", "password", Role.USER));
        userDao.create(new User("v", "password", Role.USER));
        userDao.create(new User("w", "password", Role.USER));
        userDao.create(new User("x", "password", Role.USER));
        userDao.create(new User("y", "password", Role.USER));
        userDao.create(new User("z", "password", Role.USER));

        assert userDao.count() == 3;

        userDao.createFollow(user, admin);
        userDao.createFollow(user, mod);

        Kweet kweet = kweetDao.create(new Kweet("First Kweet, Hello World! #JEA6", admin));
        kweetDao.create(new Kweet("Anyone else on this platform? @die_administrator", mod));
        kweetDao.favourite(kweet, user);
    }
}
