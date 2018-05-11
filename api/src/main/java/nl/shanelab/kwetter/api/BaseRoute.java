package nl.shanelab.kwetter.api;

import io.jsonwebtoken.Jwts;
import lombok.*;
import nl.shanelab.kwetter.api.hateoas.Linked;
import nl.shanelab.kwetter.api.jwt.KeyGenerator;

import javax.inject.Inject;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.*;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Collection;
import java.util.Date;

public abstract class BaseRoute {

    @Context
    protected UriInfo uriInfo;

    @Context
    protected HttpHeaders httpHeaders;

    @Context
    protected ServletContext servletContext;

    @Context
    protected SecurityContext securityContext;

    @Context
    private HttpServletRequest httpRequest;

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
        return Response.ok(any != null ? new ResultEntity(any, Linked.builder()
                .type(httpRequest.getMethod().toUpperCase())
                .path(uriInfo.getAbsolutePath().toString())
                .href(uriInfo.getPath())
                .title("The requested entry point")
                .requiresAuth(securityContext.getUserPrincipal() != null) // checks if jwt was set, auth was required
                .rel("self")
                .build()) : any, MediaType.APPLICATION_JSON_TYPE);
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

    protected boolean isAuthenticated(String username) {
        if (securityContext.getUserPrincipal() == null) {
            return false;
        }

        return username.equalsIgnoreCase(securityContext.getUserPrincipal().getName());
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

        @NonNull
        protected Linked link;

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
