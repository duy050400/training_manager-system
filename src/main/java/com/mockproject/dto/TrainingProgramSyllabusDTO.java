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
public class TrainingProgramSyllabusDTO implements Serializable {
    private Long id;
    private boolean status;
    private Long syllabusId;
    private String syllabusName;
    private Long trainingProgramId;
    private String trainingProgramName;
}
