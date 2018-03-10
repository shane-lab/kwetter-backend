package nl.shanelab.kwetter.dal.tests;

import nl.shanelab.kwetter.dal.dao.UserDao;
import nl.shanelab.kwetter.dal.qualifiers.InMemoryDao;
import nl.shanelab.kwetter.util.WeldJUnit4Runner;
import org.junit.Before;
import org.junit.runner.RunWith;

import javax.inject.Inject;

import static com.google.common.truth.Truth.assertWithMessage;

@RunWith(WeldJUnit4Runner.class)
public class UserDaoTest {

    @Inject
    @InMemoryDao
    UserDao userDao;

    @Before
    public void shouldInjectDao() {
        assertWithMessage("The Dao was not injected using weld-junit")
                .that(userDao)
                .isNotNull();
    }
}
