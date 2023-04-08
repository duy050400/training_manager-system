package com.mockproject.mapper;

import com.mockproject.dto.OutputStandardDTO;
import com.mockproject.entity.OutputStandard;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface OutputStandardMapper {

    OutputStandardMapper INSTANCE = Mappers.getMapper(OutputStandardMapper.class);

    OutputStandardDTO toDTO(OutputStandard outputStandard);

    OutputStandard toEntity(OutputStandardDTO dto);

}
