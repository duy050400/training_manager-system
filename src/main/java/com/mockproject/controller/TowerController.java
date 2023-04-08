package com.mockproject.controller;

import com.mockproject.dto.TowerDTO;
import com.mockproject.service.interfaces.ITowerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
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
@Tag(name = "Tower API")
@RequestMapping("api/tower")
@SecurityRequirement(name = "Authorization")
public class TowerController {

    public static final String VIEW = "ROLE_View_Class";
    public static final String MODIFY = "ROLE_Modify_Class";
    public static final String CREATE = "ROLE_Create_Class";
    public static final String FULL_ACCESS = "ROLE_Full access_Class";

    private final ITowerService service;

    @ApiResponses(value = {
            @ApiResponse(responseCode = "404", description = "When don't find any Tower"),
            @ApiResponse(responseCode = "200", description = "When found Tower",
            content = @Content(schema = @Schema(implementation = TowerDTO.class)))
    })
    @Operation(summary = "Get all Tower have status true by given Location Id")
    @GetMapping("location/{id}")
    @Secured({VIEW, MODIFY, CREATE, FULL_ACCESS})
    public ResponseEntity<?> listByLocationIdTrue(@Parameter(description = "Location ID want to get Tower") @PathVariable("id") Long id) {
        List<TowerDTO> list = service.listByTowerIdTrue(id);
        if (!list.isEmpty()) {
            return ResponseEntity.ok(list);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Don't find any Tower with Location ID = " + id);
        }
    }



    @Operation(summary = "Get all class's Locations(Towers) by TrainingClass id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "404", description = "No Such Value", content = @Content(schema = @Schema(defaultValue = "Training class id[-] not found!!!"))),
            @ApiResponse(responseCode = "200", description = "Return Sample", content = @Content(schema = @Schema(implementation = TowerDTO.class)))
    })
    @GetMapping("/class-towers")
    @Secured({VIEW, MODIFY, CREATE, FULL_ACCESS})
    public ResponseEntity<?> getAllTowers(@Parameter(description = "TrainingClass id", example = "1") @Param("id") Long id) {
        try{
            return ResponseEntity.ok(service.getAllTowersByTrainingClassId(id));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Training class id[" + id + "] not found!!!");
        }
    }



    @Operation(
            summary = "Get all class's Locations(Towers) for day-nth of total days of the class schedule",
            description = "Get list of Locations(Towers) in a date clicked in the class schedule table by the user"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "404", description = "No Such Value", content = @Content(schema = @Schema(defaultValue = "Day [-] of Training class id[-] not found!!!"))),
            @ApiResponse(responseCode = "200", description = "Return Sample", content = @Content(schema = @Schema(implementation = TowerDTO.class)))
    })
    @GetMapping("/class-towers-for-a-date")
    @Secured({VIEW, MODIFY, CREATE, FULL_ACCESS})
    public ResponseEntity<?> getAllTowersForADate(
            @Parameter(description = "TrainingClass id", example = "1") @Param("id") Long id,
            @Parameter(description = "day-nth of total days of the class schedule", example = "1") @Param("dayNth") int dayNth
    ) {
        try{
            return ResponseEntity.ok(service.getAllTowersForADateByTrainingClassId(id, dayNth));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Day [" + dayNth + "] of Training class id[" + id + "] not found!!!");
        }
    }
}
