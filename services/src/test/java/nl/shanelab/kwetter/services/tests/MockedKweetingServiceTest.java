package nl.shanelab.kwetter.services.tests;

import nl.shanelab.kwetter.dal.dao.HashTagDao;
import nl.shanelab.kwetter.dal.dao.KweetDao;
import nl.shanelab.kwetter.dal.domain.HashTag;
import nl.shanelab.kwetter.dal.domain.Kweet;
import nl.shanelab.kwetter.dal.domain.Role;
import nl.shanelab.kwetter.dal.domain.User;
import nl.shanelab.kwetter.services.exceptions.KweetException;
import nl.shanelab.kwetter.services.exceptions.UserException;
import nl.shanelab.kwetter.services.impl.KweetingServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.HashMap;
import java.util.Map;

import static com.google.common.truth.Truth.assertWithMessage;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.Silent.class)
public class MockedKweetingServiceTest extends BaseMockedServiceTest {

    @Mock
    KweetDao kweetDaoMock;

    @Mock
    HashTagDao hashTagDaoMock;

    @InjectMocks
    KweetingServiceImpl kweetingService;

    private Map<Long, Kweet> kweets;

    private User userA, userB;

    @Before
    public void onSetup() {
        super.onSetup();

        kweets = new HashMap<>();

        Kweet k = new Kweet("Some Kweet", users.get((long) 0));
        k.setId(0);
        kweets.put(k.getId(), k);

        assertWithMessage("The service was not mocked/injected")
                .that(kweetingService)
                .isNotNull();
    }

    @Override
    protected void mocks() {
        super.mocks();

        when(kweetDaoMock.create(any(Kweet.class))).thenReturn(kweets.get((long) 0));
        when(kweetDaoMock.edit(any(Kweet.class))).thenReturn(kweets.get((long) 0));

        when(kweetDaoMock.find(anyLong())).thenReturn(null);
        when(kweetDaoMock.find((long)0)).thenReturn(kweets.get((long) 0));
        when(kweetDaoMock.findAll()).thenReturn(kweets.values());

        when(kweetDaoMock.isMentionedIn(any(Kweet.class), any(User.class))).thenReturn(false);

        when(kweetDaoMock.isFavoritedBy(any(Kweet.class), any(User.class))).thenReturn(false);

        when(kweetDaoMock.getByUserId(anyLong())).thenReturn(kweets.values());
        when(kweetDaoMock.getByUserName(anyString())).thenReturn(kweets.values());

        when(kweetDaoMock.getByFavoritedBy(anyString())).thenReturn(kweets.values());

        when(kweetDaoMock.getByHashTagId(anyLong())).thenReturn(null);
        when(kweetDaoMock.getByHashTagName(anyString())).thenReturn(null);

        when(kweetDaoMock.getByMention(anyString())).thenReturn(null);

        when(hashTagDaoMock.find(anyLong())).thenReturn(null);
        when(hashTagDaoMock.findAll()).thenReturn(null);

        when(hashTagDaoMock.getByName(anyString())).thenReturn(null);

        userA = userDaoMock.getByUsername(anyString());

        userB = new User("isUserMentionedInKweet", "password", Role.USER);
        userB.setId(users.size());
        users.put(userB.getId(), userB);

        when(userDaoMock.find(userB.getId())).thenReturn(userB);
    }

    @Test
    public void createKweet() throws UserException {
        mocks();

        Kweet kweet = kweetingService.createKweet("Some Kweet", userA);

        assertWithMessage("The Kweet was not invoked")
                .that(kweet)
                .isNotNull();

        verify(kweetDaoMock, times(1)).create(any());
    }

    @Test
    public void editKweet() throws UserException, KweetException {
        mocks();

        Kweet k = kweetingService.createKweet("Some Kweet", userA);

        k.setMessage("Kweet some");

        Kweet edited = kweetingService.editKweet(k);

        assertWithMessage("The returned Kweet from the change differs from the fetched userA by name")
                .that(k)
                .isEqualTo(edited);
    }

    @Test
    public void getKweetById() throws KweetException {
        mocks();

        Kweet k = kweetingService.getKweetById(0);

        assertWithMessage("Kweet was not found")
                .that(k)
                .isNotNull();

        Kweet k1 = kweetingService.getKweetById(2);

        assertWithMessage("Kweet was found, expected null")
            .that(k1)
            .isNull();
    }

    @Test
    public void getKweetsByUser() throws UserException {
        mocks();

        assertWithMessage("No Kweets were returned, expected atleast one")
                .that(kweetingService.getKweetsByUser(userA).size())
                .isGreaterThan(0);

        assertWithMessage("No Kweets were returned, expected atleast one")
                .that(kweetingService.getKweetsByUserName(anyString()).size())
                .isGreaterThan(0);

        assertWithMessage("No Kweets were returned, expected atleast one")
                .that(kweetingService.getKweetsByUserId(userA.getId()).size())
                .isGreaterThan(0);
    }

    @Test
    public void getKweetsWithMention() throws UserException {
        mocks();

        assertWithMessage("Kweets were returned, expected none")
                .that(kweetingService.getKweetsWithMention(userA))
                .isNull();

        assertWithMessage("Kweets were returned, expected none")
                .that(kweetingService.getKweetsWithMentionByUserId(anyLong()))
                .isNull();

        assertWithMessage("Kweets were returned, expected none")
                .that(kweetingService.getKweetsWithMentionByUserName(anyString()))
                .isNull();
    }

    @Test
    public void getKweetsWithHashTag() throws UserException {
        mocks();

        HashTag h = new HashTag(anyString());

        assertWithMessage("Kweets were returned, expected none")
                .that(kweetingService.getKweetsWithHashTag(h))
                .isNull();

        assertWithMessage("Kweets were returned, expected none")
                .that(kweetingService.getKweetsWithHashTagId(anyLong()))
                .isNull();


        assertWithMessage("Kweets were returned, expected none")
                .that(kweetingService.getKweetsWithHashTagName(anyString()))
                .isNull();
    }

    @Test
    public void isUserMentionedInKweet() throws KweetException, UserException {
        mocks();

        Kweet k = kweetingService.getKweetById(anyLong());

        assertWithMessage("User B was mentioned")
                .that(kweetingService.isUserMentionedInKweet(k, userB))
                .isFalse();
    }

    @Test
    public void isKweetFavoritedByUser() throws KweetException, UserException {
        mocks();

        Kweet k = kweetingService.getKweetById(anyLong());

        assertWithMessage("User B has favourited the Kweet, expected false")
                .that(kweetingService.isKweetFavoritedByUser(k, userB))
                .isFalse();
    }

    @Test
    public void isKweetOwnedByUser() throws KweetException, UserException {
        mocks();

        Kweet k = kweetingService.getKweetById(anyLong());

        assertWithMessage("User B is not the owner of the Kweet")
                .that(kweetingService.isKweetOwnedByUser(k, userB))
                .isFalse();
    }
}
