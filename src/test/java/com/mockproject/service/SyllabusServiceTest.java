package com.mockproject.service;

import com.mockproject.dto.SyllabusDTO;
import com.mockproject.entity.*;
import com.mockproject.repository.OutputStandardRepository;
import com.mockproject.repository.SyllabusRepository;
import com.mockproject.repository.TrainingProgramSyllabusRepository;
import com.mockproject.repository.UnitDetailRepository;
import com.mockproject.service.interfaces.ISessionService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ContextConfiguration(classes = {SyllabusService.class})
@ExtendWith(SpringExtension.class)
public class SyllabusServiceTest {

    @MockBean
    private SyllabusRepository syllabusRepository;

    @MockBean
    private OutputStandardRepository outputStandardRepo;

    @MockBean
    private UnitDetailRepository unitDetailRepo;

    @MockBean
    private ISessionService sessionService;

    @MockBean
    private TrainingProgramSyllabusRepository trainingProgramSyllabusRepository;

    @Autowired
    private SyllabusService syllabusService;

    User user = new User(1L, "abc@gmail.com", "***", "Pham Quoc Thinh", "avatar", 1, LocalDate.now(), "0938081927", true, true,
            null, null, null, null, null, null, null, null, null, null, null, null, null, null);

    Syllabus syllabus1 = new Syllabus(1L, "Syllabus number 1", "SLB1", "1.0", "All level",
            1, BigDecimal.TEN, 10,"Technical Requirements", "Course Objectives",
            LocalDate.now(), LocalDate.now(), BigDecimal.TEN, BigDecimal.TEN, BigDecimal.TEN, BigDecimal.TEN, BigDecimal.TEN,
            BigDecimal.TEN, "Training Description", "ReTest Description", "Marking Description",
            "Waiver CriteriaDes", "Other Description", true, true, user, new User(),
            null, null);
    Syllabus syllabus2 = new Syllabus(2L, "Syllabus number 2", "SLB2", "1.0", "All level",
            1, BigDecimal.TEN, 10,"Technical Requirements", "Course Objectives",
            LocalDate.now(), LocalDate.now(), BigDecimal.TEN, BigDecimal.TEN, BigDecimal.TEN, BigDecimal.TEN, BigDecimal.TEN,
            BigDecimal.TEN, "Training Description", "ReTest Description", "Marking Description",
            "Waiver CriteriaDes", "Other Description", true, true, user, new User(),
            null, null);
    Syllabus syllabus3 = new Syllabus(3L, "Syllabus number 3", "SLB3", "1.0", "All level",
            1, BigDecimal.TEN, 10,"Technical Requirements", "Course Objectives",
            LocalDate.now(), LocalDate.now(), BigDecimal.TEN, BigDecimal.TEN, BigDecimal.TEN, BigDecimal.TEN, BigDecimal.TEN,
            BigDecimal.TEN, "Training Description", "ReTest Description", "Marking Description",
            "Waiver CriteriaDes", "Other Description", true, true, user, new User(),
            null, null);

    Session  session1 = new Session(1L, 1, true, syllabus1, null);
    Session  session2 = new Session(4L, 1, true, syllabus2, null);
    Session  session3 = new Session(7L, 1, true, syllabus3, null);

    Unit uni1 = new Unit(1L, "Unit title 1", 1, BigDecimal.valueOf(3.5), true, session1, null, null);
    Unit uni2 = new Unit(1L, "Unit title 2", 1, BigDecimal.valueOf(3.5), true, session2, null, null);
    Unit uni3 = new Unit(1L, "Unit title 3", 1, BigDecimal.valueOf(3.5), true, session3, null, null);

    OutputStandard osd1 = new OutputStandard(1L, "ABC", "Ăn bát cơm", "No", true,  null);

    UnitDetail detail1 = new UnitDetail(1L, "Detail 1", BigDecimal.valueOf(30), true, true, uni1, null, osd1, null);
    UnitDetail detail2 = new UnitDetail(1L, "Detail 2", BigDecimal.valueOf(30), true, true, uni2, null, osd1, null);
    UnitDetail detail3 = new UnitDetail(1L, "Detail 3", BigDecimal.valueOf(30), true, true, uni3, null, osd1, null);

    @Test
    void getAllSyllabus() {
        List<Syllabus> syllabusList = new ArrayList<>();
        syllabusList.add(syllabus1);
        syllabusList.add(syllabus2);
        syllabusList.add(syllabus3);

        List<UnitDetail> detailList = new ArrayList<>();
        detailList.add(detail1);
        detailList.add(detail2);
        detailList.add(detail3);

        List<OutputStandard> outputStandardList = new ArrayList<>();
        outputStandardList.add(osd1);

        List<Long> listId = new ArrayList<>();
        listId.add(1L);
        listId.add(2L);
        listId.add(3L);

        List<String> search = new ArrayList<>();
        search.add("");

        when(syllabusRepository.getListSyllabus(true, null, null, "", listId, Sort.by(Sort.Direction.ASC, "name"))).thenReturn(syllabusList);

        when(outputStandardRepo.findByStatusAndStandardCodeContainingIgnoreCase(true, "")).thenReturn(outputStandardList);

        when(unitDetailRepo.findByStatusAndOutputStandardIn(true, outputStandardList)).thenReturn(detailList);

        Page<SyllabusDTO> result = syllabusService.getListSyllabus(true, null, null, search, new String[] {"name","asc"}, Optional.of(0), Optional.of(10));

        assertEquals(3, result.getContent().size());
        search.add("Syllabus number 2");
        result = syllabusService.getListSyllabus(true, null, null, search, new String[] {"name","asc"}, Optional.of(0), Optional.of(10));
        assertEquals(1, result.getContent().size());
        assertEquals("SLB2", result.getContent().get(0).getCode());
        verify(syllabusRepository, times(2)).getListSyllabus(true, null, null, "", listId, Sort.by(Sort.Direction.ASC, "name"));
    }

    @Test
    void deleteSyllabusTest(){
        Syllabus afterSave = new Syllabus(1L, "Syllabus number 1", "SLB1", "1.0", "All level",
                1, BigDecimal.TEN, 10,"Technical Requirements", "Course Objectives",
                LocalDate.now(), LocalDate.now(), BigDecimal.TEN, BigDecimal.TEN, BigDecimal.TEN, BigDecimal.TEN, BigDecimal.TEN,
                BigDecimal.TEN, "Training Description", "ReTest Description", "Marking Description",
                "Waiver CriteriaDes", "Other Description", true, false, user, new User(),
                null, null);

        when(syllabusRepository.save(syllabus1)).thenReturn(afterSave);
        when(syllabusRepository.findByIdAndStatus(syllabus1.getId(), true)).thenReturn(Optional.ofNullable(syllabus1));

        boolean result = syllabusService.deleteSyllabus(syllabus1.getId(), true);

        assertEquals(true, result);

        verify(syllabusRepository, times(1)).save(syllabus1);
    }
}
