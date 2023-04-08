package com.mockproject.service.interfaces;

import com.mockproject.dto.PermissionDTO;

import java.util.List;

public interface IPermissionService {
    List<PermissionDTO> getAll();

    Long getPermissionIdByName(String name);
}
