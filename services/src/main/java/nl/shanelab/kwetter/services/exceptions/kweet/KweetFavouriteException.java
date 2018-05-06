package nl.shanelab.kwetter.services.exceptions.kweet;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import nl.shanelab.kwetter.services.exceptions.KweetException;

@Getter
public class KweetFavouriteException extends KweetException {

    private FavouriteViolationType favouriteViolationType;

    public KweetFavouriteException(FavouriteViolationType type) {
        super(type.getMessage());

        favouriteViolationType = type;
    }

    @RequiredArgsConstructor
    @Getter
    public enum FavouriteViolationType {
        ALREADY_FAVOURITED("Kweet is already favourited by the user"),
        NOT_FAVOURITED("Kweet is not favourited by the user"),
        SELF_FAVOURIITNG("Illegal to self favourite");

        private final String message;
    }
}
