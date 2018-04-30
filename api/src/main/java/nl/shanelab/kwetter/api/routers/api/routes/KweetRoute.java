package nl.shanelab.kwetter.api.routers.api.routes;

import lombok.NoArgsConstructor;
import nl.shanelab.kwetter.api.BaseRoute;
import nl.shanelab.kwetter.api.dto.KweetDto;
import nl.shanelab.kwetter.api.mappers.KweetMapper;
import nl.shanelab.kwetter.dal.dao.Pagination;
import nl.shanelab.kwetter.dal.domain.Kweet;
import nl.shanelab.kwetter.dal.domain.User;
import nl.shanelab.kwetter.services.KweetingService;
import nl.shanelab.kwetter.services.UserService;
import nl.shanelab.kwetter.services.exceptions.KweetException;
import nl.shanelab.kwetter.services.exceptions.UserException;
import nl.shanelab.kwetter.services.exceptions.kweet.KweetNotFoundException;

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
    public Response createKweet(@Valid KweetCreation kweetCreation) throws UserException {
        User user = userService.authenticate(kweetCreation.username, kweetCreation.password);

        Kweet kweet = kweetingService.createKweet(kweetCreation.message, user);

        KweetDto kweetDto = KweetMapper.INSTANCE.kweetToDto(kweet);

        return ok(kweetDto);
    }

    @GET
    @Path("/{id}")
    public Response getKweetById(@Valid @PathParam("id") long id) throws KweetNotFoundException {
        Kweet kweet = kweetingService.getKweetById(id);

        if (kweet == null) {
            throw new KweetNotFoundException(id);
        }

        KweetDto kweetDto = KweetMapper.INSTANCE.kweetToDto(kweet);

        return ok(kweetDto);
    }

    @DELETE
    @Path("/{id}")
    public Response removeKweetById(@Valid @PathParam("id") long id) throws KweetException {
        kweetingService.removeKweet(id);

        return Response.ok().build();
    }

    @GET
    @Path("/user/{id}")
    public Response getKweetsByUserId(@Valid @PathParam("id") long id, @QueryParam("page") int page, @QueryParam("size") int size) throws UserException {
        Pagination<Kweet> pagination = kweetingService.getKweetsByUserId(id, page, size);

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

    @NoArgsConstructor
    public static class KweetCreation extends UserRoute.UserCredentials {
        @Size(max = 144, message = "Exceeding the character length limit of {max} of a Kweet is not allowed")
        @NotBlank(message = "The Kweet message can not be left empty")
        public String message;

    }
}
