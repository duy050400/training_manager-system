package com.mockproject.service.interfaces;

import com.mockproject.dto.UnitDTO;
import com.mockproject.dto.UnitDetailDTO;
import com.mockproject.entity.UnitDetail;
import com.mockproject.entity.User;

import java.io.IOException;
import java.util.List;

public interface IUnitDetailService {

    List<UnitDetailDTO> getListUnitDetail(List<UnitDTO> listUnit);

    List<UnitDetailDTO> getUnitDetailByUnitId(Long idUnit);

    List<UnitDetailDTO> getAllUnitDetailByUnitId(Long unitId, boolean status);

    boolean createUnitDetail(Long unitId, List<UnitDetailDTO> listUnitDetail, User user);

    boolean createUnitDetail(Long unitId, UnitDetailDTO unitDetailDTO, User user);

    UnitDetail getUnitDetailById(Long id, boolean status);

    UnitDetail editUnitDetail(UnitDetailDTO unitDetailDTO, boolean status) throws IOException;

    boolean deleteUnitDetail(Long unitDetailId, boolean status);

    boolean deleteUnitDetails(Long unitId, boolean status);

    boolean toggleUnitDetailType(Long unitDetailId, boolean status);

    UnitDetail get(long id);

    List<UnitDetailDTO> listByUnitIdTrue(Long id);
}
