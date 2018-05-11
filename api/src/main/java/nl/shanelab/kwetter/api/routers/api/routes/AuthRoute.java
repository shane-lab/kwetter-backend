package nl.shanelab.kwetter.api.routers.api.routes;

import nl.shanelab.kwetter.api.qualifiers.Jwt;
import nl.shanelab.kwetter.dal.domain.User;
import nl.shanelab.kwetter.services.UserService;
import nl.shanelab.kwetter.services.exceptions.UserException;
import nl.shanelab.kwetter.services.exceptions.user.UserNotFoundException;

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
public class AuthRoute extends UserBaseRoute {

    @Inject
    UserService userService;

    @POST
    @Path("/login")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response authenticateUser(@Valid UserRoute.UserCredentials credentials) throws UserException {
        User user = userService.authenticate(credentials.username, credentials.password);
        try {
            return okWithJWT(mapUserWithLinks(user, true), issue(user.getUsername()));
        } catch (Exception e) {
            return nok("Unable to authenticate the user");
        }
    }

    @POST
    @Path("/refresh")
    @Jwt
    public Response refreshToken() throws UserException {
        String name = securityContext.getUserPrincipal().getName();
        User user = userService.getByUserName(name);

        if (user == null) {
            throw new UserNotFoundException(name);
        }

        return ok(mapUserWithLinks(user));
    }

}
