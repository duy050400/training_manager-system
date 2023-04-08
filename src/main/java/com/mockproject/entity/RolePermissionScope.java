package com.mockproject.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "RolePermissionScope")
@Table(name = "tblRolePermissionScope")
public class RolePermissionScope implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(
            name = "id",
            updatable = false
    )
    private Long id;

    @Column(name = "status")
    private boolean status;

    @ManyToOne
    @JsonManagedReference
    @JoinColumn(name = "role_id")
    private Role role;

    @ManyToOne
    @JsonManagedReference
    @JoinColumn(name = "permission_id")
    private Permission permission;

    @ManyToOne
    @JsonManagedReference
    @JoinColumn(name = "scope_id")
    private PermissionScope permissionScope;
}
