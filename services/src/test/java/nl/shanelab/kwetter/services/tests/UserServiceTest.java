package nl.shanelab.kwetter.services.tests;

import nl.shanelab.kwetter.dal.domain.User;
import nl.shanelab.kwetter.services.UserService;
import nl.shanelab.kwetter.services.exceptions.UserException;
import nl.shanelab.kwetter.services.exceptions.user.UserAlreadyExistsException;
import nl.shanelab.kwetter.services.exceptions.user.UserFollowException;
import nl.shanelab.kwetter.services.exceptions.user.UserIncorrectCredentialsException;
import nl.shanelab.kwetter.services.exceptions.user.UserNotFoundException;
import nl.shanelab.kwetter.services.tests.matcher.UserFollowExceptionMatcher;
import nl.shanelab.kwetter.util.WeldJUnit4Runner;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;

import javax.inject.Inject;

import static com.google.common.truth.Truth.assertThat;
import static com.google.common.truth.Truth.assertWithMessage;

@RunWith(WeldJUnit4Runner.class)
public class UserServiceTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Inject
    UserService userService;

    @Before
    public void shouldInjectService() {
        assertWithMessage("The service was not injected using weld-junit")
                .that(userService)
                .isNotNull();
    }

    @Test
    public void shouldRegisterSuccessfully() throws UserException {
        User user = userService.register("shouldRegisterSuccessfully", "password");

        assertThat(user.getUsername()).isEqualTo("shouldRegisterSuccessfully");
    }

    @Test(expected = UserAlreadyExistsException.class)
    public void shouldNotRegister() throws UserException {
        User user = userService.register("shouldNotRegister", "password");

        // register new user with same name, but with different password. Should throw an UserAlreadyExistsException
        userService.register("shouldNotRegister", "newpassword");
    }

    @Test
    public void shouldSignInSuccessfully() throws UserException {
        User user = userService.register("shouldSignInSuccessfully", "password");

        User signedInUser = userService.authenticate(user.getUsername(), user.getPassword());

        assertThat(user).isEqualTo(signedInUser);
    }

    @Test(expected = UserNotFoundException.class)
    public void shouldNotSignInWithNonExistingUser() throws UserException {
        // sign in with a non exisiting user. Should throw an UserNotFoundException
        userService.authenticate("shouldNotSignInWithNonExistingUser", "password");
    }

    @Test(expected = UserIncorrectCredentialsException.class)
    public void shouldNotSignInWithWrongCredentials() throws UserException {
        User user = userService.register("shouldNotSignInWithWrongCredentials", "password");

        // sign in with an existing username, but with the wrong password. Should throw an UserIncorrectCredentialsException
        userService.authenticate(user.getUsername(), "indifferentPassword");
    }

    @Test
    public void shouldRenameSuccessfully() throws UserException {
        User user = userService.register("shouldRenameSuccessfully", "password");

        assertThat(user).isNotNull();

        final String newName = "shouldRenameSuccessfully_renamed";

        User renamedUser = userService.rename(newName, user);

        assertWithMessage("The new name was not set")
                .that(renamedUser.getUsername())
                .isEqualTo(newName);
    }

    @Test(expected = UserAlreadyExistsException.class)
    public void shouldNotRename() throws UserException {
        User user = userService.register("shouldNotRename", "password");

        // the name the 'shouldNotRename' user wants to rename to
        final String nameHolder = "shouldNotRename_nameholder";

        userService.register("shouldNotRename_nameholder", "password");

        // rename a user to an already exising username. Should throw an UserAlreadyExistsException
        userService.rename(nameHolder, user);
    }

    @Test
    public void shouldRemoveUserSuccessfully() throws UserException {
        final String name = "shouldRemoveUserSuccessfully";

        User user = userService.register(name, "password");

        userService.remove(user);

        assertWithMessage(String.format("The user with username '%s' was not removed", name))
                .that(userService.getByUserName(name))
                .isNull();
    }

    @Test(expected = UserNotFoundException.class)
    public void shouldNotRemoveUser() throws UserException {
        User user = userService.register("shouldNotRemoveUser", "password");

        userService.remove(user);

        // remove the user again. Should throw an UserNotFoundException
        userService.remove(user);
    }

    @Test
    public void shouldBeAbleToFollowUser() throws UserException {
        User userA = userService.register("shouldBeAbleToFollowUser_A", "password");
        User userB = userService.register("shouldBeAbleToFollowUser_B", "password");

        userService.followUser(userA, userB);

        assertWithMessage("User B is not followed by User A")
                .that(userService.isUserFollowedBy(userB, userA))
                .isTrue();
    }

    @Test(expected = UserNotFoundException.class)
    public void shouldNotBeAbleToFollowUser() throws UserException {
        User userA = userService.register("shouldNotBeAbleToFollowUser_A", "password");
        User userB = userService.register("shouldNotBeAbleToFollowUser_B", "password");

        userService.remove(userB);

        // user to follow is removed. Should thrown an UserNotFoundException
        userService.followUser(userA, userB);
    }

    @Test
    public void shouldNotBeAbleToSelfFollow() throws UserException {
        thrown.expect(UserFollowExceptionMatcher.of(UserFollowException.FollowViolationType.SELF_FOLLOWING));

        User user = userService.register("shouldNotBeAbleToSelfFollow", "password");

        // user to follow is removed. Should thrown an UserFollowExceptionException with type SELF_FOLLOWING
        userService.followUser(user, user);
    }

    @Test
    public void shouldNotBeFollowingOtherUser() throws UserException {
        User userA = userService.register("shouldNotBeFollowingOtherUser_A", "password");
        User userB = userService.register("shouldNotBeFollowingOtherUser_B", "password");
        User userC = userService.register("shouldNotBeFollowingOtherUser_C", "password");

        userService.followUser(userA, userB);

        assertWithMessage("User B is not followed by User A")
                .that(userService.isUserFollowedBy(userB, userA))
                .isTrue();

        assertWithMessage("User A is not following User B")
                .that(userService.isUserFollowing(userA, userB))
                .isTrue();

        userService.followUser(userC, userA);

        assertWithMessage("User C is not following User A")
                .that(userService.isUserFollowing(userC, userA))
                .isTrue();

        thrown.expect(UserFollowExceptionMatcher.of(UserFollowException.FollowViolationType.NOT_FOLLOWING));

        // user B has followers, but user C is not following user B. Should throw an UserFollowExceptionException with type NOT_FOLLOWING
        userService.isUserFollowing(userC, userB);
    }

    @Test
    public void shouldNotBeFollowedByOtherUser() throws UserException {
        // same test as UserServiceTest::shouldNotBeFollowingOtherUser, but checking for different FollowViolationType
        User userA = userService.register("shouldNotBeFollowedByOtherUser_A", "password");
        User userB = userService.register("shouldNotBeFollowedByOtherUser_B", "password");
        User userC = userService.register("shouldNotBeFollowedByOtherUser_C", "password");

        userService.followUser(userA, userB);

        assertWithMessage("User B is not followed by User A")
                .that(userService.isUserFollowedBy(userB, userA))
                .isTrue();

        assertWithMessage("User A is not following User B")
                .that(userService.isUserFollowing(userA, userB))
                .isTrue();

        userService.followUser(userC, userA);

        assertWithMessage("User C is not following User A")
                .that(userService.isUserFollowing(userC, userA))
                .isTrue();

        thrown.expect(UserFollowExceptionMatcher.of(UserFollowException.FollowViolationType.NOT_FOLLOWED_BY));

        // user B has followers, but user C is not following user B. Should throw an UserFollowExceptionException with type NOT_FOLLOWING
        userService.isUserFollowedBy(userB, userC);
    }
}
