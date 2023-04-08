package com.mockproject.mapper;

import com.mockproject.dto.TrainingProgramSyllabusDTO;
import com.mockproject.entity.Syllabus;
import com.mockproject.entity.TrainingProgram;
import com.mockproject.entity.TrainingProgramSyllabus;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface TrainingProgramSyllabusMapper {

    TrainingProgramSyllabusMapper INSTANCE = Mappers.getMapper(TrainingProgramSyllabusMapper.class);

    @Mapping(target = "trainingProgramId", source = "trainingProgram.id")
    @Mapping(target = "trainingProgramName", source = "trainingProgram.name")
    @Mapping(target = "syllabusId", source = "syllabus.id")
    @Mapping(target = "syllabusName", source = "syllabus.name")
    TrainingProgramSyllabusDTO toDTO(TrainingProgramSyllabus trainingProgramSyllabus);

    @Mapping(target = "trainingProgram", source = "trainingProgramId", qualifiedByName = "mapTrainingProgram")
    @Mapping(target = "syllabus", source = "syllabusId", qualifiedByName = "mapSyllabus")
    TrainingProgramSyllabus toEntity(TrainingProgramSyllabusDTO dto);

    @Named("mapTrainingProgram")
    default TrainingProgram mapTrainingProgram(Long id) {
        TrainingProgram trainingProgram = new TrainingProgram();
        trainingProgram.setId(id);
        return trainingProgram;
    }

    @Named("mapSyllabus")
    default Syllabus mapSyllabus(Long id) {
        Syllabus syllabus = new Syllabus();
        syllabus.setId(id);
        return syllabus;
    }


}
