package nl.shanelab.kwetter.dal.domain;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PastOrPresent;
import java.time.LocalDateTime;
import java.util.Set;

@Data
@Entity
@EqualsAndHashCode(exclude = "favoritedBy")
@NoArgsConstructor
@RequiredArgsConstructor
public class Kweet {

    @Id
    @GeneratedValue
    /**
     * The Kweet identifier
     */
    private long id;

    @NotBlank(message = "The Kweet message may not be set as empty")
    @Max(value = 144, message = "Exceeding the character length limit of {value} of a Kweet is not allowed")
    @Column
    @NonNull
    /**
     * The posted message associated with the Kweet
     */
    private String message;

    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.REMOVE})
    @NonNull
    /**
     * The user who posted the Kweet
     */
    private User author;

    @ManyToMany(fetch = FetchType.LAZY)
    /**
     * A collection of users mentioned in the Kweet
     */
    private Set<User> mentions;

    @ManyToMany(fetch = FetchType.LAZY)
    /**
     * A collection of user who have favorited this Kweet
     */
    private Set<User> favoritedBy;

    @ManyToMany(fetch = FetchType.LAZY)
    /**
     * A collection of hashtags used in the Kweet
     */
    private Set<HashTag> hashTags;

    @PastOrPresent(message = "The creation date of a Kweet may not exceed the present. Timetraveling is not allowed")
    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    /**
     * The date and timestamp of when the Kweet was posted
     */
    private LocalDateTime createdAt = LocalDateTime.now();
}
