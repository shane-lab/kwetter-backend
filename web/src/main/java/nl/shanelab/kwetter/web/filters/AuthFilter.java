package nl.shanelab.kwetter.web.filters;

import org.omnifaces.util.Servlets;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class AuthFilter extends JsfFacesHttpFilter {

    @Override
    public void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        boolean signedIn = isSignedIn(request);

        String location = !signedIn ? "auth/login.xhtml" : "dashboard";

        String redirectUri = createPath(request, location);

        if (request.getRequestURI().contains("/auth/")) {
            if (signedIn) {
                Servlets.facesRedirect(request, response, redirectUri);
                return;
            }

            chain.doFilter(request, response);
            return;
        }

        try {
            Servlets.facesRedirect(request, response, redirectUri);
        } catch (Exception e) {
            chain.doFilter(request, response);
        }
    }
}
