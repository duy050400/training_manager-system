package com.mockproject.mapper;

import com.mockproject.dto.RoleDTO;
import com.mockproject.entity.Role;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface RoleMapper {

    RoleMapper INSTANCE = Mappers.getMapper(RoleMapper.class);

    RoleDTO toDTO(Role role);

    Role toEntity(RoleDTO dto);

}
