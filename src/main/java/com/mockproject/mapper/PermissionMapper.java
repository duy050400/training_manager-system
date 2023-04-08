package com.mockproject.mapper;

import com.mockproject.dto.PermissionDTO;
import com.mockproject.entity.Permission;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface PermissionMapper {

    PermissionMapper INSTANCE = Mappers.getMapper(PermissionMapper.class);

    PermissionDTO toDTO(Permission permission);

    Permission toEntity(PermissionDTO dto);

}
