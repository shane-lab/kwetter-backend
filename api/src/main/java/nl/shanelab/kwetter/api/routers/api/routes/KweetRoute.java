package nl.shanelab.kwetter.api.routers.api.routes;

import nl.shanelab.kwetter.api.BaseRoute;
import nl.shanelab.kwetter.api.dto.KweetDto;
import nl.shanelab.kwetter.api.hateoas.routelinks.HashTagRouteLinks;
import nl.shanelab.kwetter.api.hateoas.routelinks.KweetRouteLinks;
import nl.shanelab.kwetter.api.mappers.KweetMapper;
import nl.shanelab.kwetter.api.qualifiers.Jwt;
import nl.shanelab.kwetter.dal.dao.Pagination;
import nl.shanelab.kwetter.dal.domain.HashTag;
import nl.shanelab.kwetter.dal.domain.Kweet;
import nl.shanelab.kwetter.dal.domain.User;
import nl.shanelab.kwetter.services.KweetingService;
import nl.shanelab.kwetter.services.UserService;
import nl.shanelab.kwetter.services.exceptions.KweetException;
import nl.shanelab.kwetter.services.exceptions.UserException;
import nl.shanelab.kwetter.services.exceptions.kweet.KweetFavouriteException;
import nl.shanelab.kwetter.services.exceptions.kweet.KweetNotFoundException;
import nl.shanelab.kwetter.services.exceptions.user.UserNotFoundException;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.stream.Collectors;

@Path("/kweets")
@Produces(MediaType.APPLICATION_JSON)
@RequestScoped
public class KweetRoute extends BaseRoute {

    @Inject
    KweetingService kweetingService;

    @Inject
    UserService userService;

    @GET
    @Path(KweetRouteLinks.Constants.LIST_KWEETS)
    public Response getAllKweets(@QueryParam("page") int page, @QueryParam("size") int size) {
        Pagination<Kweet> pagination = kweetingService.getAllKweets(page, size);

        return paginated(
                pagination.getPage(),
                pagination.getRequestedSize(),
                pagination.pages(),
                pagination.hasPrevious(),
                pagination.hasNext(), pagination.getCollection().stream()
                .map(this::mapKweetWithLinks)
                .collect(Collectors.toList()));
    }

    @POST
    @Path(KweetRouteLinks.Constants.CREATE_KWEET)
    @Jwt
    public Response createKweet(
            @Valid
            @Size(max = 144, message = "Exceeding the character length limit of {max} of a Kweet is not allowed")
            @NotBlank(message = "The Kweet message can not be left empty") String message) throws UserException {
        String name = securityContext.getUserPrincipal().getName();
        User user = userService.getByUserName(name);

        Kweet kweet = kweetingService.createKweet(message, user);

        return ok(mapKweetWithLinks(kweet));
    }

    @GET
    @Path(KweetRouteLinks.Constants.FETCH_KWEET)
    public Response getKweet(@Valid @PathParam("id") long id) throws KweetException {
        Kweet kweet = kweetingService.getKweetById(id);

        if (kweet == null) {
            throw new KweetNotFoundException(id);
        }

        return ok(mapKweetWithLinks(kweet));
    }

    @DELETE
    @Path(KweetRouteLinks.Constants.DELETE_KWEET)
    @Jwt
    public Response removeKweet(@Valid @PathParam("id") long id) throws KweetException {
        Kweet kweet = kweetingService.getKweetById(id);

        String name = securityContext.getUserPrincipal().getName();
        User user = userService.getByUserName(name);

        if (!kweet.getAuthor().equals(user)) {
            return nok("The Kweet to remove does not belong to the user");
        }

        kweetingService.removeKweet(id);

        return ok();
    }

    @POST
    @Path(KweetRouteLinks.Constants.FAVORITE_KWEET)
    @Jwt
    public Response favoriteKweet(@Valid @PathParam("id") long id) throws KweetException, UserException {
        Kweet kweet = kweetingService.getKweetById(id);

        if (kweet == null) {
            throw new KweetNotFoundException(id);
        }

        String name = securityContext.getUserPrincipal().getName();
        User user = userService.getByUserName(name);

        kweetingService.favouriteKweet(kweet, user);

        return ok();
    }

    @DELETE
    @Path(KweetRouteLinks.Constants.UNFAVORITE_KWEET)
    @Jwt
    public Response unFavoriteKweet(@Valid @PathParam("id") long id) throws KweetException, UserException {
        Kweet kweet = kweetingService.getKweetById(id);

        if (kweet == null) {
            throw new KweetNotFoundException(id);
        }

        String name = securityContext.getUserPrincipal().getName();
        User user = userService.getByUserName(name);

        kweetingService.unFavouriteKweet(kweet, user);

        return ok();
    }

    @GET
    @Path(KweetRouteLinks.Constants.IS_KWEET_FAVORITED_BY)
    public Response isFavoritedBy(@Valid @PathParam("id") long id, @Valid @PathParam("idOrName") String idOrName) throws KweetException, UserException {
        Kweet kweet = kweetingService.getKweetById(id);

        if (kweet == null) {
            throw new KweetNotFoundException(id);
        }

        Long userId = null;
        try {
            userId = Long.parseLong(idOrName);
        } catch (Exception e) { }
        User user = userId != null ? userService.getById(userId) : userService.getByUserName(idOrName);

        if (user == null) {
            throw new UserNotFoundException();
        }

        boolean flag = false;
        try {
            flag = kweetingService.isKweetFavoritedByUser(kweet, user);
        } catch (KweetFavouriteException e) { }

        return Response.ok().status(Response.Status.OK).entity(flag).build();
    }

    @GET
    @Path(KweetRouteLinks.Constants.FETCH_KWEETS_CREATED_BY)
    public Response getKweetsByUser(@Valid @PathParam("idOrName") String idOrName, @QueryParam("page") int page, @QueryParam("size") int size) throws UserException {
        Long id = null;
        try {
            id = Long.parseLong(idOrName);
        } catch (Exception e) { }
        User user = id != null ? userService.getById(id) : userService.getByUserName(idOrName);

        Pagination<Kweet> pagination = kweetingService.getKweetsByUser(user, page, size);

        return paginated(
                pagination.getPage(),
                pagination.getRequestedSize(),
                pagination.pages(),
                pagination.hasPrevious(),
                pagination.hasNext(), pagination.getCollection().stream()
                .map(this::mapKweetWithLinks)
                .collect(Collectors.toList()));
    }

    @GET
    @Path(KweetRouteLinks.Constants.FETCH_TIMELINE)
    public Response getTimeline(@Valid @PathParam("idOrName") String idOrName, @QueryParam("page") int page, @QueryParam("size") int size) throws UserException {
        Long id = null;
        try {
            id = Long.parseLong(idOrName);
        } catch (Exception e) { }
        User user = id != null ? userService.getById(id) : userService.getByUserName(idOrName);

        Pagination<Kweet> pagination = kweetingService.getTimeline(user.getUsername(), page, size);

        return paginated(
                pagination.getPage(),
                pagination.getRequestedSize(),
                pagination.pages(),
                pagination.hasPrevious(),
                pagination.hasNext(), pagination.getCollection().stream()
                .map(this::mapKweetWithLinks)
                .collect(Collectors.toList()));
    }

    @GET
    @Path(KweetRouteLinks.Constants.FETCH_KWEETS_FAVORITED_BY)
    public Response getFavorites(@Valid @PathParam("idOrName") String idOrName, @QueryParam("page") int page, @QueryParam("size") int size) throws UserException {
        Long id = null;
        try {
            id = Long.parseLong(idOrName);
        } catch (Exception e) { }
        User user = id != null ? userService.getById(id) : userService.getByUserName(idOrName);

        Pagination<Kweet> pagination = kweetingService.getFavouritedKweets(user, page, size);

        return paginated(
                pagination.getPage(),
                pagination.getRequestedSize(),
                pagination.pages(),
                pagination.hasPrevious(),
                pagination.hasNext(), pagination.getCollection().stream()
                .map(this::mapKweetWithLinks)
                .collect(Collectors.toList()));
    }

    @GET
    @Path(KweetRouteLinks.Constants.FIND_KWEETS)
    public Response getKweetsWithHashTagName(@Valid @PathParam("name") String name, @QueryParam("page") int page, @QueryParam("size") int size) {
        Pagination<Kweet> pagination = kweetingService.getKweetsWithHashTagName(name, page, size);

        return paginated(
                pagination.getPage(),
                pagination.getRequestedSize(),
                pagination.pages(),
                pagination.hasPrevious(),
                pagination.hasNext(), pagination.getCollection().stream()
                .map(this::mapKweetWithLinks)
                .collect(Collectors.toList()));
    }

    private KweetDto mapKweetWithLinks(Kweet kweet) {
        KweetDto dto = KweetMapper.INSTANCE.kweetToDto(kweet);

        String base = uriInfo.getBaseUri().toString();
        long id = kweet.getId();
        String author = kweet.getAuthor().getUsername();

        dto.add(KweetRouteLinks.FETCH_KWEET.asLinked(base, id));
        if (kweet.getHashTags().size() > 0) {
            for (HashTag hashTag: kweet.getHashTags()) {
                String tagName = hashTag.getName();
                dto.add(KweetRouteLinks.FIND_KWEETS.asLinked(base, tagName));
                dto.add(HashTagRouteLinks.FETCH_HASHTAG.asLinked(base, tagName));
            }
        }

        if (isAuthenticated(author)) {
            dto.add(KweetRouteLinks.DELETE_KWEET.asLinked(base, id));
            dto.add(KweetRouteLinks.FAVORITE_KWEET.asLinked(base, id));
            dto.add(KweetRouteLinks.UNFAVORITE_KWEET.asLinked(base, id));
            dto.add(KweetRouteLinks.IS_KWEET_FAVORITED_BY.asLinked(base, id, author));
            dto.add(KweetRouteLinks.FETCH_KWEETS_CREATED_BY.asLinked(base, author));
            dto.add(KweetRouteLinks.FETCH_KWEETS_FAVORITED_BY.asLinked(base, author));
        }

        return dto;
    }

}
