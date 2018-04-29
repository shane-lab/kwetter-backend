package nl.shanelab.kwetter.web.jsfbean;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Serializable;

public interface JsfBeanServlet extends Serializable {

    default String appRouteUri(String location) {
        return appRouteUri(location, true);
    }

    default String appRouteUri(String location, boolean flag) {
        String root = getRequest().getContextPath();
        if (location == null) {
            return "/";
        }
        if (location.startsWith("/")) {
            location = location.replaceFirst("/","");
        }

        return String.format("%s/%s?faces-redirectUri=%b", root, location, flag);
    }

    default void redirectRequest(String path) {
        try {
            this.getResponse().sendRedirect(path);
        } catch (IOException e) { }
    }

    default HttpServletRequest getRequest() {
        return (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
    }

    default HttpServletResponse getResponse() {
        return (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
    }
}
