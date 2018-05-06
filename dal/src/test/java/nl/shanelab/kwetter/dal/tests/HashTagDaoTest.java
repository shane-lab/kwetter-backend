package nl.shanelab.kwetter.dal.tests;

import nl.shanelab.kwetter.dal.dao.HashTagDao;
import nl.shanelab.kwetter.dal.dao.impl.HashTagCollectionDaoImpl;
import nl.shanelab.kwetter.dal.domain.HashTag;
import nl.shanelab.kwetter.dal.qualifiers.InMemoryDao;
import nl.shanelab.kwetter.util.WeldJUnit4Runner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;

import static com.google.common.truth.Truth.assertWithMessage;

@RunWith(WeldJUnit4Runner.class)
public class HashTagDaoTest {

    @Inject
    @InMemoryDao
    HashTagDao hashTagDao;

    @Before
    public void shouldInjectDao() {
        assertWithMessage("The Dao was not injected")
                .that(hashTagDao)
                .isNotNull();
    }

    @Test
    /**
     * @ test-case explicitly used to validate pre-rendered data
     */
    public void assertHashTagDummyData() {
        if (!(hashTagDao instanceof HashTagCollectionDaoImpl)) {
            return;
        }

        assertWithMessage("The expected amount of hashtags were not inserted")
                .that(hashTagDao.count())
                .isAtLeast(2);

        HashTag hashTag = hashTagDao.find((long) 0);
        HashTag hashTag1 = hashTagDao.find((long) 1);

        assertHashTag(hashTag, (long) 0,"FHICT");
        assertHashTag(hashTag1, (long) 1,"kwetter");
    }

    /**
     * Assert expected hashtag properties
     *
     * @param hashTag The hashtag to assert
     * @param id The expected id
     * @param expectedName The expected name of the hashtag
     */
    private void assertHashTag(HashTag hashTag, long id, String expectedName) {
        String name = hashTag.getName();

        assertWithMessage(String.format("The expected id(%d) for hashtag '%s' differs from the actual id", id, name))
                .that(hashTag.getId())
                .isEqualTo(id);

        assertWithMessage(String.format("The expected name('%s') for hashtag '%s' differs from the actual name", expectedName, name))
                .that(name)
                .isEqualTo(expectedName);
    }

    @Test
    public void shouldBeAbleToCreate() {
        int currExpectedId = hashTagDao.count();

        hashTagDao.create(new HashTag("Kwetter"));

        HashTag lookupById = hashTagDao.find((long) currExpectedId);
        HashTag lookupByName = hashTagDao.getByName("Kwetter");

        assertWithMessage("The hashtag looked up with name is not equal to the hashtag looked up by id")
                .that(lookupById)
                .isEqualTo(lookupByName);
    }

    @Test
    public void shouldNotBeAbleToCreateDuplicate() {
        int prevSize = hashTagDao.count();

        hashTagDao.create(new HashTag("UniqueIdentifier"));
        hashTagDao.create(new HashTag("UniqueIdentifier"));

        assertWithMessage("A duplicate of the hashtag was inserted")
                .that(hashTagDao.count())
                .isEqualTo(prevSize + 1);
    }


    @Test
    public void shouldNotBeAbleToEditName() {

    }
}
