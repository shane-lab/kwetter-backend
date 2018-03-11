package nl.shanelab.kwetter.services.tests;

import nl.shanelab.kwetter.dal.dao.UserDao;
import nl.shanelab.kwetter.dal.domain.Role;
import nl.shanelab.kwetter.dal.domain.User;
import nl.shanelab.kwetter.services.exceptions.UserException;
import nl.shanelab.kwetter.services.impl.UserServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static com.google.common.truth.Truth.assertWithMessage;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.Silent.class)
public class MockedUserServiceTest {

    @Mock
    UserDao userDaoMock;

    @InjectMocks
    UserServiceImpl userService;

    private Map<Long, User> users;

    @Before
    public void onSetup() {
        users = new HashMap<>();

        User u = new User("AnonymousUser", "password", Role.USER);
        u.setId(0);
        users.put( u.getId(), u);

        MockitoAnnotations.initMocks(this);

        assertWithMessage("The service was not mocked/injected")
                .that(userService)
                .isNotNull();
    }

    private void mocks() {
        when(userDaoMock.create(any(User.class))).thenReturn(users.get((long) 0));
        when(userDaoMock.edit(any(User.class))).thenReturn(users.get((long) 0));
        when(userDaoMock.getByUsername(anyString())).thenReturn(users.get((long) 0));
        when(userDaoMock.find(anyLong())).thenReturn(users.get((long) 0));
        when(userDaoMock.findAll()).thenReturn(users.values());
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
