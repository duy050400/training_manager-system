package com.mockproject.service;

import com.mockproject.dto.FsuDTO;
import com.mockproject.entity.Fsu;
import com.mockproject.entity.TrainingClass;
import com.mockproject.mapper.FsuMapper;
import com.mockproject.repository.FsuRepository;
import com.mockproject.repository.TrainingClassRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ContextConfiguration(classes = {FsuService.class})
@ExtendWith(SpringExtension.class)
class FsuServiceTest {
    @MockBean
    private FsuRepository fsuRepository;
    @MockBean
    private TrainingClassRepository trainingClassRepository;


    @Autowired
    private FsuService fsuService;

    Fsu fsu1 = new Fsu(1L, "Fsu 1" , "Desc 1", true, null);
    Fsu fsu2 = new Fsu(2L, "Fsu 2" , "Desc 2", false, null);
    Fsu fsu3 = new Fsu(3L, "Fsu 3" , "Desc 3", true, null);

    /**
     * Method under test: {@link FsuService#listAllTrue()}
     */
    @Test
    void canListAllFsuWithStatusTrue() {
        List<Fsu> list = new ArrayList<>();
        list.add(fsu1);
        list.add(fsu2);
        list.add(fsu3);

        when(fsuRepository.findByStatus(true)).thenReturn(list.stream().filter(Fsu::isStatus).toList());

        List<FsuDTO> result = fsuService.listAllTrue();

        assertEquals(2, result.size());
        assertEquals("Fsu 1", result.get(0).getFsuName());
        assertTrue(result.stream().filter(p-> !p.isStatus()).toList().isEmpty());

        verify(fsuRepository).findByStatus(anyBoolean());
    }

    /**
     * Method under test: {@link FsuService#getFsuByTrainingClassId(Long)}
     */
    @Test
    void canGetFsuByTrainingClassId() {

        TrainingClass tc = new TrainingClass();
        tc.setFsu(fsu1);

        when(trainingClassRepository.findByIdAndStatus(1L, true)).thenReturn(Optional.of(tc));
        FsuDTO result = fsuService.getFsuByTrainingClassId(1L);
        assertEquals(FsuMapper.INSTANCE.toDTO(fsu1), result);
        assertTrue(result.isStatus());
        verify(trainingClassRepository).findByIdAndStatus(result.getId(), true);
    }

    @Test
    void itShouldThrowExceptionWhenTrainingClassIdNotFoundOrFsuDisabled() {

        TrainingClass tc = new TrainingClass();
        tc.setFsu(fsu2);

        when(trainingClassRepository.findByIdAndStatus(2L, true)).thenReturn(Optional.of(tc));
        assertThrows(NullPointerException.class,
                () -> fsuService.getFsuByTrainingClassId(2L));
        verify(trainingClassRepository).findByIdAndStatus(2L, true);


        when(trainingClassRepository.findByIdAndStatus(1L, true)).thenReturn(Optional.empty());
        assertThrows(Exception.class,
                () -> fsuService.getFsuByTrainingClassId(1L));
        verify(trainingClassRepository).findByIdAndStatus(1L, true);

    }
}

