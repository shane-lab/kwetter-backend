package nl.shanelab.kwetter.web.jsfbean;

import lombok.Data;
import nl.shanelab.kwetter.dal.dao.Pagination;
import nl.shanelab.kwetter.dal.domain.Role;
import nl.shanelab.kwetter.dal.domain.User;
import nl.shanelab.kwetter.services.UserService;
import nl.shanelab.kwetter.services.exceptions.UserException;

import javax.annotation.security.RolesAllowed;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

@Named
@RequestScoped
@Data
public class UserServlet implements JsfBeanServlet {

    @Inject
    private UserService userService;

    public int count() {
        return userService.count();
    }

    public User getUserById(long id) {
        return userService.getById(id);
    }

    public Pagination<User> getUsers(int page) {
        return userService.getAllUsers(page);
    }

    public Pagination<User> getUsers(int page, int size) {
        return userService.getAllUsers(page, size);
    }

    public int getFollowers(long id) {
        User user = userService.getById(id);

        int size = 0;
        try {
            size = userService.getAmountOfFollowers(user);
        } catch (UserException e) { }

        return size;
    }

    public int getFollowings(long id) {
        User user = userService.getById(id);

        int size = 0;
        try {
            size = userService.getAmountOfFollowings(user);
        } catch (UserException e) { }

        return size;
    }

    @RolesAllowed({"Administrator"})
    public int setRole(long id, int current, int requested) {
        User user  = userService.getById(id);

        if (user == null) {
            return current;
        }

        try {
            userService.setRole(Role.findById(requested), user);
        } catch (Exception e) {
            return user.getRole().getId();
        }

        return requested;
    }

    @RolesAllowed({"Administrator"})
    public boolean delete(long id) {
        User user = userService.getById(id);

        if (user == null) {
            return false;
        }

        try {
            userService.remove(user);
        } catch (UserException e) {
            return false;
        }

        return true;
    }
}
