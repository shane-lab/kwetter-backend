package nl.shanelab.kwetter.api.hateoas.routelinks;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import nl.shanelab.kwetter.api.hateoas.Methods;
import nl.shanelab.kwetter.api.hateoas.RouteLink;

@RequiredArgsConstructor
@Getter
public enum KweetRouteLinks implements RouteLink {
    LIST_KWEETS("list", "Get a list of Kweets", Constants.LIST_KWEETS, Methods.GET, 0, false),
    CREATE_KWEET("create", "Create a new Kweet", Constants.CREATE_KWEET, Methods.POST, 0, true),
    FETCH_KWEET("fetch", "Find a Kweet by its id", Constants.FETCH_KWEET, Methods.GET, 1, false),
    DELETE_KWEET("delete", "Delete the Kweet", Constants.DELETE_KWEET, Methods.DELETE, 1,true),
    FAVORITE_KWEET("favorite", "Favorite the Kweet", Constants.FAVORITE_KWEET, Methods.POST, 1, true),
    UNFAVORITE_KWEET("unfavorite", "Unfavorite the Kweet", Constants.UNFAVORITE_KWEET, Methods.DELETE, 1, true),
    IS_KWEET_FAVORITED_BY("favorited.check", "Checks if user has favorited the Kweet", Constants.IS_KWEET_FAVORITED_BY, Methods.GET, 2, false),
    FETCH_KWEETS_CREATED_BY("created_by", "Get a list of created Kweets", Constants.FETCH_KWEETS_CREATED_BY, Methods.GET, 1, false),
    FETCH_KWEETS_FAVORITED_BY("favorited.list", "Get a list of favorited Kweets}", Constants.FETCH_KWEETS_FAVORITED_BY, Methods.GET, 1, false),
    FETCH_TIMELINE("timeline", "Get the timeline", Constants.FETCH_TIMELINE, Methods.GET, 1, false),
    FIND_KWEETS("find", "Find a list of Kweets with the exact hashtag name", Constants.FIND_KWEETS, Methods.GET, 1, false);

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
    @NonNull
    boolean authRequired;

    @Override
    public String getRoute() {
        return "kweets";
    }

    @NoArgsConstructor
    public static class Constants {
        public static final String LIST_KWEETS = "/";
        public static final String CREATE_KWEET = "/";
        public static final String DELETE_KWEET = "/{id}";
        public static final String FETCH_KWEET = "/{id}";
        public static final String FAVORITE_KWEET = "/favorite/{id}";
        public static final String UNFAVORITE_KWEET = "/favorite/{id}";
        public static final String IS_KWEET_FAVORITED_BY = "/{id}/favorited/{idOrName}";
        public static final String FETCH_KWEETS_CREATED_BY = "/user/{idOrName}";
        public static final String FETCH_KWEETS_FAVORITED_BY = "/user/{idOrName}/favorites";
        public static final String FETCH_TIMELINE = "/user/{idOrName}/timeline";
        public static final String FIND_KWEETS = "/hashtag/{name}";
    }
}