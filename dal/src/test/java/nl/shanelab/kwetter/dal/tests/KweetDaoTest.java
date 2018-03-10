package nl.shanelab.kwetter.dal.tests;

import nl.shanelab.kwetter.dal.dao.KweetDao;
import nl.shanelab.kwetter.dal.dao.UserDao;
import nl.shanelab.kwetter.dal.dao.impl.KweetCollectionDaoImpl;
import nl.shanelab.kwetter.dal.domain.Kweet;
import nl.shanelab.kwetter.dal.domain.Role;
import nl.shanelab.kwetter.dal.domain.User;
import nl.shanelab.kwetter.dal.qualifiers.InMemoryDao;
import nl.shanelab.kwetter.util.WeldJUnit4Runner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;

import java.util.ArrayList;

import static com.google.common.truth.Truth.assertThat;
import static com.google.common.truth.Truth.assertWithMessage;

@RunWith(WeldJUnit4Runner.class)
public class KweetDaoTest {

    @Inject
    @InMemoryDao
    KweetDao kweetDao;

    @Inject
    @InMemoryDao
    // required to inject UserDao to create new test users,
    // fake users are not permitted
    UserDao userDao;

    @Before
    public void shouldInjectDao() {
        assertWithMessage("The Dao was not injected using weld-junit")
                .that(kweetDao)
                .isNotNull();
    }

    @Test
    /**
     * A test-case explicitly used to validate pre-rendered data
     *
     * @see nl.shanelab.kwetter.dal.ejb.DummyData
     */
    public void assertKweetDummyData() {
        if (!(kweetDao instanceof KweetCollectionDaoImpl)) {
            return;
        }

        // assert inserted kweets
        assertWithMessage("The expected amount of kweets were not inserted")
                .that(kweetDao.count())
                .isAtLeast(5);

        Kweet kweetShane = kweetDao.find((long) 0);
        Kweet kweetShane1 = kweetDao.find((long) 1);
        Kweet kweetAlice = kweetDao.find((long) 2);
        Kweet kweetBob = kweetDao.find((long) 3);
        Kweet kweetBob1 = kweetDao.find((long) 4);

        assertKweet(kweetShane, (long) 0, "Shane", 0, 0);
        assertKweet(kweetShane1, (long) 1, "Shane", 0, 2);
        assertKweet(kweetAlice, (long) 2, "Alice", 0, 0);
        assertKweet(kweetBob, (long) 3, "Bob", 0, 0);
        assertKweet(kweetBob1, (long) 4, "Bob", 1, 0);
    }

    /**
     * Assert expected Kweet properties
     *
     * @param kweet The Kweet to assert
     * @param id The expected id
     * @param authorName The expected amount author name of the Kweet
     * @param mentions The expected amount of mentions in the Kweet
     * @param hashtags The expected amount of hashtags in the Kweet
     */
    public void assertKweet(Kweet kweet, long id, String authorName, int mentions, int hashtags) {
        assertWithMessage(String.format("The expected id(%d) differs from the actual id", kweet.getId()))
                .that(kweet.getId())
                .isEqualTo(id);

        assertWithMessage(String.format("The expected author('%s') differs from the actual author", authorName))
                .that(kweet.getAuthor().getUsername())
                .isEqualTo(authorName);

        if (mentions > 0) {
            assertWithMessage(String.format("The expected amount of mentions(%d) differs from the actual amount", mentions))
                    .that(kweet.getMentions().size())
                    .isEqualTo(mentions);
        }

        if (hashtags > 0) {
            assertWithMessage(String.format("The expected amount of hashtags(%d) differs from the actual amount", hashtags))
                    .that(kweet.getHashTags().size())
                    .isEqualTo(hashtags);
        }
    }

    @Test
    public void shouldBeAbleToPostAKweet() {
        User user = userDao.create(new User("shouldBeAbleToPostAKweet", "password", Role.USER));

        assertWithMessage("Newly created user already has Kweets")
                .that(user.getKweets())
                .isNull();

        Kweet kweet = kweetDao.create(new Kweet("aa", user));

        assertThat(kweet).isNotNull();

        assertWithMessage("Newly created user does not have any Kweets")
                .that(user.getKweets().size())
                .isEqualTo(1);
    }

    @Test
    public void shouldBeAbleToFavouriteAKweet() {
        User userIsfavouriter = userDao.create(new User("Favouriter", "password", Role.USER));

        assertWithMessage("Newly created user without kweets already favourited some Kweets")
                .that(userIsfavouriter.getFavoriteKweets())
                .isNull();

        User userWhosKweetGotFavourited = userDao.create(new User("Favourited", "password", Role.USER));

        Kweet kweet = kweetDao.create(new Kweet("bb", userWhosKweetGotFavourited));

        kweetDao.favourite(kweet, userIsfavouriter);

        assertWithMessage("Newly created user without kweets does not have any favourited Kweets")
                .that(userIsfavouriter.getFavoriteKweets().size())
                .isEqualTo(1);

        assertWithMessage("The newly created Kweet is not favourited")
                .that(kweet.getFavoritedBy().size())
                .isEqualTo(1);

        // self-favouriting
        kweetDao.favourite(kweet, userWhosKweetGotFavourited);

        assertWithMessage("Newly created user with kweets self-favourited a his Kweet")
                .that(userWhosKweetGotFavourited.getFavoriteKweets())
                .isNull();

        assertWithMessage("The newly created Kweet is not favourited")
                .that(kweet.getFavoritedBy().size())
                .isEqualTo(1);

        // unset favourite
        kweetDao.unFavourite(kweet, userIsfavouriter);

        assertWithMessage("The newly created Kweet is not favourited")
                .that(kweet.getFavoritedBy().size())
                .isEqualTo(0);
    }

    @Test
    public void shouldBeAbleToMentionAUser() {
        User userIsMentioner = userDao.create(new User("Mentioner", "password", Role.USER));

        // TODO share injected dummydata context, WeldJUnit does not share injected singleton-bean over other injected beans
//        User userWhoGotMentioned = userDao.create(new User("Mentioned", "password", Role.USER));

        Kweet kweet = kweetDao.create(new Kweet("In reply to @Shane, @Alice and @NonExisting", userIsMentioner));

        assertWithMessage("The newly created Kweet does not have any mentions")
                .that(kweet.getMentions().size())
                .isEqualTo(2);

        User user = new ArrayList<User>(kweet.getMentions()).get(0);

        assertWithMessage("The mentioned user was not mentioned")
                .that(kweetDao.isMentionedIn(kweet, user))
                .isTrue();
    }

    @Test
    public void shouldBeAbleToInvokeAHashTagOnAKweet() {
        User user = userDao.create(new User("HashTagInvoker", "password", Role.USER));

        Kweet kweet = kweetDao.create(new Kweet("This is a message with hashtags #One #two three", user));

        assertWithMessage("The newly created Kweet does not have any hashtags")
                .that(kweet.getHashTags().size())
                .isEqualTo(2);
    }
}
