package com.mockproject.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Time;
import java.time.LocalDate;
import java.util.List;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TrainingClassFilterResponseDTO {
    private Long id;
    private String className;
    private String classCode;
    private Time startTime;
    private List<String> TrainerName;
    private List<String> AdminName;
    private String attendee;
    private LocalDate day;
    private String durationDay;
    private String LocationName;
    private List<UnitResponseDTO> units;
}

