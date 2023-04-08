package com.mockproject.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "Role")
@Table(name = "tblRole")
public class Role implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(
            name = "id",
            updatable = false
    )
    private Long id;

    @Column(
            name = "role_name",
            length = 50,
            nullable = false
    )
    private String roleName;

    @Column(name = "status")
    private boolean status;

    @OneToMany(mappedBy = "role", fetch = FetchType.LAZY)
    @JsonBackReference
    private List<User> listUser;

    @OneToMany(mappedBy = "role", fetch = FetchType.LAZY)
    @JsonBackReference
    private List<RolePermissionScope> listRolePermissionScope;
}
