package com.mockproject.mapper;

import com.mockproject.dto.AttendeeDTO;
import com.mockproject.entity.Attendee;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface AttendeeMapper {

    AttendeeMapper INSTANCE = Mappers.getMapper(AttendeeMapper.class);

    AttendeeDTO toDTO(Attendee attendee);

    Attendee toEntity(AttendeeDTO dto);
}
