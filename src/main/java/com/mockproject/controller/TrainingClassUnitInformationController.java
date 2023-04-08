
package com.mockproject.controller;

import com.mockproject.dto.TrainingClassUnitInformationDTO;
import com.mockproject.service.interfaces.ITrainingClassUnitInformationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@Tag(name = "Training Class Unit Information API")
@RequestMapping("api/training-class-unit-information")
@SecurityRequirement(name = "Authorization")
@Slf4j
public class TrainingClassUnitInformationController {

    public static final String VIEW = "ROLE_View_Class";
    public static final String MODIFY = "ROLE_Modify_Class";
    public static final String CREATE = "ROLE_Create_Class";
    public static final String FULL_ACCESS = "ROLE_Full access_Class";

    private final ITrainingClassUnitInformationService service;

    @ApiResponses(value = {
        @ApiResponse(responseCode = "201",description = "When list of Information saved successfully"),
        @ApiResponse(responseCode = "400", description = "When saving Fail!")
    })
    @Operation(summary = "Save list of Unit Information when creating Class")

    @PostMapping("/list")
    @Secured({MODIFY, CREATE, FULL_ACCESS})
    public ResponseEntity<?> createListOfInformation(@Valid @RequestBody List<TrainingClassUnitInformationDTO> listDto) {
        if(service.saveList(listDto)){
            return new ResponseEntity<>("List of Unit Information have been save!", HttpStatus.CREATED);
        }else{
            return new ResponseEntity<>("Save Fail!", HttpStatus.BAD_REQUEST);
        }
    }
}