package nl.shanelab.kwetter.dal.tests;

import nl.shanelab.kwetter.dal.dao.UserDao;
import nl.shanelab.kwetter.dal.dao.impl.UserCollectionDaoImpl;
import nl.shanelab.kwetter.dal.domain.Role;
import nl.shanelab.kwetter.dal.domain.User;
import nl.shanelab.kwetter.dal.qualifiers.InMemoryDao;
import nl.shanelab.kwetter.util.WeldJUnit4Runner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;

import static com.google.common.truth.Truth.assertThat;
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

    @Test
    /**
     * A test-case explicitly used to validate pre-rendered data
     *
     * @see nl.shanelab.kwetter.dal.ejb.DummyData
     */
    public void assertUserDummyData() {
        // explicitly continue running the test when dummydata is used
        if (!(userDao instanceof UserCollectionDaoImpl)) {
            return;
        }

        // assert inserted users
        assertWithMessage("The expected amount of users were not inserted")
                .that(userDao.count())
                .isAtLeast(3);

        User userShane = userDao.getByUsername("Shane");
        User userAlice = userDao.getByUsername("Alice");
        User userBob = userDao.getByUsername("Bob");

        // assert properties on userShane
        assertUser(userShane, (long) 0, 2, 2, 2);
        assertUser(userAlice, (long) 1, 1, 2, 1);
        assertUser(userBob, (long) 2, 2, 1, 2);
    }

    /**
     * Assert expected user properties
     *
     * @param user The user to assert
     * @param id The expected id
     * @param kweets The expected amount of kweets
     * @param followers The expected amount of followers
     * @param following The expected amount of following
     */
    private void assertUser(User user, long id, int kweets, int followers, int following) {
        String name = user.getUsername();

        assertWithMessage(String.format("The expected id(%d) for user '%s' differs from the actual id", id, name))
                .that(user.getId())
                .isEqualTo(id);

        assertWithMessage(String.format("The expected amount of kweets(%d) for user '%s' differs from the actual amount", kweets, name))
                .that(user.getKweets().size())
                .isEqualTo(kweets);

        if (followers > 0) {
            assertWithMessage(String.format("The expected amount of followers(%d) for user '%s' differs from the actual amount", followers, name))
                    .that(user.getFollowers().size())
                    .isEqualTo(followers);
        }

        if (following > 0) {
            assertWithMessage(String.format("The expected amount of followings(%d) for user '%s' differs", followers, name))
                    .that(user.getFollowing().size())
                    .isEqualTo(following);
        }
    }

    @Test
    public void shouldCreateAnUserAndAppendIncrementedId() {
        int currExpectedId = userDao.count();

        userDao.create(new User("CurrentUser", "current", Role.USER));

        User user = userDao.find((long) currExpectedId);

        int nextExpectedId = userDao.count();

        assertThat(nextExpectedId).isGreaterThan(currExpectedId);

        User user1 = userDao.create(new User("NextUser", "next", Role.USER));

        assertThat(user1).isNotNull();

        User user1Dup = userDao.find((long) nextExpectedId);

        assertWithMessage("Created user is not equal to fetched user")
                .that(user1)
                .isEqualTo(user1Dup);
    }

    @Test
    public void shouldCreateAndDestructAFollowing() {
        User userA = userDao.create(new User("UserA", "a", Role.USER));
        User userB = userDao.create(new User("UserB", "b", Role.USER));

        // userA is following userB
        userDao.createFollow(userA, userB);

        assertWithMessage("UserB does not have UserA as follower")
                .that(userDao.isFollowedBy(userB, userA))
                .isTrue();

        assertWithMessage("UserA is not following UserB")
                .that(userDao.isFollowing(userA, userB))
                .isTrue();

        // userA un-follows userB
        userDao.unFollow(userA, userB);

        assertWithMessage("UserB still does have UserA as follower")
                .that(userDao.isFollowedBy(userB, userA))
                .isFalse();

        assertWithMessage("UserA is still following UserB")
                .that(userDao.isFollowing(userA, userB))
                .isFalse();
    }

    @Test
    public void shouldNotCreateAFollowing() {
        User userA = new User("UserA", "a", Role.USER);

        User userB = userDao.find((long)0);

        assertThat(userB).isNotNull();

        // create following with userA not being part of the repository
        userDao.createFollow(userA, userB);

        assertWithMessage("UserB has UserA as follower")
                .that(userDao.isFollowedBy(userB, userA))
                .isFalse();
    }
}
