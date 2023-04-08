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
public class TrainingClassAdminDTO implements Serializable {
    private Long id;
    private boolean status;
    private Long adminId;
    private String adminName;
    private Long trainingClassId;
    private String trainingClassName;
}
