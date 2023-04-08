package com.mockproject.mapper;

import com.mockproject.dto.TrainingClassFilterResponseDTO;
import com.mockproject.dto.UnitResponseDTO;
import com.mockproject.entity.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring",
        uses = {TrainingClassUnitInformation.class, Tower.class, Location.class})
public interface TrainingClassFilterMap {

    @Mapping(source = "trainingClass.id", target = "id")
    @Mapping(source = "trainingClass.className", target = "className")
    @Mapping(source = "trainingClass.classCode", target = "classCode")
    @Mapping(source = "trainingClass.startTime", target = "startTime")
    @Mapping(source = "trainingClass.listTrainingClassAdmins", target = "adminName",
            qualifiedByName = "mapTrainingClassAdminsToAdminNames")
    @Mapping(source = "trainingClass.attendee.attendeeName", target = "attendee")
    @Mapping(source = "trainers",target = "trainerName")
    @Mapping(source = "date", target = "day")
    @Mapping(source = "durationDay", target = "durationDay")
    @Mapping(source = "trainingClass.location.locationName", target = "locationName")
    @Mapping(source = "unit", target = "units")
    TrainingClassFilterResponseDTO toTrainingClassFilterResponseDTO(TrainingClass trainingClass,
//                                                                    List<String>LocationNames,
                                                                    List<String> trainers,
                                                                    String durationDay,
                                                                    LocalDate date,
                                                                    List<UnitResponseDTO> unit);

    @Named("mapTrainingClassAdminsToAdminNames")
    default List<String>mapTrainingClassAdminsToAdminNames(List<TrainingClassAdmin> admins){
            return admins.stream()
                    .map(admin -> admin.getAdmin().getFullName())
                    .collect(Collectors.toList());
    }


}
