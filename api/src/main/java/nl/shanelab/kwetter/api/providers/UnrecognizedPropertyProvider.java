package nl.shanelab.kwetter.api.providers;

import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
import lombok.Value;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.util.Collection;

@Provider
public class UnrecognizedPropertyProvider implements ExceptionMapper<UnrecognizedPropertyException> {

    @Override
    public Response toResponse(UnrecognizedPropertyException e) {

        return Response
                .status(Response.Status.BAD_REQUEST)
                .entity(new UnrecognizedPropertyEntity(e))
                .type(MediaType.APPLICATION_JSON)
                .build();
    }

    @Value
    protected static class UnrecognizedPropertyEntity {

        protected int statusCode = Response.Status.BAD_REQUEST.getStatusCode();

        protected String message;

        protected String invalidProperty;

        protected Collection<Object> properties;

        UnrecognizedPropertyEntity(UnrecognizedPropertyException exception) {
            invalidProperty = exception.getPropertyName();
            properties = exception.getKnownPropertyIds();
            message = String.format("The property '%s' does not belong to the entity", invalidProperty);
        }

    }
}
