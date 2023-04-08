package com.mockproject.service;

import com.mockproject.dto.FormRoleDTO;
import com.mockproject.dto.RoleDTO;
import com.mockproject.entity.Role;
import com.mockproject.entity.RolePermissionScope;
import com.mockproject.mapper.RoleMapper;
import com.mockproject.repository.RolePermissionScopeRepository;
import com.mockproject.repository.RoleRepository;
import com.mockproject.service.interfaces.IRoleService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class RoleService implements IRoleService {

    private final RoleRepository repository;

    private final RolePermissionScopeRepository rolePermissionScopeRepository;

    @Override
    public RoleDTO getRoleById(Long id){
        Optional<Role> role = repository.getRoleById(id);
        if (role.isPresent()){
            return RoleMapper.INSTANCE.toDTO(role.get());
        }
        return null;
    }

    @Override
    public RoleDTO getRoleByRoleName(String roleName) {
        Optional<Role> role = repository.getRoleByRoleName(roleName);
        if (role.isPresent()){
            return RoleMapper.INSTANCE.toDTO(repository.getRoleByRoleName(roleName).get());
        }
        else return null;
    }

    @Override
    public List<RoleDTO> getAll() {
        return repository.findAll().stream().map(RoleMapper.INSTANCE::toDTO).collect(Collectors.toList());
    }

    @Override
    public List<Role> updateListRole(List<RoleDTO> listRoleDTO) {
        return repository.saveAll(listRoleDTO.stream().map(RoleMapper.INSTANCE::toEntity).collect(Collectors.toList()));
    }

    @Override
    public Role save(RoleDTO roleDTO) {
        return repository.save(RoleMapper.INSTANCE.toEntity(roleDTO));
    }

    @Override
    public Boolean checkDuplicatedByRoleIdAndRoleName(Long id, String name) {
        List<Role> role = repository.getRoleByIdIsNotAndRoleName(id, name);
        if (!role.isEmpty()){
            return true;
        } else return false;
    }

    @Override
    public List<FormRoleDTO> getAllRoleDetail() {
        List<RoleDTO> roleDTOList = getAll();

        List<FormRoleDTO> list = new ArrayList<>();
        for (RoleDTO role : roleDTOList) {
            FormRoleDTO roleDTO = new FormRoleDTO();
            roleDTO.setId(role.getId());
            roleDTO.setRoleName(role.getRoleName());
            List<RolePermissionScope> listRolePermissionScope = rolePermissionScopeRepository.findAllByRoleId(role.getId());
            for (RolePermissionScope rpc : listRolePermissionScope) {
                switch (rpc.getPermissionScope().getScopeName()) {
                    case "Syllabus":
                        roleDTO.setSyllabusPermission(rpc.getPermission().getPermissionName());
                        break;
                    case "Training program":
                        roleDTO.setTraningProgramPermission(rpc.getPermission().getPermissionName());
                        break;
                    case "Class":
                        roleDTO.setClassPermission(rpc.getPermission().getPermissionName());
                        break;
                    case "Learning material":
                        roleDTO.setLeaningMaterialPermission(rpc.getPermission().getPermissionName());
                        break;
                    case "User":
                        roleDTO.setUserPermission(rpc.getPermission().getPermissionName());
                        break;
                }
            }
            list.add(roleDTO);


        }
        return list;
    }

}
