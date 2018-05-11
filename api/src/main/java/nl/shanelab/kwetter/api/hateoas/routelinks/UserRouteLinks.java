package nl.shanelab.kwetter.api.hateoas.routelinks;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import nl.shanelab.kwetter.api.hateoas.Methods;
import nl.shanelab.kwetter.api.hateoas.RouteLink;

@RequiredArgsConstructor
@Getter
public enum UserRouteLinks implements RouteLink {
    LIST_USERS("list", "Get a list of users", Constants.LIST_USERS, Methods.GET, 0, false),
    CREATE_USER("create", "Create a new user", Constants.CREATE_USER, Methods.POST, 0, false),
    UPDATE_USER("update", "Update user info", Constants.UPDATE_USER, Methods.PUT, 0, true),
    FETCH_USER("fetch", "Find a user by their exact id or username", Constants.FETCH_USER, Methods.GET, 1, false),
    FIND_USERS("find", "Find a user by their partial name", Constants.FIND_USER, Methods.GET, 1, false),
    FETCH_USER_FOLLOWERS("followers", "Get a list of followers", Constants.FETCH_USER_FOLLOWERS, Methods.GET, 1, false),
    FETCH_USER_FOLLOWINGS("following", "Get a list of followings", Constants.FETCH_USER_FOLLOWINGS, Methods.GET, 1, false),
    CREATE_USER_FOLLOW("follow.create", "Create a follow", Constants.CREATE_USER_FOLLOW, Methods.POST, 1, true),
    DELETE_USER_FOLLOW("follow.delete", "Remove a follow", Constants.DELETE_USER_FOLLOW, Methods.DELETE, 1, true),
    CHECK_USER_FOLLOWING("follow.check", "Check if auser is following another user", Constants.CHECK_USER_FOLLOWING, Methods.GET, 2, false),
    CREATE_USER_AVATAR("avatar.create", "Upload a profile picture", Constants.CREATE_USER_AVATAR, Methods.POST, 0, true),
    FETCH_USER_AVATAR("avatar.fetch", "Get the profile picture", Constants.FETCH_USER_AVATAR, Methods.GET, 1, false),
    DELETE_USER_AVATAR("avatar.delete", "Remove the profile picture", Constants.DELETE_USER_AVATAR, Methods.DELETE, 0, true);

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
        return "users";
    }

    @NoArgsConstructor
    public static class Constants {
        public static final String LIST_USERS = "/";
        public static final String CREATE_USER = "/";
        public static final String UPDATE_USER = "/";
        public static final String FETCH_USER = "/{idOrName}";
        public static final String FIND_USER = "/partial/{name}";
        public static final String FETCH_USER_FOLLOWERS = "/{idOrName}/followers";
        public static final String FETCH_USER_FOLLOWINGS = "/{idOrName}/following";
        public static final String CREATE_USER_FOLLOW = "/follow/{idOrName}";
        public static final String DELETE_USER_FOLLOW = "/follow/{idOrName}";
        public static final String CHECK_USER_FOLLOWING = "/{idOrNameA}/follows/{idOrNameB}";
        public static final String FETCH_USER_AVATAR = "/{idOrName}/avatar";
        public static final String CREATE_USER_AVATAR = "/avatar";
        public static final String DELETE_USER_AVATAR = "/avatar";
    }
}