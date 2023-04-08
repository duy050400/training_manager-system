package com.mockproject.service.interfaces;

import com.mockproject.dto.SessionDTO;
import com.mockproject.entity.Session;
import com.mockproject.entity.User;

import java.io.IOException;
import java.util.List;

public interface ISessionService {
    List<SessionDTO> getSessionListBySyllabusId(Long idSyllabus);

    List<SessionDTO> listBySyllabus(Long sid);

    List<SessionDTO> getAllSessionBySyllabusId(Long syllabusId, boolean status);

    boolean createSession(Long syllabusId, List<SessionDTO> listSession, User user);

    boolean createSession(Long syllabusId, SessionDTO sessionDTO, User user);

    Session editSession(SessionDTO sessionDTO, boolean status) throws IOException;

    boolean deleteSession(Long sessionId, boolean status);

    boolean deleteSessions(Long syllabusId, boolean status);

}
