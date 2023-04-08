package com.mockproject.service;

import com.mockproject.dto.LevelDTO;
import com.mockproject.entity.Level;
import com.mockproject.mapper.LevelMapper;
import com.mockproject.repository.LevelRepository;
import com.mockproject.service.interfaces.ILevelService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class LevelService implements ILevelService {

    private final LevelRepository repository;

    @Override
    public LevelDTO getLevelById(Long id){
        Optional<Level> level = repository.getLevelById(id);
        if (level.isPresent()){
            return LevelMapper.INSTANCE.toDTO(level.get());
        }

        return null;
    }

    @Override
    public Long getLevelByLevelCode(String levelCode) {
        Long levelId = repository.getLevelByLevelCode(levelCode).get().getId();
        return levelId;
    }

}
