package com.mockproject.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TrainingClassFilterRequestDTO {
    List<String> locations;

    @JsonFormat(pattern = "yyyy-MM-dd")
    LocalDate startDate;
    @JsonFormat(pattern = "yyyy-MM-dd")
    LocalDate endDate;
    @JsonFormat(pattern = "yyyy-MM-dd")

    LocalDate nowDate;
    List<TimeRangeDTO> TimeRanges;
    List<String> statuses;
    List<String> attendees;
    String fsu;
    String trainer;
}
