package com.mockproject.service;

import com.mockproject.dto.TowerDTO;
import com.mockproject.entity.*;
import com.mockproject.repository.TowerRepository;
import com.mockproject.repository.TrainingClassRepository;
import com.mockproject.repository.TrainingClassUnitInformationRepository;
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
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ContextConfiguration(classes = {TowerService.class})
@ExtendWith(SpringExtension.class)
class TowerServiceTest {
    @MockBean
    private TowerRepository towerRepository;
    @MockBean
    private TrainingClassRepository trainingClassRepository;
    @MockBean
    private TrainingClassUnitInformationRepository trainingClassUnitInformationRepository;
    @MockBean
    private IUnitService unitService;

    @Autowired
    private TowerService towerService;

    Location location1 = new Location(1L, "Location 1", "123 Le Loi", true, null, null);
    Location location2 = new Location(2L, "Location 2", "333 Le Hong Phong", false, null, null);


    Tower tower1 = new Tower(1L, "FTown 1", "123 Le Loi", true, location1, null);
    Tower tower2 = new Tower(2L, "FTown 2", "66 Nguyen Trai", false, location2, null);
    Tower tower3 = new Tower(3L, "FTown 3", "999 Ba Trieu", true, location1, null);


    /**
     * Method under test: {@link TowerService#listByTowerIdTrue(Long)}
     */
    @Test
    void canListTowerWithStatusTrueByGivenLocationId() {

        List<Tower> list = new ArrayList<>();
        list.add(tower1);
        list.add(tower2);
        list.add(tower3);

        Location location = new Location();
        location.setId(1L);

        when(towerRepository.findByLocationAndStatus(location, true))
                .thenReturn(list.stream().filter(p -> p.isStatus() && p.getLocation().getId() == location.getId()).toList());

        List<TowerDTO> result = towerService.listByTowerIdTrue(location.getId());
        assertEquals(2, result.size());
        assertEquals(1L, result.get(0).getId());
        assertEquals("FTown 1", result.get(0).getTowerName());
        assertEquals(3L, result.get(1).getId());
        assertEquals("999 Ba Trieu", result.get(1).getAddress());
        verify(towerRepository).findByLocationAndStatus(location, true);
    }

    /**
     * Method under test: {@link TowerService#getAllTowersByTrainingClassId(Long)}
     */

    @Test
    void canGetAllTowersByTrainingClassId() {

        TrainingClass tc = new TrainingClass();

        TrainingClassUnitInformation t1 = new TrainingClassUnitInformation();
        t1.setTower(tower1);
        t1.setStatus(true);

        TrainingClassUnitInformation t2 = new TrainingClassUnitInformation();
        t2.setTower(tower1);
        t2.setStatus(true);

        TrainingClassUnitInformation t3 = new TrainingClassUnitInformation();
        t3.setTower(tower3);
        t3.setStatus(false);

        tc.setListTrainingClassUnitInformations(new ArrayList<>(Arrays.asList(t1, t2, t3)));

        List<TrainingClassUnitInformation> list = tc.getListTrainingClassUnitInformations()
                .stream().filter(TrainingClassUnitInformation::isStatus).toList();

        when(trainingClassRepository.findByIdAndStatus(1L, true)).thenReturn(Optional.of(tc));
        when(trainingClassUnitInformationRepository.findByTrainingClassAndStatus(tc, true)).thenReturn(Optional.of(list));
        list.forEach(i -> when(towerRepository.findByIdAndStatus(i.getTower().getId(), true))
                .thenReturn(Optional.of(list.get(list.indexOf(i)).getTower())));

        List<TowerDTO> dtos = towerService.getAllTowersByTrainingClassId(1L);
        assertEquals(1, dtos.size());

        verify(trainingClassRepository).findByIdAndStatus(1L, true);
        verify(trainingClassUnitInformationRepository).findByTrainingClassAndStatus(tc, true);
        list.forEach(i -> verify(towerRepository, times(2)).findByIdAndStatus(i.getTower().getId(), true));
    }


    @Test
    void itShouldThrowExceptionWhenTrainingClassTowersNotFound() {

        when(trainingClassRepository.findByIdAndStatus(1L, true)).thenReturn(Optional.empty());
        assertThrows(Exception.class, () -> towerService.getAllTowersByTrainingClassId(1L));

        when(trainingClassRepository.findByIdAndStatus(2L, true)).thenReturn(Optional.empty());
        when(trainingClassUnitInformationRepository.findByTrainingClassAndStatus(new TrainingClass(), true)).thenReturn(Optional.empty());
        assertThrows(Exception.class, () -> towerService.getAllTowersByTrainingClassId(2L));


        when(trainingClassRepository.findByIdAndStatus(3L, true)).thenReturn(Optional.empty());
        when(trainingClassUnitInformationRepository.findByTrainingClassAndStatus(new TrainingClass(), true)).thenReturn(Optional.empty());
        when(towerRepository.findByIdAndStatus(tower2.getId(), true))
                .thenReturn(Optional.empty());
        assertThrows(Exception.class, () -> towerService.getAllTowersByTrainingClassId(3L));
    }

    /**
     * Method under test {@link TowerService#getAllTowersForADateByTrainingClassId(Long, int)}
     */
    @Test
    void canGetAllTowersForADateByTrainingClassId() {

        // Create Training class
        TrainingClass tc = new TrainingClass();

        // Create list units
        List<Unit> units = new ArrayList<>(Arrays.asList(new Unit(), new Unit()));

        // Create id
        AtomicReference<Long> id = new AtomicReference<>(1L);

        // Assign value for each unit
        units.forEach(i -> {
            i.setId(id.get());
            id.getAndSet(id.get() + 1);
            i.setListTrainingClassUnitInformations(new ArrayList<>(List.of(new TrainingClassUnitInformation())));
            i.setStatus(true);
        });

        // reset id
        id.getAndSet(1L);

        // Create list TrainingClassUnitInfo
        List<TrainingClassUnitInformation> list = new ArrayList<>();

        // Assign value for Training class unit info
        units.forEach(i -> {
            TrainingClassUnitInformation t = i.getListTrainingClassUnitInformations().get(0);
            t.setId(id.get());
            id.getAndSet(id.get() + 1);
            t.setTrainingClass(tc);
            t.setUnit(i);
            t.setTower(i.getId() % 2 == 0 ? tower1 : tower3);
            t.setStatus(true);
            list.add(t);
        });


        when(trainingClassRepository.findByIdAndStatus(1L, true)).thenReturn(Optional.of(tc));
        when(unitService.getListUnitsInASessionByTrainingClassId(1L, 1)).thenReturn(units);
        units.forEach(i -> when(trainingClassUnitInformationRepository.findByUnitAndTrainingClassAndStatus(i, tc, true))
                .thenReturn(Optional.of(i.getListTrainingClassUnitInformations().get(0))));
        list.forEach(i -> when(towerRepository.findByIdAndStatus(i.getTower().getId(), true))
                .thenReturn(Optional.of(list.get(list.indexOf(i)).getTower())));

        List<TowerDTO> dtos = towerService.getAllTowersForADateByTrainingClassId(1L, 1);

        assertNotNull(dtos);
        assertEquals(2, dtos.size());

        verify(trainingClassRepository).findByIdAndStatus(1L, true);
        verify(unitService).getListUnitsInASessionByTrainingClassId(1L, 1);
        units.forEach(i -> verify(trainingClassUnitInformationRepository).findByUnitAndTrainingClassAndStatus(i, tc, true));
        list.forEach(i -> verify(towerRepository).findByIdAndStatus(i.getTower().getId(), true));
    }


    @Test
    void itShouldThrowExceptionWhenTrainingClassDateTowersNotFound() {

        TrainingClass tc = new TrainingClass();

        TrainingClassUnitInformation t = new TrainingClassUnitInformation();
        t.setTrainingClass(tc);
        t.setStatus(false);
        TrainingClassUnitInformation t2 = new TrainingClassUnitInformation();
        t.setTrainingClass(tc);
        t2.setStatus(true);
        t2.setTower(tower2);

        Unit u = new Unit(); u.setStatus(false);
        List<Unit> units = new ArrayList<>(List.of(u));

        when(trainingClassRepository.findByIdAndStatus(1L, true)).thenReturn(Optional.empty());
        assertThrows(Exception.class, () -> towerService.getAllTowersForADateByTrainingClassId(1L, 1));

        when(trainingClassRepository.findByIdAndStatus(1L, true)).thenReturn(Optional.of(tc));
        when(unitService.getListUnitsInASessionByTrainingClassId(1L, 1)). thenReturn(units);
        when(trainingClassUnitInformationRepository.findByUnitAndTrainingClassAndStatus(u, tc, true)).thenReturn(Optional.empty());
        assertThrows(Exception.class, () -> towerService.getAllTowersForADateByTrainingClassId(1L, 1));

        verify(trainingClassRepository, times(2)).findByIdAndStatus(1L, true);
        verify(unitService).getListUnitsInASessionByTrainingClassId(1L, 1);
        verify(trainingClassUnitInformationRepository).findByUnitAndTrainingClassAndStatus(u, tc, true);
    }
}

