package com.mockproject.service.interfaces;

import com.mockproject.dto.SessionDTO;
import com.mockproject.dto.UnitDTO;
import com.mockproject.entity.Unit;
import com.mockproject.entity.User;

import java.io.IOException;
import java.util.List;

public interface IUnitService {
    List<UnitDTO> getListUnit(List<SessionDTO> session);


    List<Unit> getListUnitsByTrainingClassId(Long id);

    List<UnitDTO> getAllUnitsForADateByTrainingClassId(Long id, int dayNth);

    List<Unit> getListUnitsInASessionByTrainingClassId(Long id, int dayNth);

    List<UnitDTO> listBySessionId(Long sid);

    List<UnitDTO> getUnitBySessionId(Long idSession);

    List<UnitDTO> getAllUnitBySessionId(Long sessionId, boolean status);

    boolean createUnit(Long sessionId, List<UnitDTO> listUnit, User user);

    boolean createUnit(Long sessionId, UnitDTO unitDTO, User user);

    Unit editUnit(UnitDTO unitDTO, boolean status) throws IOException;

    boolean deleteUnit(Long unitId, boolean status);

    boolean deleteUnits(Long sessionId, boolean status);

}
