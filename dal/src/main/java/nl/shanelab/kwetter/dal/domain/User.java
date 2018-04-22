package nl.shanelab.kwetter.dal.domain;

import lombok.*;
import nl.shanelab.kwetter.dal.jpa.LocalDateTimeConverter;
import nl.shanelab.kwetter.dal.jpa.RoleConverter;
import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Pattern;
import java.time.LocalDateTime;
import java.util.Set;

import static nl.shanelab.kwetter.util.Patterns.NO_SPACES_PATTERN;

@Data
@Entity
@EqualsAndHashCode(exclude = {"kweets", "favoriteKweets", "followers", "following"})
@NoArgsConstructor
@RequiredArgsConstructor
@ToString(exclude = {"kweets", "favoriteKweets", "followers", "following"})
@NamedQueries({
        @NamedQuery(name = "User.getAmountOfFollowers", query = "SELECT size(u.followers) FROM User u WHERE u.id = :id"),
        @NamedQuery(name = "User.getAmountOfFollowings", query = "SELECT size(u.following) FROM User u WHERE u.id = :id"),
        @NamedQuery(name = "User.findByName", query = "SELECT DISTINCT u FROM User u WHERE u.username = :username"),
        @NamedQuery(name = "User.getMostFollowed", query = "SELECT u FROM User u WHERE size(u.followers) > 0 ORDER BY u.id")
})
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    /**
     * The user identifier
     */
    private long id;

    @NotBlank(message = "The username may not be set as empty")
    @Pattern(regexp = NO_SPACES_PATTERN, message = "The username may not contain any whitespaces")
    @Column(unique = true)
    @NonNull
    /**
     * The unique username, used to sign in with
     */
    private String username;

    @NotBlank(message = "The password may not be set as empty")
    @Pattern(regexp = NO_SPACES_PATTERN, message = "The password may not contain any whitespaces")
    @Column
    @NonNull
    /**
     * The password, used to sign in with
     */
    private String password;

    @Convert(converter = RoleConverter.class)
    @Column(name = "role_id")
    @NonNull
    /**
     * The restricted user role, used grants permission for certain actions
     */
    private Role role;

    @OneToMany(mappedBy = "author",fetch = FetchType.LAZY, orphanRemoval = true, cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    /**
     * A collection of Kweets posted by a user
     */
    private Set<Kweet> kweets;

    @ManyToMany(mappedBy = "favoritedBy", cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    /**
     * A collection of Kweets favorited by a user
     */
    private Set<Kweet> favoriteKweets;

    @ManyToMany(mappedBy = "following", fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinTable(name = "follower_following", inverseJoinColumns = {
            @JoinColumn(name = "following_id", referencedColumnName = "id", nullable = false)
    })
    /**
     * A collection of Users who follow the user
     */
    private Set<User> followers;

    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinTable(name = "follower_following", joinColumns = {
            @JoinColumn(name = "follower_id", referencedColumnName = "id", nullable = false)
    })
    /**
     * A collection of Users who the user is following
     */
    private Set<User> following;

    @Convert(converter = LocalDateTimeConverter.class)
    @PastOrPresent(message = "The creation date of a User may not exceed the present. Timetraveling is not allowed")
    @Column(nullable = false)
    /**
     * The date and timestamp of when the Kweet was posted
     */
    private LocalDateTime createdAt = LocalDateTime.now();

    @Max(value = 160, message = "Exceeding the biography limit of {value} is not allowed")
    @Column
    /**
     * A brief description about the user
     */
    private String bio;

    @Column
    /**
     * The personal domain or work domain of the user
     */
    private String website;

    @Column
    /**
     * The payload of the uploaded profile picture of the user
     */
    private byte[] profilePicture;

    @Column
    /**
     * The location of the user
     */
    private String location;
}