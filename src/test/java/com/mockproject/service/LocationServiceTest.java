package com.mockproject.service;

import com.mockproject.dto.LocationDTO;
import com.mockproject.entity.Location;
import com.mockproject.repository.LocationRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ContextConfiguration(classes = {LocationService.class})
@ExtendWith(SpringExtension.class)
class LocationServiceTest {
    @MockBean
    private LocationRepository locationRepository;

    @Autowired
    private LocationService locationService;

    Location location1 = new Location(1L, "Location 1", "123 Le Loi", true, null, null);
    Location location2 = new Location(2L, "Location 2", "333 Le Hong Phong", false, null, null);
    Location location3 = new Location(3L, "Location 3", "55 Nguyen Trai", true, null, null);

    /**
     * Method under test: {@link LocationService#listAllTrue()}
     */
    @Test
    void canListAllLocationWithStatusTrue() {
        List<Location> list = new ArrayList<>();
        list.add(location1);
        list.add(location2);
        list.add(location3);

        when(locationRepository.findByStatus(true)).thenReturn(list.stream().filter(Location::isStatus).toList());

        List<LocationDTO> result = locationService.listAllTrue();

        assertEquals(2, result.size());
        assertEquals(1L, result.get(0).getId());
        assertEquals("123 Le Loi", result.get(0).getAddress());
        assertEquals(3L, result.get(1).getId());
        assertEquals("Location 3", result.get(1).getLocationName());


        verify(locationRepository).findByStatus(true);
    }
}

