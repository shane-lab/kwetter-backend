package nl.shanelab.kwetter.web.jsfbean;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;

public interface JsfBeanServlet extends Serializable {

    default String redirect(String location) {
        return redirect(location, true);
    }

    default String redirect(String location, boolean flag) {
        return String.format("%s?faces-redirect=%b", location, flag);
    }

    default HttpServletRequest getRequest() {
        return (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
    }
}
