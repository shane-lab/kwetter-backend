package nl.shanelab.kwetter.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Patterns {

    public static final String MENTION_PATTERN = "(@[aA-zZ0-9])\\w+";
    public static final String HASHTAG_PATTERN = "(#[aA-zZ0-9].)\\w+";

    public static final String WORD_GROUP_START_PATTERN = "(^|[^aA-ZZ])(";
    public static final String WORD_GROUP_END_PATTERN = ")([^aA-ZZ]|$)";

    public static final String DOMAIN_PATTERN = "^(http\\:\\/\\/|https\\:\\/\\/)?([a-z0-9][a-z0-9\\-]*\\.)+[a-z0-9][a-z0-9\\-]*$";

    public static final String NO_SPACES_PATTERN = "^[^\\s]+$";

}
