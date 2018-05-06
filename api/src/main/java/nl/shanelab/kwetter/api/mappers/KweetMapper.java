package nl.shanelab.kwetter.api.mappers;

import nl.shanelab.kwetter.api.dto.KweetDto;
import nl.shanelab.kwetter.dal.domain.Kweet;
import nl.shanelab.kwetter.dal.domain.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import java.time.LocalDateTime;
import java.util.Collection;

@Mapper
/**
 * Creates a DTO mapping for the Kweet class
 *
 * @source nl.shanelab.kwetter.dal.domain.Kweet
 * @target nl.shanelab.kwetter.api.dto.KweetDto
 */
public interface KweetMapper {

    /**
     * @field Accessible auto-generated mapper object to map objects from Kweet to KweetDTO
     */
    KweetMapper INSTANCE = Mappers.getMapper(KweetMapper.class);

    @Mappings({
            @Mapping(source = "author", target = "author", resultType = String.class),
            @Mapping(source = "mentions", target = "mentions"),
            @Mapping(source = "createdAt", target = "createdAt", resultType = String.class),
            @Mapping(source = "favoritedBy", target = "favorites", resultType = Integer.class)
    })
    /**
     * Creates a mapping from Kweet to KweetDTO
     *
     * @param kweet The kweet to map
     * @return KweetDTO Returns the mapped response object
     */
    KweetDto kweetToDto(Kweet kweet);

    /**
     * Auto-accessible mapper method to map from User to its username
     *
     * @param user The user to map
     * @return String Returns the username of the user
     */
    default String mapUserToUsername(User user) {
        return user != null ? user.getUsername() : null;
    }

    /**
     * Auto-accessible mapped method to map from LocalDateTime to a string representation of the date
     *
     * @param dateTime The date to map
     * @return String Returns a representation of the date in standard date format
     */
    default String mapLocalDateTimeToString(LocalDateTime dateTime) {
        return dateTime.toString();
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