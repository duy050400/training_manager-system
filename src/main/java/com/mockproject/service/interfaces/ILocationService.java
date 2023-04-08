package com.mockproject.service.interfaces;

import com.mockproject.dto.LocationDTO;
import com.mockproject.entity.Location;

import java.util.List;

public interface ILocationService {

    List<LocationDTO> listAllTrue();

    List<Location> findLocationByTrainingClassID(Long id);

    List<LocationDTO> getAllLocation(boolean status);

    LocationDTO getLocationById(boolean status, Long id);
}
