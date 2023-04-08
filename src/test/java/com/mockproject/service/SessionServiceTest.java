package com.mockproject.service;

import com.mockproject.dto.SessionDTO;
import com.mockproject.entity.Session;
import com.mockproject.entity.Syllabus;
import com.mockproject.entity.User;
import com.mockproject.mapper.SessionMapper;
import com.mockproject.repository.SessionRepository;
import com.mockproject.repository.SyllabusRepository;
import com.mockproject.repository.UnitRepository;
import com.mockproject.service.interfaces.IUnitService;
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

@ContextConfiguration(classes = {SessionService.class})
@ExtendWith(SpringExtension.class)
public class SessionServiceTest {

    @MockBean
    private SessionRepository sessionRepository;
    @MockBean
    private SyllabusRepository syllabusRepository;
    @MockBean
    private UnitRepository unitRepository;
    @MockBean
    private IUnitService unitService;


    @Autowired
    private SessionService sessionService;

    User user = new User(1L, "abc@gmail.com", "***", "Pham Quoc Thinh", "avatar", 1, LocalDate.now(), "0938081927", true, true,
            null, null, null, null, null, null, null, null, null, null, null, null, null, null);

    Syllabus syllabus = new Syllabus(1L, "ABC", "SLB1", "1.0", "All level",
            1, BigDecimal.TEN, 10,"Technical Requirements", "Course Objectives",
            LocalDate.now(), LocalDate.now(), BigDecimal.TEN, BigDecimal.TEN, BigDecimal.TEN, BigDecimal.TEN, BigDecimal.TEN,
            BigDecimal.TEN, "Training Description", "ReTest Description", "Marking Description",
            "Waiver CriteriaDes", "Other Description", true, true, user, new User(),
            null, null);

    Session session1 = new Session(1L,1,true,syllabus, null);
    Session session2 = new Session(2L,1,true,syllabus, null);
    Session session3 = new Session(3L,1,true,syllabus, null);

    @Test
    void getAllSessionBySyllabusIdTest(){
        List<Session> list = new ArrayList<>();
        list.add(session1);
        list.add(session2);
        list.add(session3);

        when(sessionRepository.findBySyllabusIdAndStatus(syllabus.getId(), true)).thenReturn(Optional.of(list));

        List<SessionDTO> result = sessionService.getAllSessionBySyllabusId(syllabus.getId(), true);

        assertEquals(3, result.size());

        verify(sessionRepository,times(1)).findBySyllabusIdAndStatus(syllabus.getId(), true);
    }

    @Test
    void createSessionTest(){
        List<SessionDTO> list = new ArrayList<>();
        list.add(SessionMapper.INSTANCE.toDTO(session1));
        list.add(SessionMapper.INSTANCE.toDTO(session2));
        list.add(SessionMapper.INSTANCE.toDTO(session3));

        when(syllabusRepository.findByIdAndStateAndStatus(syllabus.getId(),true,true)).thenReturn(Optional.ofNullable(syllabus));
        when(syllabusRepository.save(syllabus)).thenReturn(syllabus);
        when(sessionRepository.save(any())).thenReturn(session1);

        Boolean result = sessionService.createSession(syllabus.getId(), list, user);

        assertEquals(true, result);

        verify(sessionRepository,times(3)).save(any());
    }

    @Test
    void deleteTest(){
        List<Session> list = new ArrayList<>();
        list.add(session1);
        list.add(session2);
        list.add(session3);

        when(sessionRepository.findBySyllabusIdAndStatus(syllabus.getId(), true)).thenReturn(Optional.of(list));
        when(sessionRepository.findByIdAndStatus(session1.getId(), true)).thenReturn(Optional.ofNullable(session1));
        when(sessionRepository.findByIdAndStatus(session2.getId(), true)).thenReturn(Optional.ofNullable(session2));
        when(sessionRepository.findByIdAndStatus(session3.getId(), true)).thenReturn(Optional.ofNullable(session3));
        when(sessionRepository.save(any())).thenReturn(session1);

        boolean result = sessionService.deleteSessions(syllabus.getId(), true);

        assertEquals(true, result);

        verify(sessionRepository,times(3)).findByIdAndStatus(any(), eq(true));
    }
}
