package nl.shanelab.kwetter.api;

import io.jsonwebtoken.Jwts;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import nl.shanelab.kwetter.api.jwt.KeyGenerator;

import javax.inject.Inject;
import javax.servlet.ServletContext;
import javax.ws.rs.core.*;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Collection;
import java.util.Date;

public class BaseRoute {

    @Context
    protected UriInfo uriInfo;

    @Context
    protected HttpHeaders httpHeaders;

    @Context
    protected ServletContext servletContext;

    @Context
    protected SecurityContext securityContext;

    @Inject
    private KeyGenerator keyGenerator;

    protected Response ok() {
        return ok(null);
    }

    protected Response ok(Object any) {
        String token = httpHeaders.getHeaderString(HttpHeaders.AUTHORIZATION);
        if (token != null) {
            token = token.startsWith("Bearer") ? token.replace("Bearer", "").trim() : null;
        }
        return okWithJWT(any, token);
    }

    protected Response okWithJWT(String token) {
        return okWithJWT(token);
    }

    protected Response okWithJWT(Object any, String token) {
        Response.ResponseBuilder response = okBuilder(any);
        if (token != null) {
            response.header(HttpHeaders.AUTHORIZATION, token);
        }
        return response.build();
    }

    private Response.ResponseBuilder okBuilder(Object any) {
        return Response.ok(any != null ? new ResultEntity(any) : any, MediaType.APPLICATION_JSON_TYPE);
    }

    protected Response paginated(int page, int size, int pages, boolean prev, boolean next, Collection<Object> items) {
        return ok(new PaginatedResult(page, size, pages, prev, next, items));
    }

    protected Response nok(String ... errors) {
        return Response.ok(new ErrorEntity(errors), MediaType.APPLICATION_JSON_TYPE).status(Response.Status.BAD_REQUEST).build();
    }

    protected String issue(String username) {
        Instant expireDate = LocalDateTime.now().plusMinutes(60L).atZone(ZoneId.systemDefault()).toInstant();

        return Jwts.builder()
                .setSubject(username)
                .setIssuer(uriInfo.getAbsolutePath().toString())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(Date.from(expireDate))
                .signWith(keyGenerator.getAlgorithm(), keyGenerator.generate())
                .compact();
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
