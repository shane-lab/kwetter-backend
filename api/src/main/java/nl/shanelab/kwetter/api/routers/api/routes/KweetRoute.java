package nl.shanelab.kwetter.api.routers.api.routes;

import lombok.NoArgsConstructor;
import nl.shanelab.kwetter.api.BaseRoute;
import nl.shanelab.kwetter.api.dto.KweetDto;
import nl.shanelab.kwetter.api.mappers.KweetMapper;
import nl.shanelab.kwetter.api.qualifiers.Jwt;
import nl.shanelab.kwetter.dal.dao.Pagination;
import nl.shanelab.kwetter.dal.domain.Kweet;
import nl.shanelab.kwetter.dal.domain.User;
import nl.shanelab.kwetter.services.KweetingService;
import nl.shanelab.kwetter.services.UserService;
import nl.shanelab.kwetter.services.exceptions.KweetException;
import nl.shanelab.kwetter.services.exceptions.UserException;
import nl.shanelab.kwetter.services.exceptions.kweet.KweetNotFoundException;

import javax.annotation.PostConstruct;
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
    @Path("/")
    public Response getAllKweets(@QueryParam("page") int page, @QueryParam("size") int size) {
        Pagination<Kweet> pagination = kweetingService.getAllKweets(page, size);

        return paginated(
                pagination.getPage(),
                pagination.getRequestedSize(),
                pagination.pages(),
                pagination.hasPrevious(),
                pagination.hasNext(), pagination.getCollection().stream()
                .map(KweetMapper.INSTANCE::kweetToDto)
                .collect(Collectors.toList()));
    }

    @POST
    @Path("/")
    @Jwt
    public Response createKweet(
            @Valid
            @Size(max = 144, message = "Exceeding the character length limit of {max} of a Kweet is not allowed")
            @NotBlank(message = "The Kweet message can not be left empty") String message) throws UserException {
        String name = securityContext.getUserPrincipal().getName();
        User user = userService.getByUserName(name);

        Kweet kweet = kweetingService.createKweet(message, user);

        return ok(KweetMapper.INSTANCE.kweetToDto(kweet));
    }

    @GET
    @Path("/{id}")
    public Response getKweet(@Valid @PathParam("id") long id) throws KweetException {
        Kweet kweet = kweetingService.getKweetById(id);

        if (kweet == null) {
            throw new KweetNotFoundException(id);
        }

        return ok(KweetMapper.INSTANCE.kweetToDto(kweet));
    }

    @DELETE
    @Path("/{id}")
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
    @Path("/favorite/{id}")
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
    @Path("/favorite/{id}")
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
    @Path("/user/{idOrName}")
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
                .map(KweetMapper.INSTANCE::kweetToDto)
                .collect(Collectors.toList()));
    }

    @GET
    @Path("/user/{idOrName}/timeline")
    public Response getTimeline(@Valid @PathParam("idOrName") String idOrName, @QueryParam("page") int page, @QueryParam("size") int size) throws UserException {
        Long id = null;
        try {
            id = Long.parseLong(idOrName);
        } catch (Exception e) { }
        User user = id != null ? userService.getById(id) : userService.getByUserName(idOrName);

        Pagination<Kweet> pagination = kweetingService.getTimelineByUserId(user.getId(), page, size);

        return paginated(
                pagination.getPage(),
                pagination.getRequestedSize(),
                pagination.pages(),
                pagination.hasPrevious(),
                pagination.hasNext(), pagination.getCollection().stream()
                .map(KweetMapper.INSTANCE::kweetToDto)
                .collect(Collectors.toList()));
    }

    @GET
    @Path("/user/{idOrName}/favorites")
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
                        .map(KweetMapper.INSTANCE::kweetToDto)
                        .collect(Collectors.toList()));
    }

    @GET
    @Path("/hashtag/{name}")
    public Response getKweetsWithHashTagName(@Valid @PathParam("name") String name, @QueryParam("page") int page, @QueryParam("size") int size) {
        Pagination<Kweet> pagination = kweetingService.getKweetsWithHashTagName(name, page, size);

        return paginated(
                pagination.getPage(),
                pagination.getRequestedSize(),
                pagination.pages(),
                pagination.hasPrevious(),
                pagination.hasNext(), pagination.getCollection().stream()
                .map(KweetMapper.INSTANCE::kweetToDto)
                .collect(Collectors.toList()));
    }

}
