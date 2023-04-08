package com.mockproject.mapper;

import com.mockproject.dto.TrainingProgramDTO;
import com.mockproject.entity.TrainingProgram;
import com.mockproject.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface TrainingProgramMapper {

    TrainingProgramMapper INSTANCE = Mappers.getMapper(TrainingProgramMapper.class);

    @Mapping(target = "lastModifierId", source = "lastModifier.id")
    @Mapping(target = "lastModifierName", source = "lastModifier.fullName")
    @Mapping(target = "creatorId", source = "creator.id")
    @Mapping(target = "creatorName", source = "creator.fullName")
    TrainingProgramDTO toDTO(TrainingProgram trainingProgram);

    @Mapping(target = "lastModifier", source = "lastModifierId", qualifiedByName = "mapModifier")
    @Mapping(target = "creator", source = "creatorId", qualifiedByName = "mapCreator")
    TrainingProgram toEntity(TrainingProgramDTO dto);

    @Named("mapCreator")
    default User mapCreator(Long id) {
        User user = new User();
        user.setId(id);
        return user;
    }

    @Named("mapModifier")
    default User mapModifier(Long id) {
        User user = new User();
        user.setId(id);
        return user;
    }
}
