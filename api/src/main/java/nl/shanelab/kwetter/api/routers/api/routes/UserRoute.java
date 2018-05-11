package nl.shanelab.kwetter.api.routers.api.routes;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import nl.shanelab.kwetter.api.BaseRoute;
import nl.shanelab.kwetter.api.dto.UserDto;
import nl.shanelab.kwetter.api.hateoas.Methods;
import nl.shanelab.kwetter.api.hateoas.routelinks.KweetRouteLinks;
import nl.shanelab.kwetter.api.hateoas.routelinks.UserRouteLinks;
import nl.shanelab.kwetter.api.mappers.UserMapper;
import nl.shanelab.kwetter.api.qualifiers.Jwt;
import nl.shanelab.kwetter.dal.dao.Pagination;
import nl.shanelab.kwetter.dal.domain.User;
import nl.shanelab.kwetter.services.UserService;
import nl.shanelab.kwetter.services.exceptions.UserException;
import nl.shanelab.kwetter.services.exceptions.user.UserFollowException;
import nl.shanelab.kwetter.services.exceptions.user.UserNotFoundException;
import nl.shanelab.kwetter.util.Patterns;

import javax.enterprise.context.RequestScoped;
import javax.imageio.ImageIO;
import javax.inject.Inject;
import javax.validation.Valid;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Collectors;

@Path("/users")
@Produces(MediaType.APPLICATION_JSON)
@RequestScoped
public class UserRoute extends BaseRoute {

    @Inject
    UserService userService;

    private String getAvatarBaseURL() {
        return String.format("%s/WEB-INF/resources/avatars", servletContext.getRealPath("/"));
    }

    @GET
    @Path(UserRouteLinks.Constants.LIST_USERS)
    public Response getUsers(@QueryParam("page") int page, @QueryParam("size") int size) {
        Pagination<User> pagination = userService.getAllUsers(page, size);

        return paginated(
                pagination.getPage(),
                pagination.getRequestedSize(),
                pagination.pages(),
                pagination.hasPrevious(),
                pagination.hasNext(), pagination.getCollection().stream()
                .map(this::mapUserWithLinks)
                .collect(Collectors.toSet()));
    }

    @POST
    @Path(UserRouteLinks.Constants.CREATE_USER)
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createUser(@Valid UserCredentials credentials) throws UserException {
        User user = userService.register(credentials.username, credentials.password);

        return okWithJWT(mapUserWithLinks(user, true), issue(user.getUsername()));
    }

    @PUT
    @Path(UserRouteLinks.Constants.UPDATE_USER)
    @Jwt
    public Response editUser(@Valid UpdateUser updateUser) throws UserException {
        String name = securityContext.getUserPrincipal().getName();
        User user = userService.getByUserName(name);

        String newToken = null;
        if (updateUser.username != null && userService.getByUserName(updateUser.username) == null) {
            user = userService.rename(updateUser.username, user);
            newToken = issue(user.getUsername());
        }

        if (updateUser.password != null) {
            user = userService.setPassword(updateUser.password, user);
        }

        if (updateUser.bio != null) {
            user = userService.setBiography(updateUser.bio, user);
        }

        if (updateUser.location != null) {
            user = userService.setLocation(updateUser.location, user);
        }

        if (updateUser.website != null) {
            user = userService.setWebsite(updateUser.website, user);
        }

        return okWithJWT(mapUserWithLinks(user), newToken);
    }

    @GET
    @Path(UserRouteLinks.Constants.FETCH_USER)
    public Response getUser(@Valid @PathParam("idOrName") String idOrName) throws UserException {
        User user = getUserByIdOrName(idOrName);

        return ok(mapUserWithLinks(user));
    }

    @GET
    @Path(UserRouteLinks.Constants.FIND_USER)
    public Response getUsersByPartialName(@Valid @PathParam("name") String name, @QueryParam("page") int page, @QueryParam("size") int size) {
        Pagination<User> pagination = userService.getByPartialUsername(name, page, size);

        return paginated(
                pagination.getPage(),
                pagination.getRequestedSize(),
                pagination.pages(),
                pagination.hasPrevious(),
                pagination.hasNext(), pagination.getCollection().stream()
                .map(this::mapUserWithLinks)
                .collect(Collectors.toSet()));
    }

    @GET
    @Path(UserRouteLinks.Constants.FETCH_USER_FOLLOWERS)
    public Response getFollowers(@Valid @PathParam("idOrName") String idOrName, @QueryParam("page") int page, @QueryParam("size") int size) throws UserException {
        User user = getUserByIdOrName(idOrName);

        Pagination<User> pagination = userService.getFollowers(user, page, size);

        return paginated(
                pagination.getPage(),
                pagination.getRequestedSize(),
                pagination.pages(),
                pagination.hasPrevious(),
                pagination.hasNext(), pagination.getCollection().stream()
                .map(this::mapUserWithLinks)
                .collect(Collectors.toSet()));
    }

    @GET
    @Path(UserRouteLinks.Constants.FETCH_USER_FOLLOWINGS)
    public Response getFollowing(@Valid @PathParam("idOrName") String idOrName, @QueryParam("page") int page, @QueryParam("size") int size) throws UserException {
        User user = getUserByIdOrName(idOrName);

        Pagination<User> pagination = userService.getFollowing(user, page, size);

        return paginated(
                pagination.getPage(),
                pagination.getRequestedSize(),
                pagination.pages(),
                pagination.hasPrevious(),
                pagination.hasNext(), pagination.getCollection().stream()
                .map(this::mapUserWithLinks)
                .collect(Collectors.toSet()));
    }

    @POST
    @Path(UserRouteLinks.Constants.CREATE_USER_FOLLOW)
    @Jwt
    public Response followUser(@Valid @PathParam("idOrName") String idOrName) throws UserException {
        String name = securityContext.getUserPrincipal().getName();
        User userA = userService.getByUserName(name);
        User userB = getUserByIdOrName(idOrName);

        userService.followUser(userA, userB);

        return ok();
    }

    @DELETE
    @Path(UserRouteLinks.Constants.DELETE_USER_FOLLOW)
    @Jwt
    public Response unFollowUser(@Valid @PathParam("idOrName") String idOrName) throws UserException {
        String name = securityContext.getUserPrincipal().getName();
        User userA = userService.getByUserName(name);
        User userB = getUserByIdOrName(idOrName);

        userService.unFollowUser(userA, userB);

        return ok();
    }

    @GET
    @Path(UserRouteLinks.Constants.CHECK_USER_FOLLOWING)
    public Response follows(@Valid @PathParam("idOrNameA") String idOrNameA, @Valid @PathParam("idOrNameB") String idOrNameB) throws UserException {
        User userA = getUserByIdOrName(idOrNameA);
        User userB = getUserByIdOrName(idOrNameB);

        boolean flag = false;
        try {
            flag = userService.isUserFollowing(userA, userB);
        } catch (UserFollowException e) { }

        return Response.ok().status(Response.Status.OK).entity(flag).build();
    }

    @GET
    @Path(UserRouteLinks.Constants.FETCH_USER_AVATAR)
    @Produces({"image/png", "image/jpg"})
    public Response getProfileImage(@Valid @PathParam("idOrName") String idOrName) throws UserException, IOException {
        User user = getUserByIdOrName(idOrName);

        BufferedImage image;
        try {
            image = ImageIO.read(new File(String.format("%s/%s/avatar", getAvatarBaseURL(), user.getUsername())));
        } catch (IOException e) {
            // if image is not set, load default
            image = ImageIO.read(new File(String.format("%s/profile_default.png", getAvatarBaseURL())));
        }

        return Response.ok(image).build();
    }

    @POST
    @Path(UserRouteLinks.Constants.CREATE_USER_AVATAR)
    @Consumes({"image/png", "image/jpg"})
    @Jwt
    public Response uploadProfileImage(@Valid BufferedImage image) throws UserException {
        String name = securityContext.getUserPrincipal().getName();
        User user = userService.getByUserName(name);

        if (user == null) {
            throw new UserNotFoundException();
        }

        if (image == null) {
            return nok("The uploaded file as profile image was invalid.");
        }

        File dir = new File(String.format("%s/%s", getAvatarBaseURL(), user.getUsername()));
        try {
            if (!dir.exists()) {
                dir.mkdir();
            }
            ImageIO.write(image, "png", new BufferedOutputStream(new FileOutputStream(new File(String.format("%s/avatar", dir.getAbsolutePath())))));
        } catch (IOException e) {
            dir.deleteOnExit();
            return nok("Unable to save the uploaded image.");
        }

        return ok();
    }

    @DELETE
    @Path(UserRouteLinks.Constants.DELETE_USER_AVATAR)
    @Jwt
    public Response deleteProfileImage() throws UserException {
        String name = securityContext.getUserPrincipal().getName();
        User user = userService.getByUserName(name);

        if (user == null) {
            throw new UserNotFoundException();
        }

        String basePath = String.format("%s/%s", getAvatarBaseURL(), user.getUsername());

        java.nio.file.Path path = Paths.get(basePath);
        if (Files.exists(path)) {
            try {
                Files.walk(path)
                        .map(java.nio.file.Path::toFile)
                        .sorted((o1, o2) -> -o1.compareTo(o2))
                        .forEach(File::delete);

            } catch (IOException e) {
                return nok("Unable to remove the profile image.");
            }
        }

        if (new File(String.format("%s/avatar", basePath)).exists()) {
            return nok("The profile image was not removed");
        }

        return ok();
    }

    private User getUserByIdOrName(String idOrName) throws UserException {
        Long id = null;
        try {
            id = Long.parseLong(idOrName);
        } catch (NumberFormatException ex) { }
        User user = id != null ? userService.getById(id) : userService.getByUserName(idOrName);

        if (user == null) {
            throw new UserNotFoundException();
        }
        return user;
    }

    private UserDto mapUserWithLinks(User user) {
        return mapUserWithLinks(user, false);
    }

    private UserDto mapUserWithLinks(User user, boolean overrideAuthCheck) {
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

        @Pattern(regexp = Patterns.DOMAIN_PATTERN, message = "The given website does is not meet a valid domain name")
        public String website;

        public String location;
    }

}
