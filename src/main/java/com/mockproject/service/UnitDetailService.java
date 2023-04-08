package com.mockproject.service;

import com.mockproject.dto.TrainingMaterialDTO;
import com.mockproject.dto.UnitDTO;
import com.mockproject.dto.UnitDetailDTO;
import com.mockproject.entity.*;
import com.mockproject.mapper.TrainingMaterialMapper;
import com.mockproject.mapper.UnitDetailMapper;
import com.mockproject.repository.SyllabusRepository;
import com.mockproject.repository.UnitDetailRepository;
import com.mockproject.repository.UnitRepository;
import com.mockproject.service.interfaces.ITrainingMaterialService;
import com.mockproject.service.interfaces.IUnitDetailService;
import com.mockproject.utils.ListUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class UnitDetailService implements IUnitDetailService {

    private final UnitRepository unitRepository;

    private final UnitDetailRepository unitDetailRepository;

    private final ITrainingMaterialService trainingMaterialService;

    private final SyllabusRepository syllabusRepository;

    @Override
    public List<UnitDetailDTO> getUnitDetailByUnitId(Long idUnit) {
        return unitDetailRepository.getListUnitDetailByUnitId(idUnit).stream().map(UnitDetailMapper.INSTANCE::toDTO).collect(Collectors.toList());
    }

    @Override
    public List<UnitDetailDTO> getAllUnitDetailByUnitId(Long unitId, boolean status) {
        Optional<List<UnitDetail>> unitDetails = unitDetailRepository.findByUnitIdAndStatus(unitId, status);
        List<UnitDetailDTO> unitDetailDTOList = new ArrayList<>();

        for (UnitDetail u: unitDetails.get()){
            unitDetailDTOList.add(UnitDetailMapper.INSTANCE.toDTO(u));
        }

        for (UnitDetailDTO u: unitDetailDTOList){
            List<TrainingMaterialDTO> trainingMaterialDTOList = trainingMaterialService.getFiles(u.getId(), true);
            u.setTrainingMaterialDTOList(trainingMaterialDTOList);
        }
        return unitDetailDTOList;
    }

    @Override
    public boolean createUnitDetail(Long unitId, List<UnitDetailDTO> listUnitDetail, User user){
        Optional<Unit> unit = unitRepository.findByIdAndStatus(unitId, true);
        unit.orElseThrow(() -> new ResponseStatusException(HttpStatus.NO_CONTENT, "Unit not found"));
        for (UnitDetailDTO i: listUnitDetail) {
            createUnitDetail(unitId, i, user);
        }
        return true;
    }

    @Override
    public boolean createUnitDetail(Long unitId, UnitDetailDTO unitDetailDTO, User user){
        Optional<Unit> unit = unitRepository.findByIdAndStatus(unitId, true);
        unit.orElseThrow(() -> new ResponseStatusException(HttpStatus.NO_CONTENT, "Unit not found"));
        BigDecimal duration = unit.get().getDuration();

        unitDetailDTO.setStatus(true);
        unitDetailDTO.setUnitId(unitId);
        duration = duration.add(unitDetailDTO.getDuration().divide(BigDecimal.valueOf(60), 2, RoundingMode.HALF_UP));
        UnitDetail unitDetail = unitDetailRepository.save(UnitDetailMapper.INSTANCE.toEntity(unitDetailDTO));
        trainingMaterialService.uploadFile(unitDetailDTO.getTrainingMaterialDTOList(), user, unitDetail.getId());

        //Set duration unit
        unit.get().setDuration(duration);
        unitRepository.save(unit.get());
        return true;
    }

    @Override
    public UnitDetail getUnitDetailById(Long id, boolean status){
        Optional<UnitDetail> unitDetail = unitDetailRepository.findByIdAndStatus(id, status);
        unitDetail.orElseThrow(() -> new ResponseStatusException(HttpStatus.NO_CONTENT, "UnitDetail not found"));
        return unitDetail.get();
    }

    @Override
    public UnitDetail editUnitDetail(UnitDetailDTO unitDetailDTO, boolean status) throws IOException {
        Optional<UnitDetail> unitDetail = unitDetailRepository.findByIdAndStatus(unitDetailDTO.getId(), status);
        unitDetail.orElseThrow(() -> new ResponseStatusException(HttpStatus.NO_CONTENT, "UnitDetail not found"));
        unitDetailDTO.setUnitId(unitDetail.get().getUnit().getId());

        CustomUserDetails user = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if(unitDetailDTO.isStatus()){
            for (TrainingMaterialDTO t: unitDetailDTO.getTrainingMaterialDTOList()){
                if(t.getId() == null)
                    trainingMaterialService.uploadAFile(t, unitDetail.get(), user.getUser());
                else {
                    trainingMaterialService.updateFile(t.getId(),t, user.getUser(),true);
                }
            }
        }else {
            deleteUnitDetail(unitDetailDTO.getId(),true);
        }

        unitDetail.get().getUnit().setDuration(unitDetail.get().getUnit().getDuration().subtract(unitDetail.get().getDuration().divide(BigDecimal.valueOf(60), 2, RoundingMode.HALF_UP)));

        unitDetail.get().getUnit().getSession().getSyllabus().setHour(unitDetail.get().getUnit().getSession().getSyllabus().getHour().subtract(unitDetail.get().getDuration().divide(BigDecimal.valueOf(60), 2, RoundingMode.HALF_UP)));

        unitRepository.save(unitDetail.get().getUnit());
        syllabusRepository.save(unitDetail.get().getUnit().getSession().getSyllabus());

        UnitDetail updateUnitDetail = unitDetailRepository.save(UnitDetailMapper.INSTANCE.toEntity(unitDetailDTO));

        return updateUnitDetail;
    }

    @Override
    public boolean deleteUnitDetail(Long unitDetailId, boolean status){
        Optional<UnitDetail> unitDetail = unitDetailRepository.findByIdAndStatus(unitDetailId, status);
        unitDetail.orElseThrow(() -> new ResponseStatusException(HttpStatus.NO_CONTENT, "UnitDetail not found"));
        List<TrainingMaterialDTO> trainingMaterialDTOList = trainingMaterialService.getFiles(unitDetailId, true);
        List<TrainingMaterial>   trainingMaterialList = new ArrayList<>();

        for(TrainingMaterialDTO trainingMaterialDTO : trainingMaterialDTOList){
            trainingMaterialList.add(TrainingMaterialMapper.INSTANCE.toEntity(trainingMaterialDTO));
        }
        unitDetail.get().setListMaterials(trainingMaterialList);
        unitDetail.get().setStatus(false);
        if(!unitDetail.get().getListMaterials().isEmpty())
            trainingMaterialService.deleteTrainingMaterials(unitDetailId,status);

        unitDetail.get().getUnit().setDuration(unitDetail.get().getUnit().getDuration().subtract(unitDetail.get().getDuration().divide(BigDecimal.valueOf(60), 2, RoundingMode.HALF_UP)));

        unitDetail.get().getUnit().getSession().getSyllabus().setHour(unitDetail.get().getUnit().getSession().getSyllabus().getHour().subtract(unitDetail.get().getDuration().divide(BigDecimal.valueOf(60), 2, RoundingMode.HALF_UP)));

        unitRepository.save(unitDetail.get().getUnit());
        syllabusRepository.save(unitDetail.get().getUnit().getSession().getSyllabus());

        unitDetailRepository.save(unitDetail.get());
        return true;
    }

    @Override
    public boolean deleteUnitDetails(Long unitId, boolean status){
        Optional<List<UnitDetail>> unitDetails = unitDetailRepository.findByUnitIdAndStatus(unitId, status);
        ListUtils.checkList(unitDetails);
        unitDetails.get().forEach((i) -> deleteUnitDetail(i.getId(), status));
        return true;
    }

    @Override
    public boolean toggleUnitDetailType(Long unitDetailId, boolean status){
        Optional<UnitDetail> unitDetail = unitDetailRepository.findByIdAndStatus(unitDetailId, status);
        unitDetail.orElseThrow(() -> new ResponseStatusException(HttpStatus.NO_CONTENT, "UnitDetail not found"));
        unitDetail.get().setType(unitDetail.get().isType() == true ? false: true);
        unitDetailRepository.save(unitDetail.get());
        return true;
    }

    @Override
    public UnitDetail get(long id) {
        return null;
    }

    @Override
    public List<UnitDetailDTO> listByUnitIdTrue(Long id) {
        Unit unit = new Unit();
        unit.setId(id);
        return unitDetailRepository.findByUnitAndStatus(unit,true).stream().map(UnitDetailMapper.INSTANCE::toDTO).toList();
    }

    public List<UnitDetailDTO> getUnitDetailByUnitId(long idUnit) {
        return unitDetailRepository.getListUnitDetailByUnitId(idUnit).stream().map(UnitDetailMapper.INSTANCE::toDTO).collect(Collectors.toList());
    }

    @Override
    public List<UnitDetailDTO> getListUnitDetail(List<UnitDTO> listUnit){
        List<UnitDetailDTO> listUnitDetail = new ArrayList<>();
        for(UnitDTO u: listUnit){
            listUnitDetail.addAll(getUnitDetailByUnitId(u.getId()));
        }
        return listUnitDetail;
    }
}
