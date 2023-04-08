package com.mockproject.mapper;

import com.mockproject.dto.UnitDetailDTO;
import com.mockproject.entity.DeliveryType;
import com.mockproject.entity.OutputStandard;
import com.mockproject.entity.Unit;
import com.mockproject.entity.UnitDetail;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface UnitDetailMapper {

    UnitDetailMapper INSTANCE = Mappers.getMapper(UnitDetailMapper.class);

    @Mapping(target = "unitId", source = "unit.id")
    @Mapping(target = "unitTitle", source = "unit.unitTitle")
    @Mapping(target = "outputStandardId", source = "outputStandard.id")
    @Mapping(target = "outputStandardCode", source = "outputStandard.standardCode")
    @Mapping(target = "outputStandardName", source = "outputStandard.standardName")
    @Mapping(target = "deliveryTypeId", source = "deliveryType.id")
    @Mapping(target = "deliveryTypeName", source = "deliveryType.typeName")
    UnitDetailDTO toDTO(UnitDetail unitDetail);

    @Mapping(target = "unit", source = "unitId", qualifiedByName = "mapUnit")
    @Mapping(target = "outputStandard", source = "outputStandardId", qualifiedByName = "mapOutputStandard")
    @Mapping(target = "deliveryType", source = "deliveryTypeId", qualifiedByName = "mapDeliveryType")
    UnitDetail toEntity(UnitDetailDTO dto);

    @Named("mapUnit")
    default Unit mapUnit(Long id) {
        Unit unit = new Unit();
        unit.setId(id);
        return unit;
    }

    @Named("mapOutputStandard")
    default OutputStandard mapOutputStandard(Long id) {
        OutputStandard outputStandard = new OutputStandard();
        outputStandard.setId(id);
        return outputStandard;
    }

    @Named("mapDeliveryType")
    default DeliveryType mapDeliveryType(Long id) {
        DeliveryType deliveryType = new DeliveryType();
        deliveryType.setId(id);
        return deliveryType;
    }

}
