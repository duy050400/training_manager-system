package com.mockproject.controller;

import com.mockproject.service.interfaces.ITrainingClassAdminService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@Tag(name = "Training Class Admin API")
@RequestMapping("api/training-class-admin")
@SecurityRequirement(name = "Authorization")
public class TrainingClassAdminController {

    public static final String VIEW = "ROLE_View_Class";
    public static final String MODIFY = "ROLE_Modify_Class";
    public static final String CREATE = "ROLE_Create_Class";
    public static final String FULL_ACCESS = "ROLE_Full access_Class";

    private final ITrainingClassAdminService service;

    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "When list saved successful!"),
            @ApiResponse(responseCode = "400", description = "When saving fail!")
    })
    @Operation(summary = "Save list of class admin by given Training Class Id and List ID of admin")
    @PostMapping("training-class/{tcId}")
    @Secured({MODIFY, CREATE, FULL_ACCESS})
    public ResponseEntity<?> createListClassAdmin(@io.swagger.v3.oas.annotations.parameters.RequestBody(
                                                            content = @Content(examples = @ExampleObject(value = "[1, 2, 3]") ))
                                                  @RequestBody List<Long> listAdminId,
                                                  @Parameter(description = "Training Class ID when call create Training Class API return")
                                                  @PathVariable("tcId") Long tcId){
        if(service.saveList(listAdminId, tcId)){
            return new ResponseEntity<>("List Class Admin have been saved successfully!", HttpStatus.CREATED);
        }
        return new ResponseEntity<>("Saving fail!", HttpStatus.BAD_REQUEST);
    }
}
