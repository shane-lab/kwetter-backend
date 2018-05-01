package nl.shanelab.kwetter.api;

import io.jsonwebtoken.Jwts;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import nl.shanelab.kwetter.api.jwt.KeyGenerator;
import nl.shanelab.kwetter.api.jwt.SecretKeyGenerator;

import javax.inject.Inject;
import javax.servlet.ServletContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Collection;
import java.util.Date;

public class BaseRoute {

    @Context
    protected UriInfo uriInfo;

    @Context
    protected ServletContext servletContext;

    @Inject
    private KeyGenerator keyGenerator;

    protected Response ok(Object any) {
        return Response.ok(new ResultEntity(any), MediaType.APPLICATION_JSON_TYPE).build();
    }

    protected Response paginated(int page, int size, int pages, boolean prev, boolean next, Collection<Object> items) {
        return ok(new PaginatedResult(page, size, pages, prev, next, items));
    }

    protected Response nok(String ... errors) {
        return Response.ok(new ErrorEntity(errors), MediaType.APPLICATION_JSON_TYPE).status(Response.Status.BAD_REQUEST).build();
    }

    protected String issue(String login) {
        Instant expireDate = LocalDateTime.now().plusMinutes(60L).atZone(ZoneId.systemDefault()).toInstant();
        KeyGenerator keyGenerator = new SecretKeyGenerator();

        return Jwts.builder()
                .setSubject("shanevdb")
//                .setIssuer(uriInfo.getAbsolutePath().toString())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(Date.from(expireDate))
                .signWith(keyGenerator.getAlgorithm(), keyGenerator.generate())
                .compact();
    }


    public static void main(String ... args) {
        Instant expireDate = LocalDateTime.now().plusMinutes(60L).atZone(ZoneId.systemDefault()).toInstant();
        KeyGenerator keyGenerator = new SecretKeyGenerator();

        String s = Jwts.builder()
                .setSubject("shanevdb")
//                .setIssuer(uriInfo.getAbsolutePath().toString())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(Date.from(expireDate))
                .signWith(keyGenerator.getAlgorithm(), keyGenerator.generate())
                .compact();
        System.out.println(s);
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
