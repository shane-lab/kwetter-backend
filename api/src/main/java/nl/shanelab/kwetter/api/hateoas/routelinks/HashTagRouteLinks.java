package nl.shanelab.kwetter.api.hateoas.routelinks;

import lombok.*;
import nl.shanelab.kwetter.api.hateoas.Methods;
import nl.shanelab.kwetter.api.hateoas.RouteLink;

@RequiredArgsConstructor
@Getter
public enum HashTagRouteLinks implements RouteLink {
    LIST_HASHTAGS("list", "Get a list of hashtags", Constants.LIST_HASHTAGS, Methods.GET, 0),
    FETCH_HASHTAG("fetch", "Find a hashtag by its id", Constants.FETCH_HASHTAG, Methods.GET, 1),
    FETCH_TRENDING("trending", "Find all trending hashtags by date", Constants.FETCH_TRENDING, Methods.GET, 1);

    @NonNull
    String key;
    @NonNull
    String title;
    @NonNull
    String path;
    @NonNull
    Methods method;
    @NonNull
    int requiredPathParams;

    @Override
    public String getRoute() {
        return "hashtags";
    }

    @Override
    public boolean isAuthRequired() {
        return false;
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Constants {
        public static final String LIST_HASHTAGS = "/";
        public static final String FETCH_HASHTAG = "/{id}";
        public static final String FETCH_TRENDING = "/trending/{date}";
    }
}
