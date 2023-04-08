package com.mockproject.service;

import com.mockproject.dto.PermissionDTO;
import com.mockproject.mapper.PermissionMapper;
import com.mockproject.repository.PermissionRepository;
import com.mockproject.service.interfaces.IPermissionService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class PermissionService implements IPermissionService {

    private final PermissionRepository repository;

    @Override
    public List<PermissionDTO> getAll() {
        return repository.findAll().stream().map(PermissionMapper.INSTANCE::toDTO).collect(Collectors.toList());
    }

    @Override
    public Long getPermissionIdByName(String name) {
        return repository.getPermissionsByPermissionName(name).getId();
    }
}
