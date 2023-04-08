package com.mockproject.service;

import com.mockproject.dto.RolePermissionScopeDTO;
import com.mockproject.entity.RolePermissionScope;
import com.mockproject.mapper.RolePermissionScopeMapper;
import com.mockproject.repository.PermissionRepository;
import com.mockproject.repository.RolePermissionScopeRepository;
import com.mockproject.service.interfaces.IRolePermissionScopeService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class RolePermissionScopeService implements IRolePermissionScopeService {

    private final RolePermissionScopeRepository repository;
    private final PermissionRepository permissionRepository;

    @Override
    public List<RolePermissionScope> findAllByRoleId(Long roleId) {
        return repository.findAllByRoleId(roleId);
    }

    @Override
    public List<RolePermissionScope> updateRolePermissionScope(List<RolePermissionScopeDTO> listRolePermissionScopeDTO) {
        return repository.saveAll(listRolePermissionScopeDTO.stream().map(RolePermissionScopeMapper.INSTANCE::toEntity).collect(Collectors.toList()));
    }

    @Override
    public RolePermissionScope save(RolePermissionScopeDTO rolePermissionScopeDTO) {
        return repository.save(RolePermissionScopeMapper.INSTANCE.toEntity(rolePermissionScopeDTO));
    }

    @Override
    public RolePermissionScopeDTO updateRolePermissionScopeByPermissionNameAndRoleIdAndScopeId(String permissionName, Long roleId, Long scopeId) {
        Long permissionId = permissionRepository.getPermissionsByPermissionName(permissionName).getId();
        RolePermissionScopeDTO rolePermissionScopeDTO = RolePermissionScopeMapper.INSTANCE.toDTO(repository.findByRoleIdAndAndPermissionScopeId(roleId, scopeId));
        rolePermissionScopeDTO.setPermissionId(permissionId);
        repository.save(RolePermissionScopeMapper.INSTANCE.toEntity(rolePermissionScopeDTO));
        return null;
    }

}
