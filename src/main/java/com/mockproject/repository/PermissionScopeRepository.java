package com.mockproject.repository;

import com.mockproject.entity.PermissionScope;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PermissionScopeRepository extends JpaRepository<PermissionScope, Long> {

    Optional<PermissionScope> getPermissionScopeByScopeName(String name);
}
