package com.mockproject.repository;

import com.mockproject.entity.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface PermissionRepository extends JpaRepository<Permission, Long> {

    Permission getPermissionsByPermissionName(String name);
}
