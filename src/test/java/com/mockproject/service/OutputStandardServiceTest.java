package com.mockproject.service;

import com.mockproject.dto.OutputStandardDTO;
import com.mockproject.entity.*;
import com.mockproject.repository.OutputStandardRepository;
import com.mockproject.repository.UnitDetailRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.webjars.NotFoundException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ContextConfiguration(classes = {OutputStandardService.class})
@ExtendWith(SpringExtension.class)
public class OutputStandardServiceTest {

    @MockBean
    private OutputStandardRepository outputStandardRepository;

    @MockBean
    private UnitDetailRepository unitDetailRepo;

    @Autowired
    private OutputStandardService outputStandardService;

    User user = new User(1L, "abc@gmail.com", "***", "Pham Quoc Thinh", "avatar", 1, LocalDate.now(), "0938081927", true, true,
            null, null, null, null, null, null, null, null, null, null, null, null, null, null);

    OutputStandard osd1 = new OutputStandard(1L, "PQT", "Pham Quoc Thinh", "No", true,  null);
    OutputStandard osd2 = new OutputStandard(2L, "ABC", "Ăn bát cơm", "No", true,  null);
    OutputStandard osd3 = new OutputStandard(3L, "HBC", "Húp bát canh", "No", true,  null);
    OutputStandard osd4 = new OutputStandard(4L, "ATC", "Ăn trái chuối", "No", true,  null);

    Syllabus syllabus = new Syllabus(1L, "Syllabus number 1", "SLB1", "1.0", "All level",
            1, BigDecimal.TEN, 10,"Technical Requirements", "Course Objectives",
            LocalDate.now(), LocalDate.now(), BigDecimal.TEN, BigDecimal.TEN, BigDecimal.TEN, BigDecimal.TEN, BigDecimal.TEN,
            BigDecimal.TEN, "Training Description", "ReTest Description", "Marking Description",
            "Waiver CriteriaDes", "Other Description", true, true, user, new User(),
            null, null);

    Session session = new Session(1L, 1, true, syllabus, null);

    Unit unit = new Unit(1L, "Unit title 1", 1, BigDecimal.valueOf(3.5), true, session, null, null);

    UnitDetail detail1 = new UnitDetail(1L, "Detail 1", BigDecimal.valueOf(30), true, true, unit, null, osd1, null);
    UnitDetail detail2 = new UnitDetail(2L, "Detail 2", BigDecimal.valueOf(30), true, true, unit, null, osd2, null);
    UnitDetail detail3 = new UnitDetail(3L, "Detail 3", BigDecimal.valueOf(30), true, true, unit, null, osd3, null);
    UnitDetail detail4 = new UnitDetail(4L, "Detail 4", BigDecimal.valueOf(30), true, true, unit, null, osd3, null);


    @Test
    void getOsdBySyllabusId(){
        List<OutputStandard> outputStandardList = new ArrayList<>();
        outputStandardList.add(osd1);
        outputStandardList.add(osd2);
        outputStandardList.add(osd3);
        outputStandardList.add(osd4);

        List<UnitDetail> detailList = new ArrayList<>();
        detailList.add(detail1);
        detailList.add(detail2);
        detailList.add(detail3);
        detailList.add(detail4);

        when(unitDetailRepo.findUnitDetailBySyllabusId(true, 1L)).thenReturn(detailList.stream().map(u -> u.getOutputStandard()).collect(Collectors.toList()));

        List<OutputStandardDTO> result = outputStandardService.getOsdBySyllabusId(true, 1L);
        assertEquals(3, result.size());

        NotFoundException thrown = assertThrows(NotFoundException.class, () -> outputStandardService.getOsdBySyllabusId(true, 2L), "No Exception");
        assertTrue(thrown.getMessage().equals("Output standard not found with syllabus id: 2"));

        verify(unitDetailRepo, times(1)).findUnitDetailBySyllabusId(true, 1L);
    }
}
