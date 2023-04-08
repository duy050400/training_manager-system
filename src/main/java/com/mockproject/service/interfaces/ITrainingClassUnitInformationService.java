package com.mockproject.service.interfaces;

import com.mockproject.dto.TrainingClassUnitInformationDTO;
import com.mockproject.entity.TrainingClassUnitInformation;

import java.util.List;

public interface ITrainingClassUnitInformationService {

    boolean saveList(List<TrainingClassUnitInformationDTO> listDto);

    List<TrainingClassUnitInformation> findAllByTrainingClassId(Long id);
}
