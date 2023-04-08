package com.mockproject.mapper;

import com.mockproject.dto.UserDTO;
import com.mockproject.entity.Attendee;
import com.mockproject.entity.Level;
import com.mockproject.entity.Role;
import com.mockproject.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    @Mapping(target = "roleId", source = "role.id")
    @Mapping(target = "roleName", source = "role.roleName")
    @Mapping(target = "levelId", source = "level.id")
    @Mapping(target = "levelCode", source = "level.levelCode")
    @Mapping(target = "attendeeId", source = "attendee.id")
    @Mapping(target = "attendeeName", source = "attendee.attendeeName")
    UserDTO toDTO(User user);

    @Mapping(target = "password", ignore = true)
    @Mapping(target = "role", source = "roleId", qualifiedByName = "mapRole")
    @Mapping(target = "level", source = "levelId", qualifiedByName = "mapLevel")
    @Mapping(target = "attendee", source = "attendeeId", qualifiedByName = "mapAttendee")
    User toEntity(UserDTO dto);

    @Named("mapRole")
    default Role mapRole(Long id) {
        Role role = new Role();
        role.setId(id);
        return role;
    }

    @Named("mapLevel")
    default Level mapLevel(Long id) {
        Level level = new Level();
        level.setId(id);
        return level;
    }

    @Named("mapAttendee")
    default Attendee mapAttendee(Long id) {
        Attendee attendee = new Attendee();
        attendee.setId(id);
        return attendee;
    }

}
