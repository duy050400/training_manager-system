package com.mockproject.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class TrainingProgramAddDto {
    @NotNull(message = "Name is null")
    @NotEmpty(message = "Name is empty")
    private String name;

    @NotNull(message = "SyllabusId is null")
    @NotEmpty(message = "SyllabusId is empty")
    private List<Long> syllabusIdList;
}
