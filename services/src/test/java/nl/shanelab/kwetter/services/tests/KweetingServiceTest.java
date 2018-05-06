package nl.shanelab.kwetter.services.tests;

import nl.shanelab.kwetter.dal.domain.Kweet;
import nl.shanelab.kwetter.dal.domain.Role;
import nl.shanelab.kwetter.dal.domain.User;
import nl.shanelab.kwetter.services.KweetingService;
import nl.shanelab.kwetter.services.UserService;
import nl.shanelab.kwetter.services.exceptions.KweetException;
import nl.shanelab.kwetter.services.exceptions.UserException;
import nl.shanelab.kwetter.services.exceptions.kweet.KweetFavouriteException;
import nl.shanelab.kwetter.services.exceptions.kweet.KweetNotFoundException;
import nl.shanelab.kwetter.services.exceptions.user.UserNotFoundException;
import nl.shanelab.kwetter.services.tests.matcher.KweetFavouriteExceptionMatcher;
import nl.shanelab.kwetter.util.WeldJUnit4Runner;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;

import javax.inject.Inject;

import static com.google.common.truth.Truth.assertWithMessage;

@RunWith(WeldJUnit4Runner.class)
public class KweetingServiceTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Inject
    // injected to create new users
    UserService userService;

    @Inject
    KweetingService kweetingService;

    @Before
    public void shouldInjectService() {
        assertWithMessage("The service was not injected using weld-junit")
                .that(kweetingService)
                .isNotNull();
    }

    @Test
    public void shouldCreateAKweet() throws UserException {
        final String name = "shouldCreateAKweet";
        User user = userService.register(name, "password");

        Kweet kweet = kweetingService.createKweet("Some kweet", user);

        assertWithMessage("The author of the Kweet is not the same as the expected user")
                .that(kweet.getAuthor())
                .isEqualTo(user);

        kweetingService.createKweet("Second kweet", user);
        kweetingService.createKweet("Third kweet", user);

        assertWithMessage("The user does not have the expected amount of Kweets")
                .that(kweetingService.getKweetsByUser(user).size())
                .isEqualTo(3);

        assertWithMessage("The user does not have the expected amount of Kweets when fetched by name")
                .that(kweetingService.getKweetsByUserName(name).size())
                .isEqualTo(3);

        assertWithMessage("The user does not have the expected amount of Kweets when fetched by id")
                .that(kweetingService.getKweetsByUserId(user.getId()).size())
                .isEqualTo(3);
    }

    @Test(expected = UserNotFoundException.class)
    public void shouldNotCreateAKweet() throws UserException {
        User user = new User("shouldNotCreateAKweet", "password", Role.USER);

        // the user does not exist in the repository. Should throw an UserNotFoundException
        kweetingService.createKweet("Some kweet", user);
    }

    @Test
    public void shouldBeTheOwnerOfTheKweet() throws UserException, KweetException {
        User user = userService.register("shouldBeTheOwnerOfTheKweet", "password");

        Kweet kweet = kweetingService.createKweet("Some kweet", user);

        assertWithMessage("The author is of the Kweet was not the same as the expected user")
                .that(kweetingService.isKweetOwnedByUser(kweet, user))
                .isTrue();
    }

    @Test
    public void shouldNotBeTheOwnerOfTheKweet() throws UserException, KweetException {
        User userA = userService.register("shouldBeTheOwnerOfTheKweet_A", "password");
        User userB = userService.register("shouldBeTheOwnerOfTheKweet_B", "password");

        Kweet kweet = kweetingService.createKweet("Some kweet", userA);

        assertWithMessage("The Kweet belongs too user B")
            .that(kweetingService.isKweetOwnedByUser(kweet, userB))
            .isFalse();
    }

    @Test
    public void shouldBeAbleToRemoveAKweet() throws UserException, KweetException {
        User user = userService.register("shouldBeAbleToRemoveAKweet", "password");

        Kweet kweet = kweetingService.createKweet("Some kweet", user);

        kweetingService.removeKweet(kweet);

        assertWithMessage("The Kweet was not removed")
                .that(kweetingService.getKweetById(kweet.getId()))
                .isNull();
    }

    @Test(expected = KweetNotFoundException.class)
    public void shouldNotBeAbleToRemoveAKweetAgain() throws UserException, KweetException {
        User user = userService.register("shouldNotBeAbleToRemoveAKweetAgain", "password");

        Kweet kweet = kweetingService.createKweet("Some kweet", user);

        kweetingService.removeKweet(kweet);

        // the kweet is already removed. Should throw a KweetNotFoundException
        kweetingService.removeKweet(kweet);
    }

    @Test
    public void shouldBeAbleToFavouriteAndUnFavouriteAKweet() throws UserException, KweetException {
        User userA = userService.register("shouldBeAbleToFavouriteAndUnFavouriteAKweet_A", "password");
        User userB = userService.register("shouldBeAbleToFavouriteAndUnFavouriteAKweet_B", "password");

        Kweet kweet = kweetingService.createKweet("Some kweet", userA);

        kweetingService.favouriteKweet(kweet, userB);

        assertWithMessage("The user did not favourite the expected kweet")
                .that(kweetingService.isKweetFavoritedByUser(kweet, userB))
                .isTrue();

        assertWithMessage("The user does not have have any favourited Kweets")
                .that(kweetingService.getFavouritedKweets(userB).size())
                .isEqualTo(1);

        kweetingService.unFavouriteKweet(kweet, userB);

        assertWithMessage("The user still has favourited Kweets")
                .that(kweetingService.getFavouritedKweets(userB))
                .isEmpty();
    }

    @Test
    public void shouldNotBeAbleToFavouriteAnOwnedKweet() throws UserException, KweetException {
        thrown.expect(KweetFavouriteExceptionMatcher.of(KweetFavouriteException.FavouriteViolationType.SELF_FAVOURIITNG));

        User user = userService.register("shouldNotBeAbleToFavouriteAnOwnedKweet", "password");

        Kweet kweet = kweetingService.createKweet("Some kweet", user);

        // the created user is the author of the Kweet. Should throw a KweetFavouriteException with type SELF_FAVOURING
        kweetingService.favouriteKweet(kweet, user);
    }

    @Test
    public void shouldNotBeAbleToFavouriteAFavouritedKweet() throws UserException, KweetException {
        User userA = userService.register("shouldNotBeAbleToFavouriteAFavouritedKweet_A", "password");
        User userB = userService.register("shouldNotBeAbleToFavouriteAFavouritedKweet_B", "password");

        Kweet kweet = kweetingService.createKweet("Some kweet", userA);

        kweetingService.favouriteKweet(kweet, userB);

        thrown.expect(KweetFavouriteExceptionMatcher.of(KweetFavouriteException.FavouriteViolationType.ALREADY_FAVOURITED));

        // user B already favourited the Kweet by user A. Should throw a KweetFavouriteException with type ALREADY_FAVOURITED
        kweetingService.favouriteKweet(kweet, userB);
    }

    @Test
    public void shouldNotBeAbleToUnFavouriteANonFavouritedKweet() throws UserException, KweetException {
        thrown.expect(KweetFavouriteExceptionMatcher.of(KweetFavouriteException.FavouriteViolationType.NOT_FAVOURITED));

        User userA = userService.register("shouldNotBeAbleToUnFavouriteANonFavouritedKweet_A", "password");
        User userB = userService.register("shouldNotBeAbleToUnFavouriteANonFavouritedKweet_B", "password");

        Kweet kweet = kweetingService.createKweet("Some kweet", userA);

        // user B did not favourite the Kweet of user A. Should throw a KweetFavouriteException with type of NOT_FAVOURITED
        kweetingService.unFavouriteKweet(kweet, userB);
    }
}
