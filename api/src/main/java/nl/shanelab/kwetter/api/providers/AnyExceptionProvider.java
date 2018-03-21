package nl.shanelab.kwetter.api.providers;

import lombok.Value;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
/**
 * Disable any exception to the user
 * TODO log to file on deployed server?
 */
public class AnyExceptionProvider implements ExceptionMapper<Exception> {

    @Override
    public Response toResponse(Exception e) {

        return Response
                .status(Response.Status.BAD_REQUEST)
                .entity(new AnyExceptionEntity())
                .type(MediaType.APPLICATION_JSON)
                .build();
    }

    @Value
    protected static class AnyExceptionEntity {

        protected int statusCode = Response.Status.BAD_REQUEST.getStatusCode();

        protected String message = "The request is invalid";

    }
}
