package com.mockproject.service;

import com.mockproject.dto.TowerDTO;
import com.mockproject.entity.*;
import com.mockproject.mapper.TowerMapper;
import com.mockproject.repository.TowerRepository;
import com.mockproject.repository.TrainingClassRepository;
import com.mockproject.repository.TrainingClassUnitInformationRepository;
import com.mockproject.service.interfaces.ITowerService;
import com.mockproject.service.interfaces.IUnitService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class TowerService implements ITowerService {

    private final TowerRepository repository;
    private final TrainingClassRepository trainingClassRepository;
    private final TrainingClassUnitInformationRepository trainingClassUnitInformationRepository;
    private final IUnitService unitService;


    @Override
    public List<TowerDTO> listByTowerIdTrue(Long id) {
        Location location = new Location();
        location.setId(id);
        return repository.findByLocationAndStatus(location,true).stream().map(TowerMapper.INSTANCE::toDTO).toList();
    }

    @Override
    public List<TowerDTO> getAllTowersByTrainingClassId(Long id) {
        TrainingClass tc = trainingClassRepository.findByIdAndStatus(id, true).orElseThrow();
        List<TrainingClassUnitInformation> list = trainingClassUnitInformationRepository.findByTrainingClassAndStatus(tc, true).orElseThrow();
        List<Tower> listTower = list.stream().map(p -> repository.findByIdAndStatus(p.getTower().getId(), true).orElseThrow()).distinct().toList();
        return listTower.stream().map(TowerMapper.INSTANCE::toDTO).toList();
    }

    @Override
    public List<TowerDTO> getAllTowersForADateByTrainingClassId(Long id, int dayNth) {
        TrainingClass tc = trainingClassRepository.findByIdAndStatus(id, true).orElseThrow();
        List<Unit> units = unitService.getListUnitsInASessionByTrainingClassId(id, dayNth);
        List<TrainingClassUnitInformation> list = units.stream().map(p-> trainingClassUnitInformationRepository.findByUnitAndTrainingClassAndStatus(p, tc, true).orElseThrow()).toList();
        List<Tower> towers = list.stream().map(p-> repository.findByIdAndStatus(p.getTower().getId(), true).orElseThrow()).distinct().toList();
        return towers.stream().map(TowerMapper.INSTANCE::toDTO).toList();
    }
}
