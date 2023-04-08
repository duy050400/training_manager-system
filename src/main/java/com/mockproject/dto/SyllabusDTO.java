package com.mockproject.dto;


import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SyllabusDTO implements Serializable {
    private Long id;
    private String name;
    @NotEmpty
    private String code;
    @NotNull
    private String version;
    @NotNull
    private String level;
    @NotNull
    @Min(0)
    private int attendee;
    private BigDecimal hour;
    private int day;
    @NotNull
    private String technicalRequirements;
    @NotNull
    private String courseObjectives;
    private LocalDate dateCreated;
    private LocalDate lastDateModified;
    @NotNull
    @Min(0)
    private BigDecimal quiz;
    @NotNull
    @Min(0)
    private BigDecimal assignment;
    @NotNull
    @Min(0)
    private BigDecimal finalExam;
    @NotNull
    @Min(0)
    private BigDecimal finalTheory;
    @NotNull
    @Min(0)
    private BigDecimal finalPractice;
    @NotNull
    @Min(0)
    private BigDecimal gpa;
    @NotNull
    private String trainingDes;
    @NotNull
    private String reTestDes;
    @NotNull
    private String markingDes;
    @NotNull
    private String waiverCriteriaDes;
    @NotNull
    private String otherDes;
    @NotNull
    private boolean state;
    @NotNull
    private boolean status;



    private Long creatorId;
    private String creatorName;
    private Long lastModifierId;
    private String lastModifierName;
    private List<SessionDTO> sessionDTOList;
    private List<String> outputStandardCodeList;
}
