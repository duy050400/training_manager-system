package com.mockproject.mapper;

import com.mockproject.dto.UnitDTO;
import com.mockproject.entity.Session;
import com.mockproject.entity.Unit;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface UnitMapper {

    UnitMapper INSTANCE = Mappers.getMapper(UnitMapper.class);

    @Mapping(target = "sessionId", source = "session.id")
    @Mapping(target = "sessionNumber", source = "session.sessionNumber")
    UnitDTO toDTO(Unit unit);

    @Mapping(target = "session", source = "sessionId", qualifiedByName = "mapSession")
    Unit toEntity(UnitDTO dto);

    @Named("mapSession")
    default Session mapSession(Long id) {
        Session session = new Session();
        session.setId(id);
        return session;
    }
}
