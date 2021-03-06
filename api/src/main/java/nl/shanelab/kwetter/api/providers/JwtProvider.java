package nl.shanelab.kwetter.api.providers;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.impl.crypto.JwtSigner;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import nl.shanelab.kwetter.api.jwt.KeyGenerator;
import nl.shanelab.kwetter.api.qualifiers.Jwt;
import org.jboss.weld.context.http.Http;

import javax.annotation.Priority;
import javax.inject.Inject;
import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.ext.Provider;
import java.security.Key;
import java.security.Principal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Provider
@Priority(Priorities.AUTHENTICATION)
@Jwt
public class JwtProvider implements ContainerRequestFilter {

    @Inject
    private KeyGenerator keyGenerator;

    @Override
    public void filter(ContainerRequestContext requestContext) {
        String authorizationHeader = requestContext.getHeaderString(HttpHeaders.AUTHORIZATION);

        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            throw NotAuthorizedExceptionEntity("The Authorization header must be provided");
        }

        String token = authorizationHeader.substring("Bearer".length()).trim();

        try {
            Key key = keyGenerator.generate();
            Claims body = Jwts.parser()
                    .setSigningKey(key)
                    .parseClaimsJws(token)
                    .getBody();

            String username = body.getSubject();

            body.setExpiration(Date.from(LocalDateTime.now().plusMinutes(60L).atZone(ZoneId.systemDefault()).toInstant()));

            requestContext.getHeaders().remove(HttpHeaders.AUTHORIZATION);
            requestContext.getHeaders().add(HttpHeaders.AUTHORIZATION, String.format("Bearer %s", Jwts.builder()
                    .setClaims(body)
                    .signWith(keyGenerator.getAlgorithm(), key)
                    .compact()));

            requestContext.setSecurityContext(new SecurityContext() {

                @Override
                public Principal getUserPrincipal() {
                    return () -> username;
                }

                @Override
                public boolean isUserInRole(String role) {
                    return true;
                }

                @Override
                public boolean isSecure() {
                    return false;
                }

                @Override
                public String getAuthenticationScheme() {
                    return "Bearer";
                }
            });

        } catch (Exception e) {
            if (e instanceof io.jsonwebtoken.ExpiredJwtException) {
                throw NotAuthorizedExceptionEntity("The provided JWT has expired");
            }

            throw NotAuthorizedExceptionEntity("Unable to authenticate the user with the provided JWT");
        }
    }

    private NotAuthorizedException NotAuthorizedExceptionEntity(String reason) {
        return new NotAuthorizedException(Response
                .status(Response.Status.UNAUTHORIZED)
                .entity(new NotAuthorizedExceptionEntity(reason))
                .build());
    }

    @Value
    @RequiredArgsConstructor
    protected static class NotAuthorizedExceptionEntity {

        protected final int statusCode = Response.Status.UNAUTHORIZED.getStatusCode();

        protected final String message;

    }
}