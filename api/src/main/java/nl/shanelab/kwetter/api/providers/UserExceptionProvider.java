package nl.shanelab.kwetter.api.providers;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import nl.shanelab.kwetter.services.exceptions.UserException;
import nl.shanelab.kwetter.services.exceptions.user.UserAlreadyExistsException;
import nl.shanelab.kwetter.services.exceptions.user.UserFollowException;
import nl.shanelab.kwetter.services.exceptions.user.UserIncorrectCredentialsException;
import nl.shanelab.kwetter.services.exceptions.user.UserNotFoundException;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.util.HashMap;
import java.util.Map;

@Provider
public class UserExceptionProvider implements ExceptionMapper<UserException> {

    @Override
    public Response toResponse(UserException e) {
        UserExceptionStatusType type = UserExceptionStatusType.by(e);

        return Response
                .status(type.getStatus())
                .entity(new UserExceptionEntity(type, e.getMessage()))
                .type(MediaType.APPLICATION_JSON)
                .build();
    }

    @RequiredArgsConstructor
    @Getter
    protected enum UserExceptionStatusType {
        DEFAULT(UserException.class, Response.Status.BAD_REQUEST),
        ALREADY_EXISTS(UserAlreadyExistsException.class, Response.Status.CONFLICT),
        NOT_FOUND(UserNotFoundException.class, Response.Status.NOT_FOUND),
        INCORRECT_CREDENTIALS(UserIncorrectCredentialsException.class, Response.Status.NOT_FOUND),
        FOLLOW_RELATED(UserFollowException.class, Response.Status.METHOD_NOT_ALLOWED);

        private final Class<? extends UserException> klass;

        private final Response.Status status;

        private static final Map<Class<? extends UserException>, UserExceptionStatusType> EXCEPTION_TYPE_LOOKUP_TABLE = new HashMap<>();

        static {
            for (UserExceptionStatusType uest : UserExceptionStatusType.values()) {
                EXCEPTION_TYPE_LOOKUP_TABLE.put(uest.klass, uest);
            }
        }

        public static UserExceptionStatusType by(UserException e) {
            return EXCEPTION_TYPE_LOOKUP_TABLE.get(e.getClass());
        }
    }

    @Value
    protected static class UserExceptionEntity {

        protected int statusCode;

        protected String message;

        UserExceptionEntity(UserExceptionStatusType type, String reason) {
            statusCode = type.getStatus().getStatusCode();
            message = reason;
        }
    }
}
