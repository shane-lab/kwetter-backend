package nl.shanelab.kwetter.api.mappers;

import nl.shanelab.kwetter.api.dto.UserDto;
import nl.shanelab.kwetter.dal.domain.Role;
import nl.shanelab.kwetter.dal.domain.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import java.util.Collection;

@Mapper
/**
 * Creates a DTO mapping for the User class
 *
 * @source nl.shanelab.kwetter.dal.domain.User
 * @target nl.shanelab.kwetter.api.dto.UserDto
 */
public interface UserMapper {

    /**
     * @field Accessible auto-generated mapper object to map objects from User to UserDTO
     */
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    @Mappings({
            @Mapping(source = "role", target = "role", resultType = String.class),
            @Mapping(source = "role", target = "roleId", resultType = Integer.class)
    })
    /**
     * Creates a mapping from User to UserDto
     *
     * @param user The user to map
     * @return UserDto Returns a mapped response object
     */
    UserDto userAsDTO(User user);

    /**
     * Auto-accessible mapper method to map from Role to its discriminator value
     *
     * @param role The role to map
     * @return Integer Returns the discriminator value of the role
     */
    default Integer mapRoleToInt(Role role) {
        return role.getId();
    }

    /**
     * Auto-accessible mapper method to map rom a collection to the amount of records in the collection
     *
     * @param collection The collection to check
     * @return Integer Returns the amount of records in the collection
     */
    default Integer mapCollectionToInt(Collection<?> collection) {
        return collection != null ? collection.size() : 0;
    }
}