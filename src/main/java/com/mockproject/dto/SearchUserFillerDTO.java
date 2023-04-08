package com.mockproject.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SearchUserFillerDTO {
    private Long id;
    private LocalDate dob;
    private  String email;
    private  String fullname;
    private Boolean gender;
    private String phone;
    private Long state;
    private Long attendeeId;
    private String attendeeName;
    private Long levelId;
    private String levelCode;
    private Long roleId;
    private String roleName;
}
