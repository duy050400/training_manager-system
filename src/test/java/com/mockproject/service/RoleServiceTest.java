package com.mockproject.service;

import com.mockproject.dto.RoleDTO;
import com.mockproject.entity.Role;
import com.mockproject.mapper.RoleMapper;
import com.mockproject.repository.RolePermissionScopeRepository;
import com.mockproject.repository.RoleRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ContextConfiguration(classes = {RoleService.class})
@ExtendWith(SpringExtension.class)
public class RoleServiceTest {

    @MockBean
    private RoleRepository repository;

    @MockBean
    private RolePermissionScopeRepository rolePermissionScopeRepository;

    @Autowired
    private RoleService roleService;

    Role role1 = new Role(1L, "Super Admin", true, null, null);
    Role role2 = new Role(2L, "Class Admin", true, null, null);

    Role role3 = new Role(1L, "Trainer", true, null, null);
    Role role4 = new Role(2L, "Student", true, null, null);



    @Test
    void getRoleByIdTest(){
        List<Role> list = new ArrayList<>();
        list.add(role1);
        list.add(role2);

        when(repository.getRoleById(1L)).thenReturn(Optional.ofNullable(list.stream().filter(p -> p.getId() == 1L).collect(Collectors.toList()).get(0)));

        RoleDTO result = roleService.getRoleById(1L);

        assertEquals("Super Admin", result.getRoleName());

        verify(repository,times(1)).getRoleById(1L);
    }

    @Test
    void getRoleByRoleNameTest(){
        List<Role> list = new ArrayList<>();
        list.add(role1);
        list.add(role2);

        when(repository.getRoleByRoleName("Class Admin")).thenReturn(Optional.ofNullable(list.stream().filter(p -> p.getRoleName() == "Class Admin").collect(Collectors.toList()).get(0)));
        RoleDTO result = roleService.getRoleByRoleName("Class Admin");
        assertEquals(RoleMapper.INSTANCE.toDTO(role2), result);

        result = roleService.getRoleByRoleName("Class Ad");
        assertEquals(null, result);

        verify(repository,times(2)).getRoleByRoleName("Class Admin");
    }

    @Test
    void getAllRoleTest(){
        List<Role> list = new ArrayList<>();
        list.add(role1);
        list.add(role2);

        when(repository.findAll()).thenReturn(list.stream().collect(Collectors.toList()));

        List<RoleDTO> result = roleService.getAll();

        assertEquals(list.stream().map(RoleMapper.INSTANCE::toDTO).collect(Collectors.toList()), result);

        verify(repository, times(1)).findAll();
    }

    @Test
    void updateListRoleTest(){
        List<Role> list = new ArrayList<>();
        list.add(role1);
        list.add(role2);

        List<Role> listUpdate = new ArrayList<>();
        listUpdate.add(role3);
        listUpdate.add(role4);


        when(repository.saveAll(listUpdate)).thenReturn(listUpdate.stream().collect(Collectors.toList()));

        List<Role> result = roleService.updateListRole(listUpdate.stream().map(RoleMapper.INSTANCE::toDTO).collect(Collectors.toList()));

        assertEquals(listUpdate, result);

        verify(repository, times(1)).saveAll(listUpdate);
    }

    @Test
    void saveRoleTest(){

        when(repository.save(role1)).thenReturn(role1);
        Role result = roleService.save(RoleMapper.INSTANCE.toDTO(role1));

        assertEquals(role1, result);

        verify(repository, times(1)).save(role1);
    }

    @Test
    void checkDuplicatedByRoleIdAndRoleNameTest(){
        List<Role> list = new ArrayList<>();
        list.add(role1);
        list.add(role2);

        when(repository.getRoleByIdIsNotAndRoleName(1L, "Class Admin")).thenReturn(list.stream().filter(p -> p.getRoleName() == "Class Admin" && p.getId() != 1L).collect(Collectors.toList()));

        Boolean result = roleService.checkDuplicatedByRoleIdAndRoleName(1L, "Class Admin");
        assertEquals(true, result);

        verify(repository, times(1)).getRoleByIdIsNotAndRoleName(1L, "Class Admin");


        when(repository.getRoleByIdIsNotAndRoleName(1L, "Super Admin")).thenReturn(list.stream().filter(p -> p.getRoleName() == "Super Admin" && p.getId() != 1L).collect(Collectors.toList()));
        result = roleService.checkDuplicatedByRoleIdAndRoleName(1L, "Super Admin");
        assertEquals(false, result);

        verify(repository,times(1)).getRoleByIdIsNotAndRoleName(1L, "Super Admin");

    }


}
