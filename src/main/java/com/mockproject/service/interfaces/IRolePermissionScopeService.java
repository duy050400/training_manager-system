package com.mockproject.service.interfaces;

import com.mockproject.dto.RolePermissionScopeDTO;
import com.mockproject.entity.RolePermissionScope;

import java.util.List;

public interface IRolePermissionScopeService {
    public List<RolePermissionScope> findAllByRoleId(Long roleId);

     List<RolePermissionScope> updateRolePermissionScope(List<RolePermissionScopeDTO> listRolePermissionScopeDTO);

     RolePermissionScope save(RolePermissionScopeDTO rolePermissionScopeDTO);

     RolePermissionScopeDTO updateRolePermissionScopeByPermissionNameAndRoleIdAndScopeId(String permissionName, Long roleId, Long scopeId);
}
