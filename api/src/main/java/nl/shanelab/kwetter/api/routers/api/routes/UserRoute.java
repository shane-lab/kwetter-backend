package nl.shanelab.kwetter.api.routers.api.routes;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.NoArgsConstructor;
import nl.shanelab.kwetter.api.BaseRoute;
import nl.shanelab.kwetter.api.dto.UserDto;
import nl.shanelab.kwetter.api.mappers.UserMapper;
import nl.shanelab.kwetter.dal.dao.Pagination;
import nl.shanelab.kwetter.dal.domain.User;
import nl.shanelab.kwetter.services.UserService;
import nl.shanelab.kwetter.services.exceptions.UserException;
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
    @Path("/")
    public Response getUsers(@QueryParam("page") int page, @QueryParam("size") int size) {
        Pagination<User> pagination = userService.getAllUsers(page, size);

        return paginated(
                pagination.getPage(),
                pagination.getRequestedSize(),
                pagination.pages(),
                pagination.hasPrevious(),
                pagination.hasNext(), pagination.getCollection().stream()
                .map(UserMapper.INSTANCE::userAsDTO)
                .collect(Collectors.toSet()));
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
    @Path("/{idOrName}")
    public Response getUserById(@Valid @PathParam("idOrName") String idOrName) throws UserNotFoundException {
        Long id = null;
        try {
            id = Long.parseLong(idOrName);
        } catch (NumberFormatException ex) { }
        User user = id != null ? userService.getById(id) : userService.getByUserName(idOrName);

        if (user == null) {
            throw new UserNotFoundException();
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

    @GET
    @Path("/{idOrName}/avatar")
    @Produces({"image/png", "image/jpg"})
    public Response getProfileImage(@Valid @PathParam("idOrName") String idOrName) throws UserException, IOException {
        Long id = null;
        try {
            id = Long.parseLong(idOrName);
        } catch (NumberFormatException ex) { }
        User user = id != null ? userService.getById(id) : userService.getByUserName(idOrName);

        if (user == null) {
            throw new UserNotFoundException();
        }

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
    @Path("/{id}/avatar")
    @Consumes({"image/png", "image/jpg"})
    public Response uploadProfileImage(@Valid @PathParam("id") long id, BufferedImage image) throws UserException {
        User user = userService.getById(id);
        if (user == null) {
            throw new UserNotFoundException(id);
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

        return Response.ok().build();
    }

    @DELETE
    @Path("/{id}/avatar")
    public Response deleteProfileImage(@Valid @PathParam("id") long id) throws UserException {
        User user = userService.getById(id);
        if (user == null) {
            throw new UserNotFoundException(id);
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

        return Response.ok().build();
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
