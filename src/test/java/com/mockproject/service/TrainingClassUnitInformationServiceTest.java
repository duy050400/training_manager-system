package com.mockproject.service;

import com.mockproject.dto.TrainingClassUnitInformationDTO;
import com.mockproject.entity.*;
import com.mockproject.repository.TrainingClassUnitInformationRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ContextConfiguration(classes = {TrainingClassUnitInformationService.class})
@ExtendWith(SpringExtension.class)
class TrainingClassUnitInformationServiceTest {
    @MockBean
    private TrainingClassUnitInformationRepository trainingClassUnitInformationRepository;

    @Autowired
    private TrainingClassUnitInformationService trainingClassUnitInformationService;

    /**
     * Method under test: {@link TrainingClassUnitInformationService#saveList(List)}
     */

//    TrainingClassUnitInformation ui1 = new TrainingClassUnitInformation(null, true, new User(), new Unit(), new TrainingClass(), new Tower());
//    TrainingClassUnitInformation ui2 = new TrainingClassUnitInformation(null, true, new User(), new Unit(), new TrainingClass(), new Tower());
//    TrainingClassUnitInformation ui3 = new TrainingClassUnitInformation(null, true, new User(), new Unit(), new TrainingClass(), new Tower());
    @Test
    void itShouldReturnTrueWhenListInformationSavedSuccessfully() {
        TrainingClass tc = new TrainingClass();
        tc.setId(1L);
        User user = new User();
        user.setId(1L);
        Unit unit = new Unit();
        unit.setId(1L);
        Tower tower = new Tower();
        tower.setId(1L);

        TrainingClassUnitInformation ui = new TrainingClassUnitInformation(null, true, user, unit, tc, tower);
        List<TrainingClassUnitInformation> listBefore = new ArrayList<>();
        listBefore.add(ui);
        listBefore.add(ui);
        listBefore.add(ui);

        List<TrainingClassUnitInformation> listAfter = new ArrayList<>();
        listAfter.add(new TrainingClassUnitInformation(1L, true, user, unit, tc, tower));
        listAfter.add(new TrainingClassUnitInformation(2L, true, user, unit, tc, tower));
        listAfter.add(new TrainingClassUnitInformation(3L, true, user, unit, tc, tower));


        TrainingClassUnitInformationDTO dto = new TrainingClassUnitInformationDTO(null, true, 1L,null, 1L,null, 1L,null, 1L,null);
        List<TrainingClassUnitInformationDTO> listDto = new ArrayList<>();
        listDto.add(dto);
        listDto.add(dto);
        listDto.add(dto);

        when(trainingClassUnitInformationRepository.saveAll(listBefore))
                .thenReturn(listAfter);
        assertTrue(trainingClassUnitInformationService.saveList(listDto));
        verify(trainingClassUnitInformationRepository).saveAll(listBefore);
    }

    /**
     * Method under test: {@link TrainingClassUnitInformationService#saveList(List)}
     */
    @Test
    void itShouldReturnFalseWhenListInformationSavedSuccessfully() {

        TrainingClassUnitInformation ui = new TrainingClassUnitInformation(null, true, new User(), new Unit(), new TrainingClass(), new Tower());
        List<TrainingClassUnitInformation> listBefore = new ArrayList<>();
        listBefore.add(ui);
        listBefore.add(ui);
        listBefore.add(ui);

        TrainingClassUnitInformationDTO dto = new TrainingClassUnitInformationDTO(null, true, null,null,null,null,null, null, null, null);
        List<TrainingClassUnitInformationDTO> listDto = new ArrayList<>();
        listDto.add(dto);
        listDto.add(dto);
        listDto.add(dto);

        when(trainingClassUnitInformationRepository.saveAll(listBefore))
                .thenReturn(Collections.emptyList());
        assertFalse(trainingClassUnitInformationService.saveList(listDto));
        verify(trainingClassUnitInformationRepository).saveAll(listBefore);
    }
}

