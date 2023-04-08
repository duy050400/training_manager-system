package com.mockproject.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TrainingClassUnitInformationDTO implements Serializable {
    @Schema(hidden = true)
    private Long id;
    private boolean status;
    private Long trainerId;
    private String trainerName;
    private Long unitId;
    private String unitTitle;
    private Long trainingClassId;
    private String trainingClassName;
    private Long towerId;
    private String towerName;
}
