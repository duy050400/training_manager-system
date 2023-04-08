package com.mockproject.repository;

import com.mockproject.entity.RolePermissionScope;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RolePermissionScopeRepository extends JpaRepository<RolePermissionScope, Long> {

    List<RolePermissionScope> findAllByRoleId(Long roleId);

    RolePermissionScope findByRoleIdAndAndPermissionScopeId(Long roleId, Long permissionScopeId);
}
