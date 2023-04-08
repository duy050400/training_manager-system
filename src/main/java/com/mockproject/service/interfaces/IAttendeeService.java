package com.mockproject.service.interfaces;

import com.mockproject.dto.AttendeeDTO;
import com.mockproject.entity.Attendee;

import java.util.List;

public interface IAttendeeService {

    List<AttendeeDTO> getAllAttendee(boolean status);

    Attendee save(AttendeeDTO dto);

    AttendeeDTO getAttendeeById(boolean status, Long id);

    List<AttendeeDTO> listAllTrue();

    AttendeeDTO getAttendeeByTrainingClassId(Long id);
}
