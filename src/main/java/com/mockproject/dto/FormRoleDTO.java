package com.mockproject.dto;

import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class FormRoleDTO {
    private Long id;

    private String roleName;

    private String syllabusPermission;

    private String traningProgramPermission;

    private String classPermission;

    private String leaningMaterialPermission;

    private String userPermission;
}
