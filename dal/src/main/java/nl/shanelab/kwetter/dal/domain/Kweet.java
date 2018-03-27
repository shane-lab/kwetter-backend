package nl.shanelab.kwetter.dal.domain;

import lombok.*;
import nl.shanelab.kwetter.dal.jpa.LocalDateTimeConverter;

import javax.enterprise.context.ApplicationScoped;
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
@NamedQueries({
        @NamedQuery(name = "Kweet.findByUserId", query = "SELECT k FROM Kweet k WHERE k.author.id = :id ORDER BY k.createdAt DESC"),
        @NamedQuery(name = "Kweet.findByUserName", query = "SELECT k FROM Kweet k WHERE k.author.username = :username ORDER BY k.createdAt DESC"),
        @NamedQuery(name = "Kweet.findByHashTagId", query = "SELECT k FROM Kweet k WHERE :id IN(SELECT h.id FROM k.hashTags h)"),
        @NamedQuery(name = "Kweet.findByHashTagName", query = "SELECT k FROM Kweet k WHERE :name IN(SELECT h.name FROM k.hashTags h)"),
        @NamedQuery(name = "Kweet.findByMentioned", query = "SELECT k FROM Kweet k WHERE :username IN(SELECT u.username FROM k.mentions u)"),
        @NamedQuery(name = "Kweet.findByFavoritedBy", query = "SELECT k FROM Kweet k WHERE :username IN(SELECT u.username FROM k.favoritedBy u)"),
        @NamedQuery(name = "Kweet.isMentionedIn", query = "SELECT COUNT(k) FROM Kweet k WHERE k.id = :id AND :username IN(SELECT u.username FROM k.mentions u)"),
        @NamedQuery(name = "Kweet.isFavoritedBy", query = "SELECT COUNT(k) FROM Kweet k WHERE k.id = :id AND :username IN(SELECT u.username FROM k.favoritedBy u)")
})
public class Kweet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    /**
     * The Kweet identifier
     */
    private long id;

    @NotBlank(message = "The Kweet message may not be set as empty")
//    @Max(value = 144, message = "Exceeding the character length limit of {value} of a Kweet is not allowed")
    @Column
    @NonNull
    /**
     * The posted message associated with the Kweet
     */
    private String message;

    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.REMOVE})
    @NonNull
    @JoinTable(name = "user_kweet", inverseJoinColumns = {
            @JoinColumn(name="user_id", referencedColumnName = "id", nullable = false)
    })
    /**
     * The user who posted the Kweet
     */
    private User author;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "mentioned_kweet",
            joinColumns = @JoinColumn(name="kweet_id", referencedColumnName = "id", nullable = false),
            inverseJoinColumns = @JoinColumn(name="user_id", referencedColumnName = "id", nullable = false))
    /**
     * A collection of users mentioned in the Kweet
     */
    private Set<User> mentions;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "favorited_kweet",
            joinColumns = @JoinColumn(name="kweet_id", referencedColumnName = "id", nullable = false),
            inverseJoinColumns = @JoinColumn(name="user_id", referencedColumnName = "id", nullable = false))
    /**
     * A collection of user who have favorited this Kweet
     */
    private Set<User> favoritedBy;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name = "kweet_hashtag",
            joinColumns = @JoinColumn(name="kweet_id", referencedColumnName = "id", nullable = false),
            inverseJoinColumns = @JoinColumn(name="hashtag_id", referencedColumnName = "id", nullable = false))
    /**
     * A collection of hashtags used in the Kweet
     */
    private Set<HashTag> hashTags;

    @Convert(converter = LocalDateTimeConverter.class)
    @PastOrPresent(message = "The creation date of a Kweet may not exceed the present. Timetraveling is not allowed")
    @Column(nullable = false)
//    @Temporal(TemporalType.TIMESTAMP)
    /**
     * The date and timestamp of when the Kweet was posted
     */
    private LocalDateTime createdAt = LocalDateTime.now();
}
