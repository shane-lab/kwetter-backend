package nl.shanelab.kwetter.api.routers.api.routes;

import nl.shanelab.kwetter.api.BaseRoute;
import nl.shanelab.kwetter.api.dto.UserDto;
import nl.shanelab.kwetter.api.hateoas.routelinks.KweetRouteLinks;
import nl.shanelab.kwetter.api.hateoas.routelinks.UserRouteLinks;
import nl.shanelab.kwetter.api.mappers.UserMapper;
import nl.shanelab.kwetter.dal.domain.User;

public abstract class UserBaseRoute extends BaseRoute {
    protected UserDto mapUserWithLinks(User user) {
        return mapUserWithLinks(user, false);
    }

    protected UserDto mapUserWithLinks(User user, boolean overrideAuthCheck) {
        UserDto dto = UserMapper.INSTANCE.userAsDTO(user);

        String base = uriInfo.getBaseUri().toString();
        String name = user.getUsername();

        // public users route paths
        dto.add(UserRouteLinks.FETCH_USER.asLinked(base, name));
        dto.add(UserRouteLinks.FETCH_USER_FOLLOWERS.asLinked(base, name));
        dto.add(UserRouteLinks.FETCH_USER_FOLLOWINGS.asLinked(base, name));
        dto.add(UserRouteLinks.FETCH_USER_AVATAR.asLinked(base, name));
        // public kweets route paths (user related)
        dto.add(KweetRouteLinks.FETCH_KWEETS_CREATED_BY.asLinked(base, name));
        dto.add(KweetRouteLinks.FETCH_KWEETS_FAVORITED_BY.asLinked(base, name));
        dto.add(KweetRouteLinks.FETCH_TIMELINE.asLinked(base, name));

        // protected authenticated route paths
        if (overrideAuthCheck || isAuthenticated(name)) {
            dto.add(UserRouteLinks.UPDATE_USER.asLinked(base));
            dto.add(UserRouteLinks.CREATE_USER_AVATAR.asLinked(base));
            dto.add(UserRouteLinks.DELETE_USER_AVATAR.asLinked(base));
            dto.add(KweetRouteLinks.CREATE_KWEET.asLinked(base));
        }

        return dto;
    }
}
