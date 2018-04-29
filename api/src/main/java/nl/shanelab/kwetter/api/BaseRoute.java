package nl.shanelab.kwetter.api;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Value;

import javax.servlet.ServletContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.util.Collection;

public class BaseRoute {

    @Context
    protected UriInfo uriInfo;

    @Context
    protected ServletContext servletContext;

    protected Response ok(Object any) {
        return Response.ok(new ResultEntity(any), MediaType.APPLICATION_JSON_TYPE).build();
    }

    protected Response paginated(int page, int size, int pages, boolean prev, boolean next, Collection<Object> items) {
        return ok(new PaginatedResult(page, size, pages, prev, next, items));
    }

    protected Response nok(String ... errors) {
        return Response.ok(new ErrorEntity(errors), MediaType.APPLICATION_JSON_TYPE).status(Response.Status.BAD_REQUEST).build();
    }

    @Value
    @RequiredArgsConstructor
    protected static class PaginatedResult {

        private int page;

        private int perPage;

        private int pages;

        private boolean hasPreviousPage;

        private boolean hasNextPage;

        @NonNull
        private Collection<Object> items;

    }

    @Value
    @RequiredArgsConstructor
    protected static class ResultEntity {

        protected int statusCode = Response.Status.OK.getStatusCode();

        @NonNull
        private Object data;

    }

    @Value
    @RequiredArgsConstructor
    protected static class ErrorEntity {

        protected int statusCode = Response.Status.BAD_REQUEST.getStatusCode();

        private boolean error = true;

        @NonNull
        private String[] errors;

    }

}
