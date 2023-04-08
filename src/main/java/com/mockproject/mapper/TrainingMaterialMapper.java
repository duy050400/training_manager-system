package com.mockproject.mapper;

import com.mockproject.dto.TrainingMaterialDTO;
import com.mockproject.entity.TrainingMaterial;
import com.mockproject.entity.UnitDetail;
import com.mockproject.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface TrainingMaterialMapper {

    TrainingMaterialMapper INSTANCE = Mappers.getMapper(TrainingMaterialMapper.class);

    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "userName", source = "user.fullName")
    @Mapping(target = "unitDetailId", source = "unitDetail.id")
    @Mapping(target = "unitDetailTitle", source = "unitDetail.title")
    TrainingMaterialDTO toDTO(TrainingMaterial trainingMaterial);

    @Mapping(target = "user", source = "userId", qualifiedByName = "mapUser")
    @Mapping(target = "unitDetail", source = "unitDetailId", qualifiedByName = "mapUnitDetail")
    TrainingMaterial toEntity(TrainingMaterialDTO dto);


    @Named("mapUser")
    default User mapUser(Long id) {
        User user = new User();
        user.setId(id);
        return user;
    }

    @Named("mapUnitDetail")
    default UnitDetail mapUnitDetail(Long id) {
        UnitDetail unitDetail = new UnitDetail();
        unitDetail.setId(id);
        return unitDetail;
    }

}
