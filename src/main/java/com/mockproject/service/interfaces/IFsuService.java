package com.mockproject.service.interfaces;

import com.mockproject.dto.FsuDTO;

import java.util.List;

public interface IFsuService {

    FsuDTO getFsuById(boolean status, Long id);

    List<FsuDTO> getAllFsu(boolean status);

    List<FsuDTO> listAllTrue();

    FsuDTO getFsuByTrainingClassId(Long id);
}
