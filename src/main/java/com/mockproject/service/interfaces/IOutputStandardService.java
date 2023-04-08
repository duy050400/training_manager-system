package com.mockproject.service.interfaces;

import com.mockproject.dto.OutputStandardDTO;

import java.util.List;

public interface IOutputStandardService {

    OutputStandardDTO getOutputStandardById(Long outputStandardId, boolean status);

    List<OutputStandardDTO> getOutputStandard(boolean status);

    List<OutputStandardDTO> getOsdBySyllabusId(boolean status, Long id);
}
