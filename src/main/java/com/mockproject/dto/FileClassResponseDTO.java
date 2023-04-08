package com.mockproject.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.sql.Time;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FileClassResponseDTO {

    private String className;

    private LocalDate startDate;

    private Time startTime;

    private Time endTime;

    private BigDecimal hour;

    private int day;

    private int planned;

    private int accepted;

    private int actual;

    private Long locationId;

    private String locationName;

    private Long fsuId;

    private String fsuName;

    private Long contactId;

    private String contactEmail;

    private Long trainingProgramId;

    private String trainingProgramName;

    private Long attendeeId;

    private String attendeeName;

    private List<Map<Long,String>> listAdmin;

}
