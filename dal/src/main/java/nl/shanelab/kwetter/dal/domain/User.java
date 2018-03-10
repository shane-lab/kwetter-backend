package nl.shanelab.kwetter.dal.domain;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.util.Set;

@Data
@Entity
@EqualsAndHashCode(exclude = {"favoriteKweets", "followers", "following"})
@NoArgsConstructor
@RequiredArgsConstructor
public class User {

    @Id
    @GeneratedValue
    /**
     * The user identifier
     */
    private long id;

    @NotBlank(message = "The username may not be set as empty")
    @Pattern(regexp = "/[^\\s+]/g", message = "The username may not contain any whitespaces")
    @Column(unique = true)
    @NonNull
    /**
     * The unique username, used to sign in with
     */
    private String username;

    @NotBlank(message = "The password may not be set as empty")
    @Pattern(regexp = "/[^\\s+]/g", message = "The password may not contain any whitespaces")
    @Column
    @NonNull
    /**
     * The password, used to sign in with
     */
    private String password;

    @Enumerated(value = EnumType.ORDINAL)
    @NonNull
    /**
     * The restricted user role, used grants permission for certain actions
     */
    private Role role;

    @OneToMany(mappedBy = "author",fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    /**
     * A collection of Kweets posted by a user
     */
    private Set<Kweet> kweets;

    @ManyToMany(mappedBy = "favoritedBy", cascade = CascadeType.ALL)
    /**
     * A collection of Kweets favorited by a user
     */
    private Set<Kweet> favoriteKweets;

    @ManyToMany(mappedBy = "following", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    /**
     * A collection of Users who follow the user
     */
    private Set<User> followers;

    @ManyToMany(fetch = FetchType.LAZY)
    /**
     * A collection of Users who the user is following
     */
    private Set<User> following;

    @Max(value = 160, message = "Exceeding the biography limit of {value} is not allowed")
    @Column
    /**
     * A brief description about the user
     */
    private String bio;
}