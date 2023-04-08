package com.mockproject.service;

import com.mockproject.dto.LocationDTO;
import com.mockproject.entity.Location;
import com.mockproject.mapper.LocationMapper;
import com.mockproject.repository.LocationRepository;
import com.mockproject.service.interfaces.ILocationService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class LocationService implements ILocationService {

    private final LocationRepository locationRepo;

    @Override
    public List<LocationDTO> listAllTrue() {
        return locationRepo.findByStatus(true).stream().map(LocationMapper.INSTANCE::toDTO).toList();
    }

    @Override
    public List<LocationDTO> getAllLocation(boolean status) {
        return locationRepo.findAllByStatus(status).stream().map(LocationMapper.INSTANCE::toDTO).collect(Collectors.toList());
    }
    @Override
    public List<Location> findLocationByTrainingClassID(Long id) {
        return locationRepo.findDistinctAllByListTowersListTrainingClassUnitInformationsTrainingClassId(id);
    }

    @Override
    public LocationDTO getLocationById(boolean status, Long id) {
        Location location = locationRepo.findByStatusAndId(status, id).orElseThrow(() -> new NotFoundException("Location not found with id: " + id));
        return LocationMapper.INSTANCE.toDTO(location);
    }
}
