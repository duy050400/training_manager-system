package com.mockproject.controller;

import com.mockproject.dto.LocationDTO;
import com.mockproject.service.interfaces.ILocationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@Tag(name = "Location API")
@RequestMapping("api/location")
@SecurityRequirement(name = "Authorization")
public class LocationController {

    public static final String VIEW = "ROLE_View_Class";
    public static final String MODIFY = "ROLE_Modify_Class";
    public static final String CREATE = "ROLE_Create_Class";
    public static final String FULL_ACCESS = "ROLE_Full access_Class";

    private final ILocationService locationService;

    @ApiResponses(value = {
            @ApiResponse(responseCode = "404", description = "When don't find any Location"),
            @ApiResponse(responseCode = "200", description = "When found location",
                    content = @Content(schema = @Schema(implementation = LocationDTO.class)))
    })
    @Operation(summary = "Get all Location have status true")
    @GetMapping("")
    @Secured({VIEW, MODIFY, CREATE, FULL_ACCESS})
    public ResponseEntity<?> listAllTrue() {
        List<LocationDTO> list = locationService.listAllTrue();
        if (!list.isEmpty()) {
            return ResponseEntity.ok(list);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Don't find any Location!");
        }
    }

    @GetMapping("/list")
    @Operation(
            summary = "Get location list"
    )
    @Secured({VIEW, MODIFY, CREATE, FULL_ACCESS})
    public ResponseEntity<?> getAllLocation(){
        return ResponseEntity.ok(locationService.getAllLocation(true));
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Get location by ID"
    )
    @Secured({VIEW, MODIFY, CREATE, FULL_ACCESS})
    public ResponseEntity<?> getLocationById(
            @PathVariable("id")
            @Parameter(
                    description = "<b>Insert ID to get location<b>",
                    example = "1"
            ) Long id) {
        return ResponseEntity.ok(locationService.getLocationById(true, id));
    }
}
