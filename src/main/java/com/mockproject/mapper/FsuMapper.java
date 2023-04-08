package com.mockproject.mapper;

import com.mockproject.dto.FsuDTO;
import com.mockproject.entity.Fsu;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface FsuMapper {

    FsuMapper INSTANCE = Mappers.getMapper(FsuMapper.class);

    FsuDTO toDTO(Fsu fsu);

    Fsu toEntity(FsuDTO dto);

}
