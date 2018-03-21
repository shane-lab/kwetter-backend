package nl.shanelab.kwetter.api.routers;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Value;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

public class BaseRoute {

    @Context
    protected UriInfo uriInfo;

    protected Response ok(Object any) {
        return Response.ok(new ResultEntity(any), MediaType.APPLICATION_JSON_TYPE).build();
    }

    protected Response nok(String ... errors) {
        return Response.ok(new ErrorEntity(errors), MediaType.APPLICATION_JSON_TYPE).status(Response.Status.BAD_REQUEST).build();
    }

    @Value
    @RequiredArgsConstructor
    protected static class ResultEntity {

        @NonNull
        protected int statusCode = Response.Status.OK.getStatusCode();

        @NonNull
        private Object data;

    }

    @Value
    @RequiredArgsConstructor
    protected static class ErrorEntity {

        @NonNull
        protected int statusCode = Response.Status.BAD_REQUEST.getStatusCode();

        private boolean error = true;

        @NonNull
        private String[] errors;

    }
}
