package com.mockproject.service;

import com.mockproject.dto.UnitDTO;
import com.mockproject.entity.*;
import com.mockproject.mapper.UnitMapper;
import com.mockproject.repository.*;
import com.mockproject.service.interfaces.IUnitDetailService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ContextConfiguration(classes = {UnitService.class})
@ExtendWith(SpringExtension.class)
class UnitServiceTest {
    @MockBean
    private UnitRepository unitRepository;
    @MockBean
    private IUnitDetailService unitDetailService;
    @MockBean
    private SessionRepository sessionRepository;
    @MockBean
    private SyllabusRepository syllabusRepository;
    @MockBean
    private TrainingClassRepository trainingClassRepository;
    @MockBean
    private TrainingClassUnitInformationRepository trainingClassUnitInformationRepository;

    @Autowired
    private UnitService unitService;


    // Create Syllabus
    Syllabus syllabus1 = new Syllabus(1L, "Syllabus number 1", "SLB1", "1.0", "All level",
            1, BigDecimal.TEN, 10,"Technical Requirements", "Course Objectives",
            LocalDate.now(), LocalDate.now(), BigDecimal.TEN, BigDecimal.TEN, BigDecimal.TEN, BigDecimal.TEN, BigDecimal.TEN,
            BigDecimal.TEN, "Training Description", "ReTest Description", "Marking Description",
            "Waiver CriteriaDes", "Other Description", true, true, new User(), new User(),
            null, null);
    // Create Session
    Session s1 = new Session(1L, 1, true, syllabus1, null);
    Session s2 = new Session(2L, 2, true, syllabus1, null);

    // Create Unit
    Unit unit1 = new Unit(1L, "Unit title 123", 1, BigDecimal.TEN, true, s1, null, null);
    Unit unit2 = new Unit(2L, "Unit title 234", 2, BigDecimal.TEN, true, s2, null, null);
    Unit unit3 = new Unit(3L, "Unit title 345", 3, BigDecimal.TEN, true, s1, null, null);

    /**
     * Method under test: {@link UnitService#listBySessionId(Long)}
     */
    @Test
    void canListUnitByGivenSessionId() {
        ArrayList<Unit> unitList = new ArrayList<>();
        unitList.add(unit1);
        unitList.add(unit2);
        unitList.add(unit3);

        Long sessionId = 1L;
        Session session = new Session();
        session.setId(sessionId);
        when(unitRepository.findBySession(session)).thenReturn(unitList.stream().filter(p -> p.getSession().getId() == s1.getId()).toList());
        List<UnitDTO> result = unitService.listBySessionId(1L);
        assertEquals(2, result.size());
        assertEquals("Unit title 123", result.get(0).getUnitTitle());
        verify(unitRepository).findBySession((Session) any());
    }

    @Test
    void getAllUnitBySessionIdTest(){
        List<Unit> list = new ArrayList<>();
        list.add(unit1);
        list.add(unit3);

        when(unitRepository.findUnitBySessionIdAndStatus(s1.getId(), true)).thenReturn(Optional.of(list));

        List<UnitDTO> result = unitService.getAllUnitBySessionId(s1.getId(), true);

        assertEquals(2, result.size());

        verify(unitRepository,times(1)).findUnitBySessionIdAndStatus(s1.getId(), true);
    }

    @Test
    void createUnitTest(){
        List<UnitDTO> list = new ArrayList<>();
        list.add(UnitMapper.INSTANCE.toDTO(unit1));
        list.add(UnitMapper.INSTANCE.toDTO(unit3));

        when(sessionRepository.findByIdAndStatus(s1.getId(), true)).thenReturn(Optional.ofNullable(s1));
        when(unitRepository.save(any())).thenReturn(unit1);
        when(unitRepository.findByIdAndStatus(any(), eq(true))).thenReturn(Optional.ofNullable(unit1));
        when(syllabusRepository.findByIdAndStateAndStatus(any(),eq(true),eq(true))).thenReturn(Optional.ofNullable(syllabus1));

        boolean result = unitService.createUnit(s1.getId(), list, new User());

        assertTrue(result);

        verify(unitRepository,times(2)).save(any());
    }


    /**
     * Method under test: {@link UnitService#getListUnitsByTrainingClassId(Long)}
     */
    @Test
    void canGetListUnitsByTrainingClassId() {

        TrainingClass tc = new TrainingClass();
        tc.setId(1L);
        tc.setStatus(true);

        AtomicReference<Long> index = new AtomicReference<>(1L);

//        List<TrainingClassUnitInformation> infos = new ArrayList<>(Arrays.asList(new TrainingClassUnitInformation(), new TrainingClassUnitInformation(), new TrainingClassUnitInformation()));
        List<TrainingClassUnitInformation> infos = Stream.of(1, 2, 3, 4)
                        .map(i -> new TrainingClassUnitInformation())
                                .toList();

        infos.forEach(i -> {
            i.setId(index.get());

            Unit unit = new Unit();
            unit.setId(index.get());
            unit.setStatus(true);
            i.setUnit(unit);

            i.setTrainingClass(tc);
            i.setStatus(index.get() % 2 == 0);

            index.updateAndGet(v -> v + 1);
        });

        when(trainingClassRepository.findByIdAndStatus(1L, true)).thenReturn(Optional.of(tc));
        List<TrainingClassUnitInformation> list = infos.stream().filter(TrainingClassUnitInformation::isStatus).toList();
        when(trainingClassUnitInformationRepository.findByTrainingClassAndStatus(tc, true)).thenReturn(Optional.of(list));
        list.forEach(i -> when(unitRepository.findByIdAndStatus(i.getUnit().getId(), true))
                .thenReturn(Optional.of(i.getUnit())));

        List<Unit> imp = unitService.getListUnitsByTrainingClassId(1L);
        assertNotNull(imp);
        assertEquals(2, imp.size());

        verify(trainingClassRepository).findByIdAndStatus(1L, true);
        verify(trainingClassUnitInformationRepository).findByTrainingClassAndStatus(tc, true);
        list.forEach(i -> verify(unitRepository).findByIdAndStatus(i.getUnit().getId(), true));
    }



    @Test
    void itShouldThrowExceptionWhenTrainingClassUnitsNotFound() {

        when(trainingClassRepository.findByIdAndStatus(1L, true)).thenReturn(null);
        assertThrows(Exception.class, () -> unitService.getListUnitsByTrainingClassId(1L));

        when(trainingClassUnitInformationRepository.findByTrainingClassAndStatus(null, true)).thenReturn(Optional.empty());
        assertThrows(Exception.class, () -> unitService.getListUnitsByTrainingClassId(1L));

        when(unitRepository.findByIdAndStatus(null, true)).thenReturn(Optional.empty());
        assertThrows(Exception.class, () -> unitService.getListUnitsByTrainingClassId(1L));

        verify(trainingClassRepository, times(3)).findByIdAndStatus(1L, true);
    }




}


