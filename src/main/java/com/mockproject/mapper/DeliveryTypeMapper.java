package com.mockproject.mapper;

import com.mockproject.dto.DeliveryTypeDTO;
import com.mockproject.entity.DeliveryType;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface DeliveryTypeMapper {

    DeliveryTypeMapper INSTANCE = Mappers.getMapper(DeliveryTypeMapper.class);

    DeliveryTypeDTO toDTO(DeliveryType deliveryType);

    DeliveryType toEntity(DeliveryTypeDTO dto);
}
