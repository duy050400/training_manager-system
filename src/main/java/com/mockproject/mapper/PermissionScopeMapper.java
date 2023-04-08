package com.mockproject.mapper;

import com.mockproject.dto.PermissionScopeDTO;
import com.mockproject.entity.PermissionScope;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface PermissionScopeMapper {

    PermissionScopeMapper INSTANCE = Mappers.getMapper(PermissionScopeMapper.class);

    PermissionScopeDTO toDTO(PermissionScope permissionScope);

    PermissionScope toEntity(PermissionScopeDTO dto);

}
