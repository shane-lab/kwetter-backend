package nl.shanelab.kwetter.services.tests.matcher;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import nl.shanelab.kwetter.services.exceptions.kweet.KweetFavouriteException;
import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

@RequiredArgsConstructor
public class KweetFavouriteExceptionMatcher extends TypeSafeMatcher<KweetFavouriteException> {

    public static KweetFavouriteExceptionMatcher of(KweetFavouriteException.FavouriteViolationType type) {
        return new KweetFavouriteExceptionMatcher(type);
    }

    @NonNull
    private KweetFavouriteException.FavouriteViolationType favouriteViolationType;

    protected boolean matchesSafely(final KweetFavouriteException exception) {
        return favouriteViolationType.equals(exception.getFavouriteViolationType());
    }

    public void describeTo(Description description) {
        // not needed to extend or refactor the description
    }
}
