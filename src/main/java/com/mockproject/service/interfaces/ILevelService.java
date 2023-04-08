package com.mockproject.service.interfaces;

import com.mockproject.dto.LevelDTO;

public interface ILevelService {

    LevelDTO getLevelById(Long id);

    Long getLevelByLevelCode(String levelCode);
}
