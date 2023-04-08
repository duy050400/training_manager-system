package com.mockproject.service;

import com.mockproject.dto.FsuDTO;
import com.mockproject.entity.Fsu;
import com.mockproject.entity.TrainingClass;
import com.mockproject.mapper.FsuMapper;
import com.mockproject.repository.FsuRepository;
import com.mockproject.repository.TrainingClassRepository;
import com.mockproject.service.interfaces.IFsuService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class FsuService implements IFsuService {

    private final FsuRepository fsuRepo;
    private final TrainingClassRepository trainingClassRepository;

    @Override
    public List<FsuDTO> listAllTrue() {
        return fsuRepo.findByStatus(true).stream().map(FsuMapper.INSTANCE::toDTO).collect(Collectors.toList());
    }

    @Override
    public FsuDTO getFsuByTrainingClassId(Long id) {
        TrainingClass tc = trainingClassRepository.findByIdAndStatus(id, true).orElseThrow();
        if(!tc.getFsu().isStatus()){throw new NullPointerException();}
        return FsuMapper.INSTANCE.toDTO(tc.getFsu());
    }

    @Override
    public FsuDTO getFsuById(boolean status, Long id) {
        Fsu fsu = fsuRepo.findByStatusAndId(status, id).orElseThrow(() -> new NotFoundException("Fsu not found with id: "+ id));
        return FsuMapper.INSTANCE.toDTO(fsu);
    }

    @Override
    public List<FsuDTO> getAllFsu(boolean status) {
        return fsuRepo.findAllByStatus(status).stream().map(FsuMapper.INSTANCE::toDTO).collect(Collectors.toList());
    }
}
