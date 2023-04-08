package com.mockproject.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoleDTO implements Serializable {

    public RoleDTO(String roleName, boolean status) {
        this.roleName = roleName;
        this.status = status;
    }
    private Long id;
    private String roleName;
    private boolean status;
}
