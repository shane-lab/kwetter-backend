package nl.shanelab.kwetter.api.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Data
@NoArgsConstructor
/**
 * An immutable class representation of the User entity
 *
 * @see nl.shanelab.kwetter.dal.domain.User
 */
public class UserDto implements Serializable {

    /**
     * The id of a user
     */
    private long id;

    /**
     * The username, a part of the login credential, of a user
     */
    private String username;

    /**
     * The mapping from user role enumeration to a string representation of the user role
     *
     * @see nl.shanelab.kwetter.dal.domain.Role
     */
    @NotBlank(message = "Role may not be blank")
    private String role;

    /**
     * The mapping from user role enumeration to the discriminator value of the user role
     *
     * @see nl.shanelab.kwetter.dal.domain.Role
     */
    private int roleId;

    /**
     * The amount of kweets posted by a user
     */
    private int kweets;

    /**
     * The amount of users the user is following
     */
    private int following;

    /**
     * The amount of followings the user has
     */
    private int followers;

    /**
     * The biographical description of a user
     */
    private String bio;
}
