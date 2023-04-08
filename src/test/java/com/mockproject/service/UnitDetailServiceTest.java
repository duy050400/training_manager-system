package com.mockproject.service;

import com.mockproject.dto.UnitDetailDTO;
import com.mockproject.entity.*;
import com.mockproject.mapper.UnitDetailMapper;
import com.mockproject.repository.SyllabusRepository;
import com.mockproject.repository.UnitDetailRepository;
import com.mockproject.repository.UnitRepository;
import com.mockproject.service.interfaces.ITrainingMaterialService;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ContextConfiguration(classes = {UnitDetailService.class})
@ExtendWith(SpringExtension.class)
class UnitDetailServiceTest {

    @MockBean
    private UnitDetailRepository unitDetailRepository;

    @MockBean
    private UnitRepository unitRepository;

    @MockBean
    private SyllabusRepository syllabusRepository;

    @MockBean
    private ITrainingMaterialService trainingMaterialService;

    @Autowired
    private UnitDetailService unitDetailService;

    User user = new User(1L, "abc@gmail.com", "***", "Pham Quoc Thinh", "avatar", 1, LocalDate.now(), "0938081927", true, true,
            null, null, null, null, null, null, null, null, null, null, null, null, null, null);

    Syllabus syllabus = new Syllabus(1L, "ABC", "SLB1", "1.0", "All level",
            1, BigDecimal.TEN, 10,"Technical Requirements", "Course Objectives",
            LocalDate.now(), LocalDate.now(), BigDecimal.TEN, BigDecimal.TEN, BigDecimal.TEN, BigDecimal.TEN, BigDecimal.TEN,
            BigDecimal.TEN, "Training Description", "ReTest Description", "Marking Description",
            "Waiver CriteriaDes", "Other Description", true, true, user, new User(),
            null, null);

    Session session1 = new Session(1L,1,true,syllabus, null);

    Unit unit1 = new Unit(1L, "Unit title 123", 1, BigDecimal.TEN, true, session1, null, null);
    Unit unit2 = new Unit(2L, "Unit title 234", 2, BigDecimal.TEN, true, session1, null, null);

    UnitDetail unitDetail1 = new UnitDetail(1L, "Unit Detail 1", BigDecimal.TEN, true,
            true, unit1, new DeliveryType(), new OutputStandard(), null);
    UnitDetail unitDetail2 = new UnitDetail(2L, "Unit Detail 2", BigDecimal.TEN, true,
            false, unit1, new DeliveryType(), new OutputStandard(), null);
    UnitDetail unitDetail3 = new UnitDetail(3L, "Unit Detail 3", BigDecimal.TEN, true,
            true, unit1, new DeliveryType(), new OutputStandard(), null);
    UnitDetail unitDetail4 = new UnitDetail(4L, "Unit Detail 4", BigDecimal.TEN, true,
            true, unit2, new DeliveryType(), new OutputStandard(), null);

    /**
     * Method under test: {@link UnitDetailService#listByUnitIdTrue(Long)}
     */
    @Test
    void canListUnitDetailWithStatusTrueByUnitId() {
        List<UnitDetail> list = new ArrayList<>();
        list.add(unitDetail1);
        list.add(unitDetail2);
        list.add(unitDetail3);
        list.add(unitDetail4);

        Unit unit = new Unit();
        unit.setId(1L);

        when(unitDetailRepository.findByUnitAndStatus(unit, true)).thenReturn(list.stream()
                .filter(p -> p.isStatus() && p.getUnit().getId() == unit.getId()).toList());

        List<UnitDetailDTO> result = unitDetailService.listByUnitIdTrue(unit.getId());

        assertEquals(2, result.size());
        assertEquals(1L, result.get(0).getId());
        assertEquals("Unit Detail 1",result.get(0).getTitle());
        assertEquals(3L, result.get(1).getId());
        assertEquals("Unit Detail 3",result.get(1).getTitle());

        verify(unitDetailRepository).findByUnitAndStatus((Unit) any(), anyBoolean());
    }

    @Test
    void getAllUnitDetailByUnitID(){
        List<UnitDetail> list = new ArrayList<>();
        list.add(unitDetail1);
        list.add(unitDetail2);
        list.add(unitDetail3);

        when(unitDetailRepository.findByUnitIdAndStatus(unit1.getId(), true)).thenReturn(Optional.of(list));

        List<UnitDetailDTO> result = unitDetailService.getAllUnitDetailByUnitId(unit1.getId(), true);

        assertEquals(3, result.size());

        verify(unitDetailRepository,times(1)).findByUnitIdAndStatus(unit1.getId(), true);
    }

    @Test
    void createUnitDetailTest(){
        List<UnitDetailDTO> list = new ArrayList<>();
        list.add(UnitDetailMapper.INSTANCE.toDTO(unitDetail1));
        list.add(UnitDetailMapper.INSTANCE.toDTO(unitDetail2));
        list.add(UnitDetailMapper.INSTANCE.toDTO(unitDetail3));

        when(unitRepository.findByIdAndStatus(unit1.getId(), true)).thenReturn(Optional.ofNullable(unit1));
        when(unitDetailRepository.save((any()))).thenReturn(unitDetail1);
        when(unitRepository.save(unit1)).thenReturn(unit1);

        boolean result = unitDetailService.createUnitDetail(unit1.getId(), list, new User());

        assertEquals(true, result);

        verify(unitDetailRepository,times(3)).save(any());
    }

    @Test
    void getUnitDetailByIdTest(){

        when(unitDetailRepository.findByIdAndStatus(unitDetail1.getId(), true)).thenReturn(Optional.ofNullable(unitDetail1));

        UnitDetail result = unitDetailService.getUnitDetailById(unitDetail1.getId(), true);

        assertEquals("Unit Detail 1", result.getTitle());

        verify(unitDetailRepository,times(1)).findByIdAndStatus(unitDetail1.getId(), true);
    }

    @Test
    void deleteUnitDetailTest(){
        List<UnitDetail> list = new ArrayList<>();
        list.add((unitDetail1));
        list.add((unitDetail2));
        list.add((unitDetail3));

        when(unitDetailRepository.findByUnitIdAndStatus(unit1.getId(), true)).thenReturn(Optional.of(list));
        when(unitDetailRepository.findByIdAndStatus(any(), eq(true))).thenReturn(Optional.ofNullable(unitDetail1));
        when(unitDetailRepository.save(any())).thenReturn(unitDetail1);

        boolean result = unitDetailService.deleteUnitDetails(unit1.getId(), true);

        assertEquals(true, result);

        verify(unitDetailRepository,times(3)).save(any());
    }

    @Test
    void toggleUnitDetailTypeTest(){

        when(unitDetailRepository.findByIdAndStatus(unitDetail1.getId(), true)).thenReturn(Optional.ofNullable(unitDetail1));
        when(unitDetailRepository.save(any())).thenReturn(unitDetail1);

        boolean result = unitDetailService.toggleUnitDetailType(unitDetail1.getId(), true);

        assertEquals(true, result);

        verify(unitDetailRepository,times(1)).save(any());
    }


}

