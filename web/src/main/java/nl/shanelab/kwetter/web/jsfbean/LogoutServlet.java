package nl.shanelab.kwetter.web.jsfbean;

import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import javax.servlet.ServletException;

@Named
@RequestScoped
public class LogoutServlet implements JsfBeanServlet {

    public String logout() {
        String destination = "/";

        try {
            getRequest().logout();
        } catch (ServletException e) {
            destination = "/login_error";
        }

        return appRouteUri(destination);
    }
}
