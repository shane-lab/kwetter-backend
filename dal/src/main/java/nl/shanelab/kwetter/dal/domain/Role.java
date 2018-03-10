package nl.shanelab.kwetter.dal.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
public enum Role {
    USER(0),
    MODERATOR(1),
    ADMINISTRATOR(2);

    /**
     * @field A lookup table to search for Roles by its discriminator value
     */
    private static final Map<Integer, Role> ROLE_ID_LOOKUP_TABLE = new HashMap<>();
    /**
     * @field A lookup table to search for Roles by their enumerated name
     */
    private static final Map<String, Role> ROLE_SID_LOOKUP_TABLE = new HashMap<>();

    static {
        for (Role role : Role.values()) {
            ROLE_ID_LOOKUP_TABLE.put(role.id, role);
            ROLE_SID_LOOKUP_TABLE.put(role.toString(), role);
        }
    }

    @Getter
    /**
     * @field The unique discriminator value
     */
    private final int id;

    /**
     * Find a role by its discriminator value
     *
     * @param id The discriminator value
     * @return Role Returns a Role associated with the given discriminator value
     * @throws IllegalArgumentException Throws an exception when the discriminator value is below 0 or not in the lookup table
     */
    public static Role findById(final int id) {
        if (id >= 0 && ROLE_ID_LOOKUP_TABLE.containsKey(id)) {
            return ROLE_ID_LOOKUP_TABLE.get(id);
        }

        throw new IllegalArgumentException(String.format("No such Role with id of '%d' was found", id));
    }

    /**
     * Find a role by its enumerated name
     *
     * @param sid The enumerated name
     * @return Role Returns the Role associated with the given enumerated name
     * @throws IllegalArgumentException Throws an exception when the enumerated name is blank or not in the lookup table
     */
    public static Role findByName(final String sid) {
        if (sid != null && sid.length() > 0 && ROLE_SID_LOOKUP_TABLE.containsKey(sid.toLowerCase())) {
            return ROLE_SID_LOOKUP_TABLE.get(sid);
        }

        throw new IllegalArgumentException(String.format("No such Role with name of '%s' was found", sid));
    }

    @Override
    public String toString() {
        return super.toString().toLowerCase();
    }
}
