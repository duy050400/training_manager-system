package com.mockproject.service;

import com.mockproject.dto.TrainingClassAdminDTO;
import com.mockproject.entity.TrainingClassAdmin;
import com.mockproject.mapper.TrainingClassAdminMapper;
import com.mockproject.repository.TrainingClassAdminRepository;
import com.mockproject.service.interfaces.ITrainingClassAdminService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class TrainingClassAdminService implements ITrainingClassAdminService {

    private final TrainingClassAdminRepository repository;

    @Override
    public boolean saveList(List<Long> adminId, Long tcId) {
        List<TrainingClassAdmin> list =
                adminId.stream().map(p -> TrainingClassAdminMapper.INSTANCE.toEntity(
                                        new TrainingClassAdminDTO(null, true, p,null, tcId, null))
                                    ).toList();
        List<TrainingClassAdmin> result = repository.saveAll(list);
        return !result.isEmpty();
    }

}
