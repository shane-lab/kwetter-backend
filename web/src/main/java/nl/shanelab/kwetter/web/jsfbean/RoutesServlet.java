package nl.shanelab.kwetter.web.jsfbean;

import lombok.Data;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import java.util.HashMap;
import java.util.Map;

@Named
@RequestScoped
@Data
public class RoutesServlet implements JsfBeanServlet {

    public String home() {
        return fromRoute(Routes.HOME);
    }

    public String dashboard() {
        return fromRoute(Routes.DASHBOARD);
    }

    public String kweets() {
        return fromRoute(Routes.KWEETS);
    }

    public String kweets(long id) {
        return String.format("%s&id=%d",fromRoute(Routes.KWEET), id);
    }

    public String profiles() {
        return fromRoute(Routes.PROFILES);
    }

    public String profiles(long id) {
        return String.format("%s&id=%d", fromRoute(Routes.PROFILE), id);
    }

    public String login() {
        return fromRoute(Routes.LOGIN);
    }

    private String fromRoute(Routes route) {
        return appRouteUri(route.path, false);
    }

    @RequiredArgsConstructor
    public enum Routes {
        HOME("home", "/"),
        KWEET("kweet", "/dashboard/kweet.xhtml"),
        KWEETS("kweets", "/dashboard/kweets.xhtml"),
        PROFILE("profile", "/dashboard/profile.xhtml"),
        PROFILES("profiles", "/dashboard/profiles.xhtml"),
        DASHBOARD("dashboard", "/dashboard"),
        LOGIN("login", "/auth/login.xhtml");

        @NonNull
        private String key;

        @NonNull
        @Getter
        private String path;

        private static final Map<String, Routes> ROUTES_LOOKUP_TABLE = new HashMap<>();

        static {
            for (Routes route: Routes.values()) {
                ROUTES_LOOKUP_TABLE.put(route.key, route);
            }
        }

        public static Routes by(String key) {
            return ROUTES_LOOKUP_TABLE.get(key);
        }
    }

}
