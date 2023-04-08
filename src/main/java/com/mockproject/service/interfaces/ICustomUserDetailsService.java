package com.mockproject.service.interfaces;

import com.mockproject.entity.RolePermissionScope;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

public interface ICustomUserDetailsService {

    UserDetails loadUserById(Long id) throws Exception;

    List<String> generateAuthoriessByRoleId(List<RolePermissionScope> rolePermissionScopeEntityList);
}
