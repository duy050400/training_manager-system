package com.mockproject.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UnitDetailDTO implements Serializable {
    private Long id;
    private String title;
    @Min(0)
    private BigDecimal duration;
    @NotNull
    private boolean type;
    @NotNull
    private boolean status;
    private Long unitId;
    private String unitTitle;
    private Long deliveryTypeId;
    private String deliveryTypeName;
    private Long outputStandardId;
    private String outputStandardName;
    private String outputStandardCode;
    private List<TrainingMaterialDTO> trainingMaterialDTOList;
}
