package com.mockproject.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDTOCustom {
    private Long id;
    private String email;
    private String fullName;
    private String image;
    private String state;
    private LocalDate dob;
    private String phone;
    private boolean gender;
    private boolean status;
    private RoleDTO role;
    private LevelDTO level;
    private AttendeeDTO attendee;
}
