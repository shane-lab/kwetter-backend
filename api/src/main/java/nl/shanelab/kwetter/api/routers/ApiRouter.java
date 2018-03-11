package nl.shanelab.kwetter.api.routers;

import io.swagger.jaxrs2.integration.resources.OpenApiResource;
import nl.shanelab.kwetter.api.routers.routes.UserRoutes;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.MediaType;
import java.util.HashSet;
import java.util.Set;

@Path("/api")
/**
 * Root API resource endpoint
 */
public class ApiRouter extends Application {

    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> resources = new HashSet<>();

        resources.add(UserRoutes.class);

        resources.add(OpenApiResource.class);

        System.out.println("aaa");

        return resources;
    }

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String getInfo() {
        // TODO check for some REST interpreter (e.g. https://swagger.io)
        return "Kwetter API - v1";
    }
}
