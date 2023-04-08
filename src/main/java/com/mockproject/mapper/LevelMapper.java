package com.mockproject.mapper;

import com.mockproject.dto.LevelDTO;
import com.mockproject.entity.Level;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")

public interface LevelMapper {

    LevelMapper INSTANCE = Mappers.getMapper(LevelMapper.class);

    LevelDTO toDTO(Level level);

    Level toEntity(LevelDTO dto);


}
