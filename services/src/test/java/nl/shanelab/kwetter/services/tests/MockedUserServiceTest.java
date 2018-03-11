package nl.shanelab.kwetter.services.tests;

import nl.shanelab.kwetter.dal.domain.User;
import nl.shanelab.kwetter.services.exceptions.UserException;
import nl.shanelab.kwetter.services.impl.UserServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Collection;
import java.util.Map;

import static com.google.common.truth.Truth.assertWithMessage;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.times;

@RunWith(MockitoJUnitRunner.Silent.class)
public class MockedUserServiceTest extends BaseMockedServiceTest {

    @InjectMocks
    UserServiceImpl userService;

    private Map<Long, User> users;

    @Before
    public void onSetup() {
        super.onSetup();

        assertWithMessage("The service was not mocked/injected")
                .that(userService)
                .isNotNull();
    }

    @Test
    public void getById() {
        mocks();

        User u = userService.getById(anyLong());

        assertWithMessage("The expected user was not invoked when given any long")
                .that(u)
                .isNotNull();

        Mockito.verify(userDaoMock).find(anyLong());
    }

    @Test
    public void getUserByName() {
        mocks();

        User u = userService.getByUserName(anyString());

        assertWithMessage("The expected user was not invoked when given any string")
                .that(u)
                .isNotNull();

        Mockito.verify(userDaoMock).getByUsername(anyString());
    }

    @Test
    public void setBiography() throws UserException {
        mocks();

        User u = userService.getByUserName(anyString());

        User edited = userService.setBiography(anyString(), u);

        assertWithMessage("The returned user from the change differs from the fetched user by name")
                .that(u)
                .isEqualTo(edited);

        Mockito.verify(userDaoMock, times(1)).edit(any());
    }

    @Test
    public void getAllUsers() {
        mocks();

        Collection<User> users = userService.getAllUsers();

        assertWithMessage("No collection was returned")
                .that(users.size())
                .isGreaterThan(0);

        Mockito.verify(userDaoMock).findAll();
    }
}
