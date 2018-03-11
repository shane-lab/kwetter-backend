package nl.shanelab.kwetter.api.routers.routes;

import nl.shanelab.kwetter.api.dto.UserDto;
import nl.shanelab.kwetter.api.mappers.UserMapper;
import nl.shanelab.kwetter.services.UserService;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.Collection;
import java.util.HashSet;

@Path("/users")
public class UserRoutes {

    @Inject
    UserService userService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Collection<UserDto> getUsers() {
        Collection<UserDto> userDtos = new HashSet<>();

        userService.getAllUsers().forEach(user -> userDtos.add(UserMapper.INSTANCE.userAsDTO(user)));

        return userDtos;
    }
}
