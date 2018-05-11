package nl.shanelab.kwetter.api.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Collection;

@Data
@NoArgsConstructor
/**
 * An immutable class representation of the Kweet entity
 *
 * @see nl.shanelab.kwetter.dal.domain.Kweet
 */
public class KweetDto extends LinkedDto implements Serializable {

    /**
     * The serializable identifier of a kweet
     */
    private long id;

    /**
     * The content of a kweet
     */
    private String message;

    /**
     * The mapping from a user entity to an unique username as the author of the kweet
     *
     * @see nl.shanelab.kwetter.dal.domain.User
     */
    private String author;

    /**
     * The creation date of the kweet
     */
    private String createdAt;

    /**
     * The mapping from a collection of user entities to a list of mentioned usernames in the kweet
     *
     * @see nl.shanelab.kwetter.dal.domain.User
     */
    private Collection<String> mentions;

    /**
     * A collection of hashtags used in the kweet
     */
    private Collection<HashTagDto> hashTags;

    /**
     * The amount of times the Kweet was favourited
     */
    private int favorites;
}
