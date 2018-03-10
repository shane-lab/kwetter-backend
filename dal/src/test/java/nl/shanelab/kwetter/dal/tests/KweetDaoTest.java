package nl.shanelab.kwetter.dal.tests;

import nl.shanelab.kwetter.dal.dao.KweetDao;
import nl.shanelab.kwetter.dal.dao.impl.KweetCollectionDaoImpl;
import nl.shanelab.kwetter.dal.domain.Kweet;
import nl.shanelab.kwetter.dal.qualifiers.InMemoryDao;
import nl.shanelab.kwetter.util.WeldJUnit4Runner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;

import static com.google.common.truth.Truth.assertWithMessage;

@RunWith(WeldJUnit4Runner.class)
public class KweetDaoTest {

    @Inject
    @InMemoryDao
    KweetDao kweetDao;

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
}
