package nl.shanelab.kwetter.dal.jpa;

import nl.shanelab.kwetter.dal.domain.Role;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class RoleConverter implements AttributeConverter<Role, Integer> {

    public Integer convertToDatabaseColumn(Role role) {
        return role.getId();
    }

    public Role convertToEntityAttribute(Integer attribute) {
        return Role.findById(attribute);
    }
}
