package nl.shanelab.kwetter.api.routers.api.routes;

import nl.shanelab.kwetter.api.BaseRoute;
import nl.shanelab.kwetter.api.mappers.UserMapper;
import nl.shanelab.kwetter.api.qualifiers.Jwt;
import nl.shanelab.kwetter.dal.domain.User;
import nl.shanelab.kwetter.services.UserService;
import nl.shanelab.kwetter.services.exceptions.UserException;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/auth")
@RequestScoped
public class AuthRoute extends BaseRoute {

    @Inject
    UserService userService;

    @POST
    @Path("/login")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response authenticateUser(@Valid UserRoute.UserCredentials credentials) {
        try {
            User user = userService.authenticate(credentials.username, credentials.password);

            return okWithJWT(UserMapper.INSTANCE.userAsDTO(user), issue(user.getUsername()));
        } catch (Exception e) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
    }

    @POST
    @Path("/refresh")
    @Jwt
    public Response refreshToken() throws UserException {
        return ok();
    }

}
