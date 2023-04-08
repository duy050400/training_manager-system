package com.mockproject.service.interfaces;

import com.mockproject.dto.TrainingMaterialDTO;
import com.mockproject.dto.UnitDetailDTO;
import com.mockproject.entity.UnitDetail;
import com.mockproject.entity.User;

import java.io.IOException;
import java.util.List;
import java.util.zip.DataFormatException;

public interface ITrainingMaterialService {

    List<TrainingMaterialDTO> getListTrainingMaterial(List<UnitDetailDTO> listUnitDetail);

    TrainingMaterialDTO uploadAFile(TrainingMaterialDTO createDTO, UnitDetail unitDetail, User user) throws IOException;

    TrainingMaterialDTO getFile(Long id, boolean status) throws DataFormatException, IOException;

    List<TrainingMaterialDTO> uploadFile(List<TrainingMaterialDTO> createTrainingMaterialDTOList, User user, Long unitDetailID);

    TrainingMaterialDTO updateFile(Long id, TrainingMaterialDTO createDTO, User user, boolean status) throws IOException;

    List<TrainingMaterialDTO> getFiles(Long unitDetailId, boolean status);

    boolean deleteTrainingMaterial(Long trainingMaterialId, boolean status);

    boolean deleteTrainingMaterials(Long unitDetailId, boolean status);

    List<TrainingMaterialDTO> getListTrainingMaterialByUnitDetailId(Long id);
}
