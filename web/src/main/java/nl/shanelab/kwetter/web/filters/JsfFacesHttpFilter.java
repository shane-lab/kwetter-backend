package nl.shanelab.kwetter.web.filters;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;

public abstract class JsfFacesHttpFilter extends HttpFilter {

    protected boolean isSignedIn(ServletRequest request) {
        return request instanceof HttpServletRequest ? isSignedIn((HttpServletRequest) request) : false;
    }

    protected boolean isSignedIn(HttpServletRequest request) {
        return request.getUserPrincipal() != null;
    }

    protected String getContextPath(ServletRequest request) {
        return request instanceof HttpServletRequest ? ((HttpServletRequest)request).getContextPath() : request.getServletContext().getContextPath();
    }

    protected String createPath(ServletRequest request, String path) {
        return String.format("%s/%s", getContextPath(request), path);
    }
}
