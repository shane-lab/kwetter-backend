package nl.shanelab.kwetter.services.tests;

import nl.shanelab.kwetter.dal.domain.User;
import nl.shanelab.kwetter.services.UserService;
import nl.shanelab.kwetter.services.exceptions.UserAlreadyExistsException;
import nl.shanelab.kwetter.services.exceptions.UserException;
import nl.shanelab.kwetter.services.exceptions.UserIncorrectCredentialsException;
import nl.shanelab.kwetter.services.exceptions.UserNotFoundException;
import nl.shanelab.kwetter.util.WeldJUnit4Runner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;

import static com.google.common.truth.Truth.assertThat;
import static com.google.common.truth.Truth.assertWithMessage;

@RunWith(WeldJUnit4Runner.class)
public class UserServiceTest {

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

        User signedInUser = userService.signIn(user.getUsername(), user.getPassword());

        assertThat(user).isEqualTo(signedInUser);
    }

    @Test(expected = UserNotFoundException.class)
    public void shouldNotSignInWithNonExistingUser() throws UserException {
        // sign in with a non exisiting user. Should throw an UserNotFoundException
        userService.signIn("shouldNotSignInWithNonExistingUser", "password");
    }

    @Test(expected = UserIncorrectCredentialsException.class)
    public void shouldNotSignInWithWrongCredentials() throws UserException {
        User user = userService.register("shouldNotSignInWithWrongCredentials", "password");

        // sign in with an existing username, but with the wrong password. Should throw an UserIncorrectCredentialsException
        userService.signIn(user.getUsername(), "indifferentPassword");
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
}
