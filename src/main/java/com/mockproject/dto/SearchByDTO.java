package com.mockproject.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SearchByDTO {
    @NotEmpty(message = "searchText can not be null.")
    private List<String> searchText;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate nowDate;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;
    @JsonFormat(pattern = "yyyy-MM-dd")

    private LocalDate endDate;
}
