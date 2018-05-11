package nl.shanelab.kwetter.api.hateoas;

import nl.shanelab.kwetter.api.hateoas.routelinks.UserRouteLinks;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public interface RouteLink {

    String getRoute();

    String getKey();

    String getTitle();

    String getPath();

    Methods getMethod();

    int getRequiredPathParams();

    boolean isAuthRequired();

    default Linked asLinked(String base, Object ... params) {
        String path = getPath();
        if (getRequiredPathParams() > 0) {
            if (params.length < getRequiredPathParams()) {
                throw new IllegalArgumentException(String.format("Path %s requires %d url parameters", getPath(), getRequiredPathParams()));
            }
            Pattern pattern = Pattern.compile("(\\{\\w+})");
            Matcher matcher = pattern.matcher(path);

            Set<String> pathParams = new HashSet<>();

            while (matcher.find()) {
                pathParams.add(matcher.group(0));
            }

            int i = 0;
            for (String param : pathParams) {
                path = path.replace(param, params[i++].toString());
            }
        }

        String route = getRoute();
        return Linked.builder()
                .path(String.format("%s%s%s", base, route, path))
                .href(String.format("/%s%s", route, path))
                .rel(String.format("%s.%s", route, getKey()))
                .title(getTitle())
                .type(getMethod().getType())
                .requiresAuth(isAuthRequired())
                .build();
    }
}
