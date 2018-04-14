package nl.shanelab.kwetter.web.jsfbean;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;

public interface JsfBeanServlet extends Serializable {

    default String redirect(String location) {
        return redirect(location, true);
    }

    default String redirect(String location, boolean flag) {
        String root = getRequest().getContextPath();
        if (location == null) {
            return "/";
        }
        if (location.startsWith("/")) {
            location = location.replaceFirst("/","");
        }

        return String.format("%s/%s?faces-redirect=%b", root, location, flag);
    }

    default HttpServletRequest getRequest() {
        return (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
    }
}
