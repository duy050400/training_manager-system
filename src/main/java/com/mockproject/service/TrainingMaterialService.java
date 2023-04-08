package com.mockproject.service;

import com.mockproject.dto.TrainingMaterialDTO;
import com.mockproject.dto.UnitDetailDTO;
import com.mockproject.entity.TrainingMaterial;
import com.mockproject.entity.UnitDetail;
import com.mockproject.entity.User;
import com.mockproject.mapper.TrainingMaterialMapper;
import com.mockproject.repository.TrainingMaterialRepository;
import com.mockproject.service.interfaces.ITrainingMaterialService;
import com.mockproject.service.interfaces.IUnitDetailService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.zip.DataFormatException;

@Service
@Transactional
@RequiredArgsConstructor
public class TrainingMaterialService implements ITrainingMaterialService {

    private final TrainingMaterialRepository trainingMaterialRepository;

    private final IUnitDetailService unitDetailService;

    @Autowired
    public TrainingMaterialService(@Lazy  IUnitDetailService unitDetailService, TrainingMaterialRepository trainingMaterialRepository){
        this.unitDetailService = unitDetailService;
        this.trainingMaterialRepository = trainingMaterialRepository;
    }

    @Override
    public TrainingMaterialDTO uploadAFile(TrainingMaterialDTO createDTO,UnitDetail unitDetail, User user) throws IOException {
        TrainingMaterial trainingMaterial = trainingMaterialRepository.save(TrainingMaterial.builder()
                .uploadDate(LocalDate.now())
                .data(createDTO.getData())
                .name(createDTO.getName())
                .type(createDTO.getType())
                .size(createDTO.getSize())
                .status(true)
                .unitDetail(unitDetail)
                .user(user)
                .build());
        return TrainingMaterialMapper.INSTANCE.toDTO(trainingMaterial);
    }

    @Override
    public TrainingMaterialDTO getFile(Long id, boolean status) throws DataFormatException, IOException {
        Optional<TrainingMaterial> trainingMaterial = trainingMaterialRepository.findByIdAndStatus(id, status);
        trainingMaterial.orElseThrow(() -> new ResponseStatusException(HttpStatus.NO_CONTENT, "Training Material not found"));
        TrainingMaterialDTO trainingMaterialDTO = TrainingMaterialMapper.INSTANCE.toDTO(trainingMaterial.get());
        return trainingMaterialDTO;
    }

    @Override
    public List<TrainingMaterialDTO> uploadFile(List<TrainingMaterialDTO> createTrainingMaterialDTOList, User user, Long unitDetailID) {
        UnitDetail unitDetail = unitDetailService.getUnitDetailById(unitDetailID, true);
        List<TrainingMaterialDTO> trainingMaterialDTOS = new ArrayList<>();
        createTrainingMaterialDTOList.forEach(
                (dto) -> {
                    try {
                        trainingMaterialDTOS.add(uploadAFile(dto, unitDetail, user));
                    } catch (IOException e) {
                        throw new ResponseStatusException(HttpStatus.NO_CONTENT, "Add training material file failed");
                    }
                }
        );
        return trainingMaterialDTOS;
    }

    @Override
    public TrainingMaterialDTO updateFile(Long id, TrainingMaterialDTO createDTO, User user, boolean status) throws IOException {
        Optional<TrainingMaterial> trainingMaterial = trainingMaterialRepository.findByIdAndStatus(id, status);
        trainingMaterial.orElseThrow(() -> new ResponseStatusException(HttpStatus.NO_CONTENT, "Training Material not found"));
        return TrainingMaterialMapper.INSTANCE.toDTO(trainingMaterialRepository.save(TrainingMaterial.builder()
                .id(id)
                .data(createDTO.getData())
                .name(createDTO.getName())
                .type(createDTO.getType())
                .size(createDTO.getSize())
                .uploadDate(LocalDate.now())
                .unitDetail(unitDetailService.getUnitDetailById(trainingMaterial.get().getUnitDetail().getId(), true))
                .user(user)
                .status(createDTO.isStatus())
                .build()));
    }

    @Override
    public List<TrainingMaterialDTO> getFiles(Long unitDetailId, boolean status){
        List<TrainingMaterialDTO> trainingMaterialDTOS = new ArrayList<>();
        Optional<List<TrainingMaterial>> trainingMaterials = trainingMaterialRepository.findAllByUnitDetailIdAndStatus(unitDetailId, status);
        if(trainingMaterials.isEmpty()){
            return new ArrayList<>();
        }
        else {
            trainingMaterials.get().forEach(trainingMaterial -> {
                try {
                    TrainingMaterialDTO trainingMaterialDTO = TrainingMaterialMapper.INSTANCE.toDTO(trainingMaterial);
                    trainingMaterialDTOS.add(trainingMaterialDTO);
                } catch (Exception e) {
                    throw new ResponseStatusException(HttpStatus.NO_CONTENT,"Get failed");
                }
            });
            return trainingMaterialDTOS;
        }
    }

    @Override
    public boolean deleteTrainingMaterial(Long trainingMaterialId, boolean status){
        try {
            Optional<TrainingMaterial> trainingMaterial = trainingMaterialRepository.findByIdAndStatus(trainingMaterialId, status);
            trainingMaterial.orElseThrow(() -> new ResponseStatusException(HttpStatus.NO_CONTENT, "Training Material not found"));
            trainingMaterial.get().setStatus(false);
            trainingMaterialRepository.save(trainingMaterial.get());
        } catch (Exception e){
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public  List<TrainingMaterialDTO> getListTrainingMaterial(List<UnitDetailDTO> listUnitDetail){
        List<TrainingMaterialDTO> listTrainingMaterial = new ArrayList<>();
        for(UnitDetailDTO u : listUnitDetail){
            listTrainingMaterial.addAll(getListTrainingMaterialByUnitDetailId(u.getId()));
        }
        return listTrainingMaterial;
    }

    @Override
    public List<TrainingMaterialDTO> getListTrainingMaterialByUnitDetailId(Long id){
        return trainingMaterialRepository.getListTrainingMaterialByUnitDetailId(id).stream().map(TrainingMaterialMapper.INSTANCE::toDTO).collect(Collectors.toList());
    }

    @Override
    public boolean deleteTrainingMaterials(Long unitDetailId, boolean status){
        Optional<List<TrainingMaterial>> listTrainingMaterial = trainingMaterialRepository.findAllByUnitDetailIdAndStatus(unitDetailId, status);
        listTrainingMaterial.orElseThrow(() -> new ResponseStatusException(HttpStatus.NO_CONTENT));
        listTrainingMaterial.get().forEach((i) -> deleteTrainingMaterial(i.getId(), status));
        return true;
    }
}
