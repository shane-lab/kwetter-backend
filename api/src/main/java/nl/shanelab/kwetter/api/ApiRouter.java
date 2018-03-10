package nl.shanelab.kwetter.api;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/api")
/**
 * Root API resource endpoint
 */
public class ApiRouter {

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String getInfo() {
        // TODO check for some REST interpreter (e.g. https://swagger.io)
        return "Kwetter API - v1";
    }
}
