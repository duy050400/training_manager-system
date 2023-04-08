package com.mockproject.mapper;

import com.mockproject.dto.TrainingClassUnitInformationDTO;
import com.mockproject.entity.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface TrainingClassUnitInformationMapper {

    TrainingClassUnitInformationMapper INSTANCE = Mappers.getMapper(TrainingClassUnitInformationMapper.class);


    @Mapping(target = "unitId", source = "unit.id")
    @Mapping(target = "unitTitle", source = "unit.unitTitle")
    @Mapping(target = "trainingClassId", source = "trainingClass.id")
    @Mapping(target = "trainingClassName", source = "trainingClass.className")
    @Mapping(target = "trainerId", source = "trainer.id")
    @Mapping(target = "trainerName", source = "trainer.fullName")
    @Mapping(target = "towerId", source = "tower.id")
    @Mapping(target = "towerName", source = "tower.towerName")
    TrainingClassUnitInformationDTO toDTO(TrainingClassUnitInformation trainingClassUnitInformation);

    @Mapping(target = "unit", source = "unitId", qualifiedByName = "mapUnit")
    @Mapping(target = "trainingClass", source = "trainingClassId", qualifiedByName = "mapTrainingClass")
    @Mapping(target = "trainer", source = "trainerId", qualifiedByName = "mapTrainer")
    @Mapping(target = "tower", source = "towerId", qualifiedByName = "mapTower")
    TrainingClassUnitInformation toEntity(TrainingClassUnitInformationDTO dto);

    @Named("mapTrainingClass")
    default TrainingClass mapTrainingClass(Long id) {
        TrainingClass trainingClass = new TrainingClass();
        trainingClass.setId(id);
        return trainingClass;
    }

    @Named("mapUnit")
    default Unit mapUnit(Long id) {
        Unit unit = new Unit();
        unit.setId(id);
        return unit;
    }

    @Named("mapTrainer")
    default User mapTrainer(Long id) {
        User user = new User();
        user.setId(id);
        return user;
    }

    @Named("mapTower")
    default Tower mapTower(Long id) {
        Tower tower = new Tower();
        tower.setId(id);
        return tower;
    }
}
