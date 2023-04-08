package com.mockproject.mapper;

import com.mockproject.dto.LocationDTO;
import com.mockproject.entity.Location;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")

public interface LocationMapper {

    LocationMapper INSTANCE = Mappers.getMapper(LocationMapper.class);

    LocationDTO toDTO(Location location);

    Location toEntity(LocationDTO dto);

}
