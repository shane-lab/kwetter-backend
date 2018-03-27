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

        assert userDao.count() == 3;

        userDao.createFollow(user, admin);
        userDao.createFollow(user, mod);

        kweetDao.create(new Kweet("First Kweet, Hello World! #JEA6", admin));
        kweetDao.create(new Kweet("Anyone else on this platform? @die_administrator", mod));
    }
}
