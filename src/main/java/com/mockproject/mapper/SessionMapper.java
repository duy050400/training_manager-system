package com.mockproject.mapper;

import com.mockproject.dto.SessionDTO;
import com.mockproject.entity.Session;
import com.mockproject.entity.Syllabus;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface SessionMapper {

    SessionMapper INSTANCE = Mappers.getMapper(SessionMapper.class);

    @Mapping(target = "syllabusId", source = "syllabus.id")
    @Mapping(target = "syllabusName", source = "syllabus.name")
    SessionDTO toDTO(Session session);

    @Mapping(target = "syllabus", source = "syllabusId", qualifiedByName="mapSyllabus" )
    Session toEntity(SessionDTO dto);


    @Named("mapSyllabus")
    default Syllabus mapSyllabus(Long id) {
        Syllabus syllabus = new Syllabus();
        syllabus.setId(id);
        return syllabus;
    }
}
