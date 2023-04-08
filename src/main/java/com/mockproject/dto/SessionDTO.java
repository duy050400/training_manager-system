package com.mockproject.dto;

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
public class SessionDTO implements Serializable {
    private Long id;
    private int sessionNumber;
    private boolean status;
    private Long syllabusId;
    private String syllabusName;
    private List<UnitDTO> unitDTOList;
}
