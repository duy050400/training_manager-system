package com.mockproject.service;

import com.mockproject.dto.SessionDTO;
import com.mockproject.dto.UnitDTO;
import com.mockproject.dto.UnitDetailDTO;
import com.mockproject.entity.*;
import com.mockproject.mapper.UnitDetailMapper;
import com.mockproject.mapper.UnitMapper;
import com.mockproject.repository.*;
import com.mockproject.service.interfaces.IUnitDetailService;
import com.mockproject.service.interfaces.IUnitService;
import com.mockproject.utils.ListUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class UnitService implements IUnitService {
    private final UnitRepository unitRepository;
    private final IUnitDetailService unitDetailService;
    private final SessionRepository sessionRepository;
    private final SyllabusRepository syllabusRepository;
    private final TrainingClassRepository trainingClassRepository;
    private final TrainingClassUnitInformationRepository trainingClassUnitInformationRepository;

    @Override
    public List<UnitDTO> getAllUnitBySessionId(Long sessionId, boolean status){
        Optional<List<Unit>> listUnit = unitRepository.findUnitBySessionIdAndStatus(sessionId, status);
        List<UnitDTO> unitDTOList = new ArrayList<>();

        for (Unit u : listUnit.get()) {
            unitDTOList.add(UnitMapper.INSTANCE.toDTO(u));
        }

        for (UnitDTO u: unitDTOList){
            List<UnitDetailDTO> unitDetailDTOList = unitDetailService.getAllUnitDetailByUnitId(u.getId(), true);
            u.setUnitDetailDTOList(unitDetailDTOList);
        }
        return unitDTOList;
    }

    @Override
    public boolean createUnit(Long sessionId, List<UnitDTO> listUnit, User user){
        Optional<Session> session = sessionRepository.findByIdAndStatus(sessionId, true);
        session.orElseThrow(() -> new ResponseStatusException(HttpStatus.NO_CONTENT,"Session not found"));

        for (UnitDTO i: listUnit) {
            createUnit(sessionId, i, user);
        }

        return true;
    }

    @Override
    public boolean createUnit(Long sessionId, UnitDTO unitDTO, User user){
        Optional<Session> session = sessionRepository.findByIdAndStatus(sessionId, true);
        session.orElseThrow(() -> new ResponseStatusException(HttpStatus.NO_CONTENT, "Session not found"));
        Optional<Syllabus> syllabus = syllabusRepository.findByIdAndStateAndStatus(session.get().getSyllabus().getId(),true,true);
        syllabus.orElseThrow(() -> new ResponseStatusException(HttpStatus.NO_CONTENT, "Syllabus not found"));
        BigDecimal duration = syllabus.get().getHour();

        unitDTO.setSessionId(sessionId);
        unitDTO.setStatus(true);
        Unit unit = UnitMapper.INSTANCE.toEntity(unitDTO);
        unit.setDuration(BigDecimal.valueOf(0));
        unitRepository.save(unit);
        unitDetailService.createUnitDetail(unit.getId(), unitDTO.getUnitDetailDTOList(), user);
        unit = unitRepository.findByIdAndStatus(unit.getId(), true).get();
        duration = duration.add(unit.getDuration());

        // Set duration syllabus
        syllabus.get().setHour(duration);
        return true;
    }

    @Override
    public Unit editUnit(UnitDTO unitDTO, boolean status) throws IOException {
        Optional<Unit> unit = unitRepository.findByIdAndStatus(unitDTO.getId(), status);
        unit.orElseThrow(() -> new ResponseStatusException(HttpStatus.NO_CONTENT, "Unit not found"));

        CustomUserDetails user = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if(unitDTO.isStatus()){
            unit.get().setDuration(BigDecimal.valueOf(0));
            unitRepository.save(unit.get());
            for (UnitDetailDTO u: unitDTO.getUnitDetailDTOList()){
                if (u.getId() == null){
                    unitDetailService.createUnitDetail(unitDTO.getId(),u,user.getUser());
                }else {
                    unitDetailService.editUnitDetail(u, true);
                }
            }
        }else{
            deleteUnit(unitDTO.getId(), true);
        }

        unit = unitRepository.findByIdAndStatus(unitDTO.getId(), status);
        unit.orElseThrow(() -> new ResponseStatusException(HttpStatus.NO_CONTENT,"Unit not found"));

        unitDTO.setSessionId(unit.get().getSession().getId());
        unitDTO.setDuration(unit.get().getDuration());
        Unit updateUnit = unitRepository.save(UnitMapper.INSTANCE.toEntity(unitDTO));

        return updateUnit;
    }

    @Override
    public boolean deleteUnit(Long unitId, boolean status){
        Optional<Unit> unit = unitRepository.findByIdAndStatus(unitId, status);
        unit.orElseThrow(() -> new  ResponseStatusException(HttpStatus.NO_CONTENT, "Unit not found"));
        List<UnitDetailDTO> unitDetailDTOList = unitDetailService.getAllUnitDetailByUnitId(unitId, true);
        List<UnitDetail>   unitDetailList = new ArrayList<>();

        for(UnitDetailDTO unitDetailDTO : unitDetailDTOList){
            unitDetailList.add(UnitDetailMapper.INSTANCE.toEntity(unitDetailDTO));
        }
        unit.get().setListUnitDetail(unitDetailList);
        unit.get().setStatus(false);
        if(!unit.get().getListUnitDetail().isEmpty())
            unitDetailService.deleteUnitDetails(unitId, status);
        unitRepository.save(unit.get());
        return true;
    }

    @Override
    public boolean deleteUnits(Long sessionId, boolean status){
        Optional<List<Unit>> units = unitRepository.findAllBySessionIdAndStatus(sessionId, status);
        ListUtils.checkList(units);
        units.get().forEach((i) -> deleteUnit(i.getId(), status));
        return true;
    }

    @Override
    public List<UnitDTO> getUnitBySessionId(Long idSession){
        return unitRepository.getListUnitBySessionId(idSession).stream().map(UnitMapper.INSTANCE::toDTO).collect(Collectors.toList());
    }

    @Override
    public List<UnitDTO> listBySessionId(Long sid) {
        Session session = new Session();
        session.setId(sid);
        return unitRepository.findBySession(session).stream().map(UnitMapper.INSTANCE::toDTO).toList();
    }

    @Override
    public List<UnitDTO> getAllUnitsForADateByTrainingClassId(Long id, int dayNth) {
        List<Unit> units = getListUnitsInASessionByTrainingClassId(id, dayNth);
        return units.stream().map(UnitMapper.INSTANCE::toDTO).toList();
    }

    @Override
    // get all units from a class
    public List<Unit> getListUnitsByTrainingClassId(Long id){
        // Get Class
        TrainingClass tc = trainingClassRepository.findByIdAndStatus(id, true).orElseThrow();

        // Get all units
        List<TrainingClassUnitInformation> list = trainingClassUnitInformationRepository.findByTrainingClassAndStatus(tc, true).orElseThrow();
        return list.stream().map(p-> unitRepository.findByIdAndStatus(p.getUnit().getId(), true).orElseThrow()).toList();
    }

    // Get a session from a date
    private Session getSession(Long id, int dayNth) {
        // Get all sessions
        List<Session> sessions = getListUnitsByTrainingClassId(id).stream().map(p -> sessionRepository.findByIdAndStatus(p.getSession().getId(), true).orElseThrow()).toList();

        List<Long> sessionIds = sessions.stream().map(Session::getId).distinct().toList();
        return sessionRepository.findByIdAndStatus(sessionIds.get(dayNth - 1), true).orElseThrow();

//        Map<Integer, Long> list = new HashMap<>();
//        list.put(1, sessions.get(0).getId());
//
//
//        for (int i = 1; i < sessions.size(); i++) {
//            if (sessions.get(i).getId() == sessions.get(i - 1).getId()) {
//                list.put(i + 1, null);
//            } else list.put(i + 1, sessions.get(i).getId());
//        }
//
//        return sessionRepository.findByIdAndStatus(list.get(dayNth), true).orElseThrow();
    }

    @Override
    // Get list units from a session
    public List<Unit> getListUnitsInASessionByTrainingClassId(Long id, int dayNth){
        Session session = getSession(id, dayNth);
        return unitRepository.findBySessionAndStatusOrderByUnitNumber(session, true).orElseThrow();
    }

    @Override
    public List<UnitDTO> getListUnit(List<SessionDTO> session){
        List<UnitDTO> listUnit = new ArrayList<>();
        for(SessionDTO s : session){
            listUnit.addAll(getUnitBySessionId(s.getId()));
        }
        return listUnit;
    }
}
