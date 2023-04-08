package com.mockproject.service;

import com.mockproject.dto.SessionDTO;
import com.mockproject.dto.UnitDTO;
import com.mockproject.entity.*;
import com.mockproject.mapper.SessionMapper;
import com.mockproject.mapper.UnitMapper;
import com.mockproject.repository.SessionRepository;
import com.mockproject.repository.SyllabusRepository;
import com.mockproject.repository.UnitRepository;
import com.mockproject.service.interfaces.ISessionService;
import com.mockproject.service.interfaces.IUnitService;
import com.mockproject.utils.ListUtils;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class SessionService implements ISessionService {

    private final SessionRepository sessionRepository;
    private final IUnitService unitService;
    private final SyllabusRepository syllabusRepository;
    private final UnitRepository unitRepository;

    @Autowired
    public SessionService(SessionRepository sessionRepository, IUnitService unitService, SyllabusRepository syllabusRepository, UnitRepository unitRepository) {
        this.sessionRepository = sessionRepository;
        this.unitService = unitService;
        this.syllabusRepository = syllabusRepository;
        this.unitRepository = unitRepository;
    }

    @Override
    public List<SessionDTO> getSessionListBySyllabusId(Long idSyllabus) {
        return sessionRepository.getSessionListBySyllabusId(idSyllabus).stream().map(SessionMapper.INSTANCE::toDTO).collect(Collectors.toList());
    }

    @Override
    public List<SessionDTO> getAllSessionBySyllabusId(Long syllabusId, boolean status) {
        Optional<List<Session>> listSession = sessionRepository.findBySyllabusIdAndStatus(syllabusId, status);
        List<SessionDTO> sessionDTOList = new ArrayList<>();
        for (Session s: listSession.get()) {
            sessionDTOList.add(SessionMapper.INSTANCE.toDTO(s));
        }
        for (SessionDTO s: sessionDTOList){
            List<UnitDTO> unitDTOList = unitService.getAllUnitBySessionId(s.getId(), true);
            s.setUnitDTOList(unitDTOList);
        }
        return sessionDTOList;
    }

    @Override
    public boolean createSession(Long syllabusId, List<SessionDTO> listSession, User user){
        Optional<Syllabus> syllabus = syllabusRepository.findByIdAndStateAndStatus(syllabusId, true,true);
        syllabus.orElseThrow(() -> new ResponseStatusException(HttpStatus.NO_CONTENT, "Syllabus not found"));
        syllabus.get().setDay(listSession.size());
        syllabusRepository.save(syllabus.get());
        listSession.forEach((i) ->
        {
            createSession(syllabusId, i, user);
        });
        return true;
    }

    @Override
    public boolean createSession(Long syllabusId, SessionDTO sessionDTO, User user){
        Optional<Syllabus> syllabus = syllabusRepository.findByIdAndStateAndStatus(syllabusId, true,true);
        syllabus.orElseThrow(() -> new ResponseStatusException(HttpStatus.NO_CONTENT, "Syllabus not found"));

        sessionDTO.setStatus(true);
        sessionDTO.setSyllabusId(syllabusId);
        Session session = sessionRepository.save(SessionMapper.INSTANCE.toEntity(sessionDTO));
        unitService.createUnit(session.getId(), sessionDTO.getUnitDTOList(), user);

        return true;
    }

    @Override
    public Session editSession(SessionDTO sessionDTO, boolean status) throws IOException{
        Optional<Session> session = sessionRepository.findByIdAndStatus(sessionDTO.getId(), status);
        session.orElseThrow(() -> new ResponseStatusException(HttpStatus.NO_CONTENT, "Session not found"));
        sessionDTO.setSyllabusId(session.get().getSyllabus().getId());

        CustomUserDetails user = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (sessionDTO.isStatus()){
            for(UnitDTO u : sessionDTO.getUnitDTOList()){
                if(u.getId() == null){
                    unitService.createUnit(sessionDTO.getId(), u, user.getUser());
                } else {
                    unitService.editUnit(u, true);
                }
            }
        }else{
            deleteSession(sessionDTO.getId(), true);
        }

        System.out.println("dang test o day");
        System.out.println(sessionDTO.isStatus());
        Session updateSession = sessionRepository.save(SessionMapper.INSTANCE.toEntity(sessionDTO));

        return updateSession;
    }

    @Override
    public boolean deleteSession(Long sessionId, boolean status){
        Optional<Session> session = sessionRepository.findByIdAndStatus(sessionId, status);
        session.orElseThrow(() -> new ResponseStatusException(HttpStatus.NO_CONTENT, "Session not found"));
        List<UnitDTO> unitDTOList = unitService.getAllUnitBySessionId(sessionId, true);
        List<Unit>   unitList = new ArrayList<>();

        for(UnitDTO unitDTO : unitDTOList){
            unitList.add(UnitMapper.INSTANCE.toEntity(unitDTO));
        }
        session.get().setListUnit(unitList);
        session.get().setStatus(false);
        if(!session.get().getListUnit().isEmpty())
            unitService.deleteUnits(sessionId, status);
        sessionRepository.save(session.get());
        System.out.println("da vao delete session");
        return true;
    }

    @Override
    public boolean deleteSessions(Long syllabusId, boolean status){
        Optional<List<Session>> sessions = sessionRepository.findBySyllabusIdAndStatus(syllabusId, status);
        ListUtils.checkList(sessions);
        sessions.get().forEach((i) ->
                deleteSession(i.getId(), status));
        return true;
    }

    @Override
    public List<SessionDTO> listBySyllabus(Long sid) {
        Syllabus syllabus = new Syllabus();
        syllabus.setId(sid);
        return sessionRepository.findBySyllabus(syllabus).stream().map(SessionMapper.INSTANCE::toDTO).collect(Collectors.toList());
    }
}
