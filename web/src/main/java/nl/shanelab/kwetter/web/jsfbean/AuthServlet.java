package nl.shanelab.kwetter.web.jsfbean;

import lombok.Data;
import nl.shanelab.kwetter.dal.domain.Role;
import nl.shanelab.kwetter.dal.domain.User;
import nl.shanelab.kwetter.services.UserService;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

@Named
@SessionScoped
@Data
public class AuthServlet implements JsfBeanServlet {

    @Inject
    private UserService userService;

    public boolean isModerator() {
        return isSignedIn() && getRequest().isUserInRole("Moderator");
    }

    public boolean isModerator(long id) {
        User user = userService.getById(id);
        if (user == null) {
            return false;
        }

        return user.getRole() == Role.ADMINISTRATOR;
    }

    public boolean isAdministrator() {
        return isSignedIn() && getRequest().isUserInRole("Administrator");
    }

    public boolean isAdministrator(long id) {
        User user = userService.getById(id);
        if (user == null) {
            return false;
        }

        return user.getRole() == Role.ADMINISTRATOR;
    }

    public boolean isSignedIn() {
        return getRequest().getUserPrincipal() != null;
    }
}
