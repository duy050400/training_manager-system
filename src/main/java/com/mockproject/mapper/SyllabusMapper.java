package com.mockproject.mapper;

import com.mockproject.dto.SyllabusDTO;
import com.mockproject.entity.Syllabus;
import com.mockproject.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface SyllabusMapper {

    SyllabusMapper INSTANCE = Mappers.getMapper(SyllabusMapper.class);

    @Mapping(target = "lastModifierId", source = "lastModifier.id")
    @Mapping(target = "lastModifierName", source = "lastModifier.fullName")
    @Mapping(target = "creatorId", source = "creator.id")
    @Mapping(target = "creatorName", source = "creator.fullName")
    SyllabusDTO toDTO(Syllabus syllabus);

    @Mapping(target = "lastModifier", source = "lastModifierId", qualifiedByName="mapLastModifier")
    @Mapping(target = "creator", source = "creatorId" , qualifiedByName="mapCreator")
    Syllabus toEntity(SyllabusDTO dto);

    @Named("mapLastModifier")
    default User mapLastModifier(Long id) {
        User user = new User();
        user.setId(id);
        return user;
    }

    @Named("mapCreator")
    default User mapCreator(Long id) {
        User user = new User();
        user.setId(id);
        return user;
    }
}
