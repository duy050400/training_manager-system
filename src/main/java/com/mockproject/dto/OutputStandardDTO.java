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
public class OutputStandardDTO implements Serializable {
    private Long id;
    private String standardCode;
    private String standardName;
    private String description;
    private boolean status;

}
