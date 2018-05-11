package nl.shanelab.kwetter.api.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
/**
 * An immutable class representation of the HashTag entity
 *
 * @see nl.shanelab.kwetter.dal.domain.HashTag
 */
public class HashTagDto extends LinkedDto implements Serializable {

    /**
     * The serializable identifier of a hashtag
     */
    private long id;

    /**
     * The name of a hashtag
     */
    private String name;
}
