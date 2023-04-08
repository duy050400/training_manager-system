package com.mockproject.mapper;

import com.mockproject.dto.ClassScheduleDTO;
import com.mockproject.entity.ClassSchedule;
import com.mockproject.entity.TrainingClass;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface ClassScheduleMapper {

    ClassScheduleMapper INSTANCE = Mappers.getMapper(ClassScheduleMapper.class);

    @Mapping(target = "trainingClassId", source = "trainingClass.id")
    @Mapping(target = "trainingClassName", source = "trainingClass.className")
    ClassScheduleDTO toDTO(ClassSchedule classSchedule);

    @Mapping(source = "trainingClassId", target = "trainingClass", qualifiedByName = "mapTrainingClass")
    ClassSchedule toEntity(ClassScheduleDTO dto);


    @Named("mapTrainingClass")
    default TrainingClass mapTrainingClass(Long id) {
        TrainingClass trainingClass = new TrainingClass();
        trainingClass.setId(id);
        return trainingClass;
    }
}
