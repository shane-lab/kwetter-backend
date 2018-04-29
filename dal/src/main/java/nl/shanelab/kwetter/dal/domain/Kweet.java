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
        @NamedQuery(name = "Kweet.getAmountOfFavourites", query = "SELECT size(k.favoritedBy) FROM Kweet k WHERE k.id = :id"),
        @NamedQuery(name = "Kweet.getAmountOfHashTags", query = "SELECT size(k.hashTags) FROM Kweet k WHERE k.id = :id"),
        @NamedQuery(name = "Kweet.getAmountOfMentions", query = "SELECT size(k.mentions) FROM Kweet k WHERE k.id = :id"),
        @NamedQuery(name = "Kweet.getAmountByUserId", query = "SELECT COUNT(k) FROM Kweet k WHERE k.author.id = :id"),
        @NamedQuery(name = "Kweet.findByUserId", query = Kweet.findByUserId+" ORDER BY k.createdAt DESC"),
        @NamedQuery(name = "Kweet.findByUserId.count", query = "SELECT COUNT(k) FROM Kweet k WHERE k IN("+Kweet.findByUserId+")"),
        @NamedQuery(name = "Kweet.findByUserName", query = Kweet.findByUserName+" ORDER BY k.createdAt DESC"),
        @NamedQuery(name = "Kweet.findByUserName.count", query = "SELECT COUNT(k) FROM Kweet k WHERE k IN("+Kweet.findByUserName+")"),
        @NamedQuery(name = "Kweet.findByHashTagId", query = Kweet.findByHashTagId+" ORDER BY k.createdAt DESC"),
        @NamedQuery(name = "Kweet.findByHashTagId.count", query = "SELECT COUNT(k) FROM Kweet k WHERE k IN("+Kweet.findByHashTagId+")"),
        @NamedQuery(name = "Kweet.findByHashTagName", query = Kweet.findByHashTagName+" ORDER BY k.createdAt DESC"),
        @NamedQuery(name = "Kweet.findByHashTagName.count", query = "SELECT COUNT(k) FROM Kweet k WHERE k IN("+Kweet.findByHashTagName+")"),
        @NamedQuery(name = "Kweet.findByMentioned", query = Kweet.findByMentioned+" ORDER BY k.createdAt DESC"),
        @NamedQuery(name = "Kweet.findByMentioned.count", query = "SELECT COUNT(k) FROM Kweet k WHERE k IN("+Kweet.findByMentioned+")"),
        @NamedQuery(name = "Kweet.findByFavoritedBy", query = Kweet.findByFavoritedBy+" ORDER BY k.createdAt DESC"),
        @NamedQuery(name = "Kweet.findByFavoritedBy.count", query = "SELECT COUNT(k) FROM Kweet k WHERE k IN("+Kweet.findByFavoritedBy+")"),
        @NamedQuery(name = "Kweet.isMentionedIn", query = "SELECT COUNT(k) FROM Kweet k WHERE k.id = :id AND :username IN(SELECT u.username FROM k.mentions u)"),
        @NamedQuery(name = "Kweet.isFavoritedBy", query = "SELECT COUNT(k) FROM Kweet k WHERE k.id = :id AND :username IN(SELECT u.username FROM k.favoritedBy u)"),
        @NamedQuery(name = "Kweet.getMostFavourited", query = "SELECT k FROM Kweet k WHERE size(k.favoritedBy) > 0 ORDER BY size(k.favoritedBy)"),
        @NamedQuery(name = "Kweet.getTimeline", query = Kweet.getTimeline + " ORDER BY k.id DESC"),
        @NamedQuery(name = "Kweet.getTimeline.count", query = "SELECT COUNT(k) FROM Kweet k WHERE k IN("+Kweet.getTimeline+")")
})
public class Kweet {

    static final String findByUserId = "SELECT k FROM Kweet k WHERE k.author.id = :id";
    static final String findByUserName = "SELECT k FROM Kweet k WHERE k.author.username = :username";
    static final String findByHashTagId = "SELECT k FROM Kweet k WHERE :id IN(SELECT h.id FROM k.hashTags h)";
    static final String findByHashTagName = "SELECT k FROM Kweet k WHERE :name IN(SELECT h.name FROM k.hashTags h)";
    static final String findByMentioned = "SELECT k FROM Kweet k WHERE :username IN(SELECT u.username FROM k.mentions u)";
    static final String findByFavoritedBy = "SELECT k FROM Kweet k WHERE :username IN(SELECT u.username FROM k.favoritedBy u)";
    static final String getTimeline = "SELECT k FROM Kweet k WHERE k.author.id = :id OR k.author IN (SELECT f FROM User u INNER JOIN u.following f WHERE u.id = :id)";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    /**
     * The Kweet identifier
     */
    private long id;

    @NotBlank(message = "The Kweet message may not be set as empty")
    @Column
    @NonNull
    /**
     * The posted message associated with the Kweet
     */
    private String message;

    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @NonNull
    /**
     * The user who posted the Kweet
     */
    private User author;

    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinTable(name = "mentioned_kweet",
            joinColumns = @JoinColumn(name="kweet_id", referencedColumnName = "id", nullable = false),
            inverseJoinColumns = @JoinColumn(name="user_id", referencedColumnName = "id", nullable = false))
    /**
     * A collection of users mentioned in the Kweet
     */
    private Set<User> mentions;

    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinTable(name = "favorited_kweet",
            joinColumns = @JoinColumn(name="kweet_id", referencedColumnName = "id", nullable = false),
            inverseJoinColumns = @JoinColumn(name="user_id", referencedColumnName = "id", nullable = false))
    /**
     * A collection of user who have favorited this Kweet
     */
    private Set<User> favoritedBy;

    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
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
    /**
     * The date and timestamp of when the Kweet was posted
     */
    private LocalDateTime createdAt = LocalDateTime.now();
}
