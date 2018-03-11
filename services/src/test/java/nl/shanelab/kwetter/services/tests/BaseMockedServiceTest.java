package nl.shanelab.kwetter.services.tests;

import nl.shanelab.kwetter.dal.dao.UserDao;
import nl.shanelab.kwetter.dal.domain.Role;
import nl.shanelab.kwetter.dal.domain.User;
import org.junit.Before;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

public abstract class BaseMockedServiceTest {

    @Mock
    UserDao userDaoMock;

    Map<Long, User> users;

    @Before
    public void onSetup() {
        users = new HashMap<>();

        User u = new User("AnonymousUser", "password", Role.USER);
        u.setId(0);
        users.put( u.getId(), u);

        MockitoAnnotations.initMocks(this);
    }

    protected void mocks() {
        when(userDaoMock.create(any(User.class))).thenReturn(users.get((long) 0));
        when(userDaoMock.edit(any(User.class))).thenReturn(users.get((long) 0));
        when(userDaoMock.getByUsername(anyString())).thenReturn(users.get((long) 0));
        when(userDaoMock.find(anyLong())).thenReturn(users.get((long) 0));
        when(userDaoMock.findAll()).thenReturn(users.values());
    }
}
