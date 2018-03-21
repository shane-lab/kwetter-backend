package nl.shanelab.kwetter.api.providers;

import lombok.RequiredArgsConstructor;
import lombok.Value;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

public class WebExceptionProvider implements ExceptionMapper<WebApplicationException> {

    @Override
    public Response toResponse(WebApplicationException e) {

        int status = Response.Status.BAD_REQUEST.getStatusCode();
        if (e.getResponse() != null) {
            status = e.getResponse().getStatus();
        }

        return Response
                .status(status)
                .entity(new WebExceptionProvider.WebExceptionEntity(status, e.getMessage()))
                .type(MediaType.APPLICATION_JSON)
                .build();
    }

    @Value
    @RequiredArgsConstructor
    protected static class WebExceptionEntity {

        protected final int statusCode;

        protected final String message;

    }
}
