package nl.shanelab.kwetter.web.jsfbean;

import lombok.Data;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

@Named
@SessionScoped
@Data
public class AuthServlet implements JsfBeanServlet {

    private String username;

    private String password;

    public boolean isModerator() {
        return isSignedIn() && getRequest().isUserInRole("Moderator");
    }

    public boolean isAdministrator() {
        return isSignedIn() && getRequest().isUserInRole("Administrator");
    }

    public boolean isSignedIn() {
        return getRequest().getUserPrincipal() != null;
    }
}
