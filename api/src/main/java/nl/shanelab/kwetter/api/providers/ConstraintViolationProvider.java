package nl.shanelab.kwetter.api.providers;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.Value;

import javax.validation.ConstraintViolationException;
import javax.validation.constraints.NotBlank;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.util.ArrayList;
import java.util.Collection;

@Provider
public class ConstraintViolationProvider implements ExceptionMapper<ConstraintViolationException> {

    @Override
    public Response toResponse(final ConstraintViolationException ex) {
        return Response
                .status(Response.Status.BAD_REQUEST)
                .entity(new ConstraintViolationEntity(ex))
                .type(MediaType.APPLICATION_JSON)
                .build();
    }

    @Value
    protected static class ConstraintViolationEntity {

        protected int statusCode = Response.Status.BAD_REQUEST.getStatusCode();

        protected Collection<ViolationDetail> errors = new ArrayList<>();

        ConstraintViolationEntity(ConstraintViolationException ex) {
            ex.getConstraintViolations().forEach(violation -> errors.add(new ViolationDetail(violation.getPropertyPath().toString(), violation.getMessage())));
        }

        @Value
        @RequiredArgsConstructor(access = AccessLevel.PACKAGE)
        protected static class ViolationDetail {
            @NotBlank
            String property;
            @NotBlank
            String message;
        }
    }
}