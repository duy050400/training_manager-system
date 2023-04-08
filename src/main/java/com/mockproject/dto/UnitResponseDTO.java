package com.mockproject.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UnitResponseDTO {
    private String unitTitle;
    private int unitNumber;
}
