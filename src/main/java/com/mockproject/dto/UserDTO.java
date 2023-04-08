package com.mockproject.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO implements Serializable {

    public UserDTO(String email, String fullName, String image, int state, LocalDate dob, String phone, boolean gender, boolean status, Long roleId, Long levelId, Long attendeeId) {
        this.email = email;
        this.fullName = fullName;
        this.image = image;
        this.state = state;
        this.dob = dob;
        this.phone = phone;
        this.gender = gender;
        this.status = status;
        this.roleId = roleId;
        this.levelId = levelId;
        this.attendeeId = attendeeId;
    }



    private Long id;
    private String email;
    private String fullName;
    private String image;
    private int state;

    private LocalDate dob;
    private String phone;
    private boolean gender;
    private boolean status;
    private Long roleId;
    private String roleName;
    private Long levelId;
    private String levelCode;
    private Long attendeeId;
    private String attendeeName;

    private String stateName;
}
