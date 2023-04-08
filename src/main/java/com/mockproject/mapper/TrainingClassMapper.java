package com.mockproject.mapper;

import com.mockproject.dto.TrainingClassDTO;
import com.mockproject.entity.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface TrainingClassMapper {

    TrainingClassMapper INSTANCE = Mappers.getMapper(TrainingClassMapper.class);


//    @BeanMapping(nullValueMappingStrategy = NullValueMappingStrategy.RETURN_DEFAULT)
//    @Mapping(target = "startTime", source = "startTime", dateFormat = "HH:mm")
//    @Mapping(target = "endTime", source = "endTime", dateFormat = "HH:mm")
    @Mapping(target = "trainingProgramId", source = "trainingProgram.id")
    @Mapping(target = "trainingProgramName", source = "trainingProgram.name")
    @Mapping(target = "reviewerId", source = "reviewer.id")
    @Mapping(target = "reviewerName", source = "reviewer.fullName")
    @Mapping(target = "lastModifierId", source = "lastModifier.id")
    @Mapping(target = "lastModifierName", source = "lastModifier.fullName")
    @Mapping(target = "fsuId", source = "fsu.id")
    @Mapping(target = "fsuName", source = "fsu.fsuName")
    @Mapping(target = "creatorId", source = "creator.id")
    @Mapping(target = "creatorName", source = "creator.fullName")
    @Mapping(target = "contactId", source = "contact.id")
    @Mapping(target = "contactName", source = "contact.contactEmail")
    @Mapping(target = "attendeeId", source = "attendee.id")
    @Mapping(target = "attendeeName", source = "attendee.attendeeName")
    @Mapping(target = "approverId", source = "approver.id")
    @Mapping(target = "approverName", source = "approver.fullName")
    @Mapping(target = "locationId", source = "location.id")
    @Mapping(target = "locationName", source = "location.locationName")
    TrainingClassDTO toDTO(TrainingClass trainingClass);


//    @Mapping(target = "startTime", source = "startTime", qualifiedByName = "timeMap")
//    @Mapping(target = "endTime", source = "endTime", qualifiedByName = "timeMap")
    @Mapping(target = "trainingProgram", source = "trainingProgramId", qualifiedByName = "mapTrainingProgram")
    @Mapping(target = "reviewer", source = "reviewerId", qualifiedByName = "mapReviewer")
    @Mapping(target = "lastModifier", source = "lastModifierId", qualifiedByName = "mapModifier")
    @Mapping(target = "fsu", source = "fsuId", qualifiedByName = "mapFsu")
    @Mapping(target = "creator", source = "creatorId", qualifiedByName = "mapCreator")
    @Mapping(target = "contact", source = "contactId", qualifiedByName = "mapContact")
    @Mapping(target = "attendee", source = "attendeeId", qualifiedByName = "mapAttendee")
    @Mapping(target = "approver", source = "approverId", qualifiedByName = "mapApprover")
    @Mapping(target = "location", source = "locationId", qualifiedByName = "mapLocation")
    TrainingClass toEntity(TrainingClassDTO dto);

//    @Named("timeMap")
//    default Time timeMap(String time_str){
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
//        return (Time) formatter.parse(time_str);
//    }

    @Named("mapLocation")
    default Location mapLocation(Long id) {
        Location location = new Location();
        location.setId(id);
        return location;
    }

    @Named("mapTrainingProgram")
    default TrainingProgram mapTrainingProgram(Long id) {
        TrainingProgram trainingProgram = new TrainingProgram();
        trainingProgram.setId(id);
        return trainingProgram;
    }

    @Named("mapReviewer")
    default User mapReviewer(Long id) {
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

    @Named("mapContact")
    default Contact mapContact(Long id) {
        Contact contact = new Contact();
        contact.setId(id);
        return contact;
    }

    @Named("mapCreator")
    default User mapCreator(Long id) {
        User user = new User();
        user.setId(id);
        return user;
    }

    @Named("mapFsu")
    default Fsu mapFsu(Long id) {
        Fsu fsu = new Fsu();
        fsu.setId(id);
        return fsu;
    }

    @Named("mapApprover")
    default User mapApprover(Long id) {
        User user = new User();
        user.setId(id);
        return user;
    }

    @Named("mapAttendee")
    default Attendee mapAttendee(Long id) {
        Attendee attendee = new Attendee();
        attendee.setId(id);
        return attendee;
    }

}
