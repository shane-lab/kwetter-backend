package nl.shanelab.kwetter.api.routers.api;

import io.swagger.jaxrs2.integration.resources.OpenApiResource;
import nl.shanelab.kwetter.api.providers.*;
import nl.shanelab.kwetter.api.routers.api.routes.HashTagRoute;
import nl.shanelab.kwetter.api.routers.api.routes.KweetRoute;
import nl.shanelab.kwetter.api.routers.api.routes.UserRoute;
import org.glassfish.jersey.jackson.JacksonFeature;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.MediaType;
import java.util.HashSet;
import java.util.Set;

@ApplicationPath(value = "/v1")
@Path("/")
public class ApiRouter extends Application {

    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> resources = new HashSet<>();

        // routes
        resources.add(UserRoute.class);
        resources.add(KweetRoute.class);
        resources.add(HashTagRoute.class);

        // extensions
        resources.add(OpenApiResource.class);
        resources.add(JacksonFeature.class);

        // providers
        resources.add(AnyExceptionProvider.class); // hide other exceptions from visitors, returns a generic 400 bad request response
        resources.add(BufferedImageBodyProvider.class);
        resources.add(ConstraintViolationProvider.class);
        resources.add(CrossOriginProvider.class);
        resources.add(UnrecognizedPropertyProvider.class);
        resources.add(UserExceptionProvider.class);
        resources.add(WebExceptionProvider.class);

        resources.add(ApiRouter.class);

        return resources;
    }


    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String getInfo() {
        // TODO check for some REST interpreter (e.g. https://swagger.io)
        return "Kwetter API - v1";
    }
}
