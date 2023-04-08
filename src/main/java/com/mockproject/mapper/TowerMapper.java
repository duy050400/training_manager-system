package com.mockproject.mapper;

import com.mockproject.dto.TowerDTO;
import com.mockproject.entity.Location;
import com.mockproject.entity.Tower;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface TowerMapper {

    TowerMapper INSTANCE = Mappers.getMapper(TowerMapper.class);

    @Mapping(target = "locationId", source = "location.id")
    @Mapping(target = "locationName", source = "location.locationName")
    TowerDTO toDTO(Tower tower);

    @Mapping(target = "location", source = "locationId", qualifiedByName = "mapLocation")
    Tower toEntity(TowerDTO dto);


    @Named("mapLocation")
    default Location mapLocation(Long id) {
        Location location = new Location();
        location.setId(id);
        return location;
    }
}
