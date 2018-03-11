package nl.shanelab.kwetter.services.tests.matcher;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import nl.shanelab.kwetter.services.exceptions.user.UserFollowException;
import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

@RequiredArgsConstructor
public class UserFollowExceptionMatcher extends TypeSafeMatcher<UserFollowException> {

    public static UserFollowExceptionMatcher of(UserFollowException.FollowViolationType type) {
        return new UserFollowExceptionMatcher(type);
    }

    @NonNull
    private UserFollowException.FollowViolationType followViolationType;

    protected boolean matchesSafely(final UserFollowException exception) {
        return followViolationType.equals(exception.getFollowViolationType());
    }

    public void describeTo(Description description) {
        // not needed to extend or refactor the description
    }
}
