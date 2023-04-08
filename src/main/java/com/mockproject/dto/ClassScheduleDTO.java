package com.mockproject.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClassScheduleDTO implements Serializable {
    private Long id;
    private LocalDate date;
    private boolean status;
    private Long trainingClassId;
    private String trainingClassName;
}
