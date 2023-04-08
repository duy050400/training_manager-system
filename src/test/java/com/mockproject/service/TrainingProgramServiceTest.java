package com.mockproject.service;

import com.mockproject.dto.TrainingProgramDTO;
import com.mockproject.entity.TrainingProgram;
import com.mockproject.repository.TrainingProgramRepository;
import com.mockproject.service.interfaces.IFileService;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ContextConfiguration(classes = {TrainingProgramService.class})
@ExtendWith(SpringExtension.class)
class TrainingProgramServiceTest {
    @MockBean
    private TrainingProgramRepository trainingProgramRepository;

    @MockBean
    private SyllabusService syllabusService;

    @MockBean
    private TrainingProgramSyllabusService trainingProgramSyllabusService;

    @MockBean
    private IFileService fileService;

    @Autowired
    private TrainingProgramService trainingProgramService;

    TrainingProgram tp1 = new TrainingProgram(1L,1, "C# for beginner", LocalDate.now(), LocalDate.now(), BigDecimal.TEN, 30, true, true, null, null, null, null);
    TrainingProgram tp2 = new TrainingProgram(2L,2, "Java for beginner", LocalDate.now(), LocalDate.now(), BigDecimal.TEN, 30, true, true, null, null, null, null);
    TrainingProgram tp3 = new TrainingProgram(3L,3, "Java for junior", LocalDate.now(), LocalDate.now(), BigDecimal.TEN, 30, true, false, null, null, null, null);

    /**
     * Method under test: {@link TrainingProgramService#searchByName(String)}
     */
    @Test
    void canGetTrainingProgramByGivenName() {
        List<TrainingProgram> list = new ArrayList<>();
        list.add(tp1);
        list.add(tp2);
        list.add(tp3);

        String searchName = "ava";

        when(trainingProgramRepository.findByNameContainingAndStatus(searchName, true))
                .thenReturn(list.stream().filter(p -> p.getName().contains(searchName) && p.isStatus()).toList());

        List<TrainingProgramDTO> result = trainingProgramService.searchByName(searchName);

        assertEquals(1, result.size());
        assertEquals("Java for beginner", result.get(0).getName());
        verify(trainingProgramRepository).findByNameContainingAndStatus((String) any(), anyBoolean());
    }
}

