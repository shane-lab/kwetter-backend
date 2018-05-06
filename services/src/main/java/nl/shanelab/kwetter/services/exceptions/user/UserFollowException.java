package nl.shanelab.kwetter.services.exceptions.user;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import nl.shanelab.kwetter.services.exceptions.UserException;

@Getter
public class UserFollowException extends UserException {

    private FollowViolationType followViolationType;

    public UserFollowException(FollowViolationType type) {
        super(type.getMessage());

        followViolationType = type;
    }

    @RequiredArgsConstructor
    @Getter
    public enum FollowViolationType {
        ALREADY_FOLLOWING("User is already following"),
        NOT_FOLLOWING("User is not following"),
        NOT_FOLLOWED_BY("User is not followed by"),
        SELF_FOLLOWING("Illegal self following");

        private final String message;
    }
}