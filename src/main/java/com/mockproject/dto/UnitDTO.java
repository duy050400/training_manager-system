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
public class UnitDTO implements Serializable {

    private Long id;
    @NotNull
    private String unitTitle;
    @Min(0)
    private int unitNumber;
    private BigDecimal duration;
    private boolean status;
    private Long sessionId;
    private String sessionNumber;
    private List<UnitDetailDTO> unitDetailDTOList;
}
