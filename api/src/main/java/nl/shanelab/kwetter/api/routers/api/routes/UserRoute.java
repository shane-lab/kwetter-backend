package nl.shanelab.kwetter.api.routers.api.routes;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.NoArgsConstructor;
import nl.shanelab.kwetter.api.dto.UserDto;
import nl.shanelab.kwetter.api.mappers.UserMapper;
import nl.shanelab.kwetter.api.routers.BaseRoute;
import nl.shanelab.kwetter.dal.domain.User;
import nl.shanelab.kwetter.services.UserService;
import nl.shanelab.kwetter.services.exceptions.UserException;
import nl.shanelab.kwetter.services.exceptions.user.UserNotFoundException;
import nl.shanelab.kwetter.util.Patterns;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.validation.Valid;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.HashSet;
import java.util.Set;

@Path("/users")
@Produces(MediaType.APPLICATION_JSON)
@ApplicationScoped
public class UserRoute extends BaseRoute {

    @Inject
    UserService userService;

    @GET
    @Path("/")
    public Response getUsers() {
        Set<UserDto> usersDtos = new HashSet<>();

        userService.getAllUsers().forEach(user -> usersDtos.add(UserMapper.INSTANCE.userAsDTO(user)));

        return ok(usersDtos);
    }

    @POST
    @Path("/")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createUser(@Valid UserCredentials credentials) throws UserException {
        User user = userService.register(credentials.username, credentials.password);

        UserDto userDto = UserMapper.INSTANCE.userAsDTO(user);

        return ok(userDto);
    }

    @GET
    @Path("/{id}")
    public Response getUserById(@Valid @PathParam("id") long id) throws UserNotFoundException {
        User user = userService.getById(id);
        if (user == null) {
            throw new UserNotFoundException(id);
        }

        UserDto userDto = UserMapper.INSTANCE.userAsDTO(user);

        return ok(userDto);
    }

    @PUT
    @Path("/{id}")
    public Response editUserById(@Valid @PathParam("id") long id, @Valid UpdateUser updateUser) throws UserException {
        User user = userService.getById(id);
        if (user == null) {
            throw new UserNotFoundException(id);
        }

        if (updateUser.username != null && userService.getByUserName(updateUser.username) == null) {
            user = userService.rename(updateUser.username, user);
        }

        if (updateUser.bio != null) {
            user = userService.setBiography(updateUser.bio, user);
        }

        UserDto userDto = UserMapper.INSTANCE.userAsDTO(user);

        return ok(userDto);
    }

    @DELETE
    @Path("/{id}")
    public Response removeUserById(@Valid @PathParam("id") long id) throws UserException {
        User user = userService.getById(id);
        if (user != null) {
            userService.remove(user);
        }

        // TODO create success response object/model?
        return Response.ok().build();
    }

    @POST
    @Path("/{id}/follow/{id1}")
    public Response followUser(@Valid @PathParam("id") long id, @Valid @PathParam("id1") long id1) throws UserException {
        User userA = userService.getById(id);
        User userB = userService.getById(id1);

        userService.followUser(userA, userB);

        return Response.ok().build();
    }

    @DELETE
    @Path("/{id}/follow/{id1}")
    public Response unFollowUser(@Valid @PathParam("id") long id, @Valid @PathParam("id1") long id1) throws UserException {
        User userA = userService.getById(id);
        User userB = userService.getById(id1);

        userService.unFollowUser(userA, userB);

        return Response.ok().build();
    }

    @GET
    @Path("/{id}/follows/{id1}")
    public Response follows(@Valid @PathParam("id") long id, @Valid @PathParam("id1") long id1) throws UserException {
        User userA = userService.getById(id);
        User userB = userService.getById(id1);

        return Response.ok().status(userService.isUserFollowing(userA, userB) ? Response.Status.OK : Response.Status.BAD_REQUEST).build();
    }

    @NoArgsConstructor
    public static class UserCredentials {
        @Pattern(regexp = Patterns.NO_SPACES_PATTERN)
        public String username;

        @Pattern(regexp = Patterns.NO_SPACES_PATTERN)
        public String password;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    @NoArgsConstructor
    public static class UpdateUser extends UserCredentials {
        @Size(max = 160, message = "Exceeding the biography limit of {max} is not allowed")
        public String bio;

    }

}
