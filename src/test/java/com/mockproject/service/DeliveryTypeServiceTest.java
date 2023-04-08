package com.mockproject.service;

import com.mockproject.dto.DeliveryTypeDTO;
import com.mockproject.entity.DeliveryType;
import com.mockproject.entity.Unit;
import com.mockproject.entity.UnitDetail;
import com.mockproject.repository.DeliveryTypeRepository;
import com.mockproject.repository.UnitDetailRepository;
import com.mockproject.service.interfaces.IUnitService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ContextConfiguration(classes = {DeliveryTypeService.class})
@ExtendWith(SpringExtension.class)
class DeliveryTypeServiceTest {
    @MockBean
    private DeliveryTypeRepository deliveryTypeRepository;

    @MockBean
    private UnitDetailRepository unitDetailRepository;

    @Autowired
    private DeliveryTypeService deliveryTypeService;

    @MockBean
    private IUnitService unitService;

    DeliveryType deliveryType1 = new DeliveryType(1L, "On Meeting", true, null);
    DeliveryType deliveryType2 = new DeliveryType(2L, "Offline", false, null);
    DeliveryType deliveryType3 = new DeliveryType(3L, "Practice", true, null);


    /**
     * Method under test: {@link DeliveryTypeService#getAllDeliveryTypesByTrainingClassId(Long)}
     */
    @Test
    void canGetAllDeliveryTypesByTrainingClassId() {
        Unit u1 = new Unit();
        u1.setId(1L);
        u1.setStatus(true);

        Unit u2 = new Unit();
        u2.setId(2L);
        u2.setStatus(false);
        List<Unit> units = new ArrayList<>(Arrays.asList(u1, u2));


        UnitDetail ud1 = new UnitDetail();
        ud1.setUnit(u1);
        ud1.setDeliveryType(deliveryType1);
        ud1.setStatus(true);

        UnitDetail ud2 = new UnitDetail();
        ud2.setUnit(u1);
        ud2.setDeliveryType(deliveryType1);
        ud2.setStatus(true);

        UnitDetail ud3 = new UnitDetail();
        ud3.setUnit(u1);
        ud3.setDeliveryType(deliveryType3);
        ud3.setStatus(true);

        UnitDetail ud4 = new UnitDetail();
        ud4.setUnit(u2);
        ud4.setDeliveryType(deliveryType3);
        ud4.setStatus(true);

        UnitDetail ud5 = new UnitDetail();
        ud5.setUnit(u1);
        ud5.setDeliveryType(deliveryType3);
        ud5.setStatus(false);
        List<UnitDetail> unitDetails = new ArrayList<>(Arrays.asList(ud1, ud2, ud3, ud4, ud5));


        List<Unit> listUnits = units.stream().filter(Unit::isStatus).toList();
        when(unitService.getListUnitsByTrainingClassId(1L)).thenReturn(listUnits);


        List<UnitDetail> listUDs = unitDetails.stream().filter(UnitDetail::isStatus).toList();
        when(unitDetailRepository.findByUnitInAndStatus(listUnits, true)).thenReturn(Optional.of(listUDs));

//        when(deliveryTypeRepository.findByIdAndStatus(ud1.getDeliveryType().getId(), true))
//                .thenReturn(Optional.of(deliveryType1));
//        when(deliveryTypeRepository.findByIdAndStatus(ud2.getDeliveryType().getId(), true))
//                .thenReturn(Optional.of(deliveryType1));
//        when(deliveryTypeRepository.findByIdAndStatus(ud3.getDeliveryType().getId(), true))
//                .thenReturn(Optional.of(deliveryType3));
//        when(deliveryTypeRepository.findByIdAndStatus(ud4.getDeliveryType().getId(), true))
//                .thenReturn(Optional.of(deliveryType3));

        listUDs.forEach(i -> when(deliveryTypeRepository.findByIdAndStatus(i.getDeliveryType().getId(), true))
                .thenReturn(Optional.of(listUDs.get(listUDs.indexOf(i)).getDeliveryType())));

        List<DeliveryTypeDTO> dtos = deliveryTypeService.getAllDeliveryTypesByTrainingClassId(1L);
        assertEquals(2, dtos.size());

        verify(unitService).getListUnitsByTrainingClassId(1L);
        verify(unitDetailRepository).findByUnitInAndStatus(listUnits, true);
        listUDs.forEach(i -> verify(deliveryTypeRepository, atLeastOnce()).findByIdAndStatus(i.getDeliveryType().getId(), true));
    }


    @Test
    void itShouldThrowExceptionWhenTrainingClassDeliveryTypesNotFound() {

        Unit u1 = new Unit();
        u1.setId(1L);
        u1.setStatus(true);

        Unit u2 = new Unit();
        u2.setId(2L);
        u2.setStatus(false);
        List<Unit> units = new ArrayList<>(Arrays.asList(u1, u2));


        UnitDetail ud1 = new UnitDetail();
        ud1.setUnit(u1);
        ud1.setDeliveryType(deliveryType1);
        ud1.setStatus(false);


//      1st case
        when(unitService.getListUnitsByTrainingClassId(1L)).thenReturn(null);
        when(unitDetailRepository.findByUnitInAndStatus(null, true)).thenReturn(Optional.empty());
        assertThrows(Exception.class, () -> deliveryTypeService.getAllDeliveryTypesByTrainingClassId(1L));
        verify(unitDetailRepository).findByUnitInAndStatus(null, true);

//      2nd case
        when(unitService.getListUnitsByTrainingClassId(1L)).thenReturn(units);
        when(unitDetailRepository.findByUnitInAndStatus(units, true)).thenReturn(Optional.of(new ArrayList<>(List.of(ud1))));
        when(deliveryTypeRepository.findByIdAndStatus(ud1.getDeliveryType().getId(), true)).thenReturn(Optional.empty());
        assertThrows(Exception.class, () -> deliveryTypeService.getAllDeliveryTypesByTrainingClassId(1L));


        verify(unitService, times(2)).getListUnitsByTrainingClassId(1L);
        verify(unitDetailRepository).findByUnitInAndStatus(units, true);
        verify(deliveryTypeRepository).findByIdAndStatus(ud1.getDeliveryType().getId(), true);
    }

    /**
     * Method under test: {@link DeliveryTypeService#getByIdTrue(Long)}
     */
//    @Test
//    @Disabled
//    void canGetDeliveryTypeByIdTrue() {
//
//        Long id = 3L;
//
//        when(deliveryTypeRepository.findByIdAndStatus(id, true).orElseThrow()).thenReturn(deliveryType3);
//        DeliveryTypeDTO result = deliveryTypeService.getByIdTrue(3L);
//        assertEquals(3L, result.getId());
//        assertTrue(result.isStatus());
//        assertEquals("Practice", result.getTypeName());
//        verify(deliveryTypeRepository).findByIdAndStatus(3L, true);
//    }
}

