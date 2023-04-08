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
public class TowerDTO implements Serializable {
    private Long id;
    private String towerName;
    private String address;
    private boolean status;
    private Long locationId;
    private String locationName;
}
