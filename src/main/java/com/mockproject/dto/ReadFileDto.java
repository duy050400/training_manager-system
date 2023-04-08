package com.mockproject.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class ReadFileDto {
    MultipartFile file;
    @NotNull(message = "encodingType is null")
    @NotEmpty(message = "encodingType is empty")
    String encodingType;
    @NotNull(message = "separator is null")
    @NotEmpty(message = "separator is empty")
    String separator;
    @NotNull(message = "scanning is null")
    List<String> scanning;
    @NotNull(message = "separator is null")
    @NotEmpty(message = "separator is empty")
    String duplicateHandle;
}
