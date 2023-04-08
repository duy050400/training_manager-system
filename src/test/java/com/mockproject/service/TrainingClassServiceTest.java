package com.mockproject.service;


import com.mockproject.dto.TrainingClassDTO;
import com.mockproject.entity.*;
import com.mockproject.exception.entity.EntityNotFoundException;
import com.mockproject.mapper.TrainingClassMapper;
import com.mockproject.repository.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.sql.Time;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ContextConfiguration(classes = {TrainingClassService.class})
@ExtendWith(SpringExtension.class)
class TrainingClassServiceTest {
    @MockBean
    private LocationRepository locationRepository;

    @MockBean
    private TrainingProgramRepository trainingProgramRepository;

    @MockBean
    private TrainingClassRepository trainingClassRepository;

    @MockBean
    private TrainingClassMapper trainingClassMapper;

    @MockBean
    private ClassScheduleRepository classScheduleRepository;

    @MockBean
    private TrainingClassUnitInformationRepository trainingClassUnitInformationRepository;

    @MockBean
    private TrainingClassAdminRepository trainingClassAdminRepository;

    @Autowired
    private TrainingClassService trainingClassService;

    Attendee attendee = new Attendee(1L, "", "", true, null, null);
    Location location = new Location(1L, "Ha Noi", "123 Le Loi", true, null, null);
    Fsu fsu = new Fsu(1L, "", "", true, null);
    Contact contact = new Contact(1L, "", "", true, null);
    User user = new User(1L, "", "", "", "", 1, LocalDate.now(), "", true,
            true, null, null, null, null, null, null,
            null, null, null, null, null,
            null, null, null);

    TrainingProgram trainingProgram = new TrainingProgram(1L, 1, "C# for beginner", LocalDate.now(), LocalDate.now(), BigDecimal.TEN,
            30,true, true, user, null, null, null);
    TrainingProgram trainingProgram1= new TrainingProgram(2L, 2, "Java for beginner", LocalDate.now(), LocalDate.now(), BigDecimal.TEN,
            30, true, true, user, null, null, null);
    TrainingProgram trainingProgram2 = new TrainingProgram(3L, 3, "Python for beginner", LocalDate.now(), LocalDate.now(), BigDecimal.TEN,
            30, true, true, user, null, null, null);
    TrainingProgram trainingProgram3 = new TrainingProgram(4L, 4, "Pascal for beginner", LocalDate.now(), LocalDate.now(), BigDecimal.TEN,
            30, true, true, user, null, null, null);

    TrainingClass trainingClass = new TrainingClass(1L, "Class Name 1", " Code113", LocalDate.now(),
                Time.valueOf("09:00:00"), Time.valueOf("11:00:00"), BigDecimal.ONE, 10, 4, 5, 6, "1", LocalDate.now(),
                LocalDate.now(), LocalDate.now(), LocalDate.now(), 0, true, attendee, trainingProgram, location, fsu,
                contact, user, user, user, user, null, null, null);
    TrainingClass trainingClass1 = new TrainingClass(2L, "Class Name 2", " Code113", LocalDate.now(),
                Time.valueOf("09:00:00"), Time.valueOf("11:00:00"), BigDecimal.ONE, 10, 4, 5, 6, "1", LocalDate.now(),
                LocalDate.now(), LocalDate.now(), LocalDate.now(),0 , true, attendee, trainingProgram1, location, fsu,
                contact, user, user, user, user, null, null, null);
    TrainingClass trainingClass2 = new TrainingClass(3L, "Class Name 3", " Code113", LocalDate.now(),
                Time.valueOf("09:00:00"), Time.valueOf("11:00:00"), BigDecimal.ONE, 10, 4, 5, 6, "1", LocalDate.now(),
                LocalDate.now(), LocalDate.now(), LocalDate.now(),0 , true, attendee, trainingProgram2, location, fsu,
                contact, user, user, user, user, null, null, null);
    TrainingClass trainingClass3 = new TrainingClass(4L, "Class Name 4", " Code113", LocalDate.now(),
                Time.valueOf("09:00:00"), Time.valueOf("11:00:00"), BigDecimal.ONE, 10, 4, 5, 6, "1", LocalDate.now(),
                LocalDate.now(), LocalDate.now(), LocalDate.now(),0 , true, attendee, trainingProgram3, location, fsu,
                contact, user, user, user, user, null, null, null);

    /**
     * Method under test: {@link TrainingClassService#create(TrainingClassDTO)}
     */
//    @Test
//   @Disabled
//    void canCreateNewTrainingClass() {
//        TrainingClass tcAfterSave = new TrainingClass(1L, "Class Name 1", " Code113", LocalDate.now(),
//                Time.valueOf("09:00:00"), Time.valueOf("11:00:00"), BigDecimal.ONE, 10, 4, 5, 6, "1", LocalDate.now(),
//                LocalDate.now(), LocalDate.now(), LocalDate.now(),0 , true, attendee, trainingProgram, location, fsu,
//                contact, user, user, user, user, null, null, null);
//
//        TrainingClassDTO dto = new TrainingClassDTO(null, "Class Name 1", " Code113", LocalDate.now(),
//                Time.valueOf("09:00:00"), Time.valueOf("11:00:00"), BigDecimal.ONE, 10, 4, 5, 6, "1", LocalDate.now(),
//                LocalDate.now(), LocalDate.now(), LocalDate.now(), 0, true, location.getId(),location.getLocationName(), attendee.getId(),
//                attendee.getAttendeeName(), trainingProgram.getId(), trainingProgram.getName(), fsu.getId(), fsu.getFsuName(),
//                contact.getId(), contact.getContactEmail(), user.getId(),user.getFullName(), user.getId(),user.getFullName(), user.getId(),
//                user.getFullName(), user.getId(),user.getFullName());
//
//        when(trainingClassRepository.save(any())).thenReturn(tcAfterSave);
//
//        Long result = trainingClassService.create(dto);
//        assertEquals(1L, result);
//        verify(trainingClassRepository).save(any());
//    }
//
//    /**
//     * Method under test: {@link TrainingClassService#create(TrainingClassDTO)}
//     */
//    @Test
//    @Disabled
//    void canNotCreateNewTrainingClassIfReferenceObjectNotExist() {
//
//        TrainingClassDTO dto = new TrainingClassDTO(null, "Class Name 1", " Code113", LocalDate.now(),
//                Time.valueOf("09:00:00"), Time.valueOf("12:00:00"), BigDecimal.ONE, 10, 4, 5, 6, "1", LocalDate.now(),
//                LocalDate.now(), LocalDate.now(), LocalDate.now(),0, true, location.getId(), attendee.getId(), trainingProgram.getId(), fsu.getId(),
//                contact.getId(), user.getId(), user.getId(), 7L, user.getId());
//
//        when(trainingClassRepository.save(any())).thenReturn(null);
//
//        Long result = trainingClassService.create(dto);
//        assertNull(result);
//        verify(trainingClassRepository).save(any());
//    }

    @Test
    void getAllClass(){
        List<TrainingClass> trainingClassList = new ArrayList<>();
        trainingClassList.add(trainingClass);
        trainingClassList.add(trainingClass1);
        trainingClassList.add(trainingClass2);
        trainingClassList.add(trainingClass3);

        List<String> search = new ArrayList<>();
        search.add("");

        List<Long> classId = new ArrayList<>();

        when(trainingClassRepository.getListClass(true, null, null, null,
                null, "", null, null, 0L, classId, "",
                Sort.by(Sort.Direction.ASC, "className"))).thenReturn(trainingClassList);
        Page<TrainingClassDTO> trainingClassDTOList = trainingClassService.getListClass(true, null,
                null, null, null, "", null, null, 0L, 0L,
                search, new String[] {"className","asc"}, Optional.of(0), Optional.of(2));
        assertEquals(2, trainingClassDTOList.getContent().size());
        verify(trainingClassRepository, times(1)).getListClass(true, null, null, null,
                null, "", null, null, 0L, classId, "",
                Sort.by(Sort.Direction.ASC, "className"));
    }

    /**
     * Method under test: {@link TrainingClassService#deleteTrainingClass(Long)}
     */
    @Test
    void canDeleteTrainingClass() {
        when(trainingClassRepository.save((TrainingClass) any())).thenReturn(new TrainingClass());
        when(trainingClassRepository.findById((Long) any())).thenReturn(Optional.of(new TrainingClass()));
        assertTrue(trainingClassService.deleteTrainingClass(1L));
        verify(trainingClassRepository).save((TrainingClass) any());
        verify(trainingClassRepository).findById((Long) any());
    }

    /**
     * Method under test: {@link TrainingClassService#deleteTrainingClass(Long)}
     */
    @Test
    void itShouldThrowIfCanNotFindTrainingClassById() {
        when(trainingClassRepository.save((TrainingClass) any()))
                .thenThrow(new EntityNotFoundException("An error occurred"));
        when(trainingClassRepository.findById((Long) any())).thenReturn(Optional.of(new TrainingClass()));
        assertThrows(EntityNotFoundException.class, () -> trainingClassService.deleteTrainingClass(1L));
        verify(trainingClassRepository).save((TrainingClass) any());
        verify(trainingClassRepository).findById((Long) any());
    }





    /**
     * Method under test: {@link TrainingClassService#getAllDetails(Long)}
     */
    @Test
    void canGetTrainingClassById() {

        when(trainingClassRepository.findByIdAndStatus(1L, true)).thenReturn(Optional.of(new TrainingClass()));
        TrainingClassDTO dto = trainingClassService.getAllDetails(1L);
        assertNotNull(dto);
        verify(trainingClassRepository).findByIdAndStatus(1L, true);
    }


    @Test
    void itShouldThrowExceptionWhenTrainingClassIdNotFound() {
        when(trainingClassRepository.findByIdAndStatus(1L, true)).thenReturn(Optional.empty());
        assertThrows(Exception.class, () -> trainingClassService.getAllDetails(1L));
        verify(trainingClassRepository).findByIdAndStatus(1L, true);
    }
}

