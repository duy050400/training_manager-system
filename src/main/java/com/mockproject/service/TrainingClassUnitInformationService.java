package com.mockproject.service;

import com.mockproject.dto.TrainingClassUnitInformationDTO;
import com.mockproject.entity.TrainingClassUnitInformation;
import com.mockproject.mapper.TrainingClassUnitInformationMapper;
import com.mockproject.repository.TrainingClassUnitInformationRepository;
import com.mockproject.service.interfaces.ITrainingClassUnitInformationService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class TrainingClassUnitInformationService implements ITrainingClassUnitInformationService {

    private final TrainingClassUnitInformationRepository repository;

    @Override
    public List<TrainingClassUnitInformation> findAllByTrainingClassId(Long id) {
        return repository.findAllByTrainingClassId(id);
    }

    @Override
    public boolean saveList(List<TrainingClassUnitInformationDTO> listDto) {

        //        List<TrainingClassUnitInformation> resultList = repository.saveAll(listDto.stream().map(TrainingClassUnitInformationMapper.INSTANCE::toEntity).toList());

        List<TrainingClassUnitInformation> listEntity = listDto.stream().map(TrainingClassUnitInformationMapper.INSTANCE::toEntity).toList();
        List<TrainingClassUnitInformation> resultList = repository.saveAll(listEntity);
        return !resultList.isEmpty();
    }
}
