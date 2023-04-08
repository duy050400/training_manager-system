package com.mockproject.controller;

import com.mockproject.dto.UnitDTO;
import com.mockproject.entity.CustomUserDetails;
import com.mockproject.entity.Unit;
import com.mockproject.service.interfaces.IUnitService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Tag(name = "Unit", description = "API realted Unit")
@RequestMapping(value = "/api/unit")
@SecurityRequirement(name = "Authorization")
@Slf4j
public class UnitController {
    public static final String VIEW = "ROLE_View_Syllabus";
    public static final String MODIFY = "ROLE_Modify_Syllabus";
    public static final String CREATE = "ROLE_Create_Syllabus";
    public static final String FULL_ACCESS = "ROLE_Full access_Syllabus";

    private final IUnitService unitService;

    @GetMapping("/{sessionId}")
    @Operation(summary = "Get all unit by session id")
    @Secured({VIEW, MODIFY, CREATE, FULL_ACCESS})
    public ResponseEntity<List<UnitDTO>> getAllUnitBySessionId(@PathVariable("sessionId") @NotNull Long sessionId){
        List<UnitDTO> listUnit = unitService.getAllUnitBySessionId(sessionId, true);
        return ResponseEntity.ok(listUnit);
    }

    @PostMapping("/create/{id}")
    @Operation(summary = "Create unit by session id")
    @Secured({CREATE, FULL_ACCESS})
    public ResponseEntity<Boolean> createUnit(@PathVariable("id") @Parameter(description = "Session id") @NotNull Long sessionId, @Valid @RequestBody List<UnitDTO> listUnit){
        CustomUserDetails user = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return ResponseEntity.ok(unitService.createUnit(sessionId, listUnit, user.getUser()));
    }

    @PutMapping("/edit")
    @Operation(summary = "Edit unit by UnitDTO")
    @Secured({MODIFY, FULL_ACCESS})
    public ResponseEntity<Unit> editUnit(@Valid @RequestBody UnitDTO unitDTO) throws IOException{
        Unit updateUnit = unitService.editUnit(unitDTO, true);
        return ResponseEntity.ok(updateUnit);
    }

    @PutMapping("delete/{id}")
    @Operation(summary = "Delete unit by unit id")
    @Secured({MODIFY, FULL_ACCESS})
    public ResponseEntity<Boolean> deleteUnit(@PathVariable("id") @Parameter(description = "Unit id") @NotNull Long unitId){
        return ResponseEntity.ok(unitService.deleteUnit(unitId, true));
    }

    @PutMapping("/multi-delete/{id}")
    @Operation(summary = "Delete multi units by sessionId")
    @Secured({MODIFY, FULL_ACCESS})
    public ResponseEntity<Boolean> deleteUnits(@PathVariable("id") @Parameter(description = "Session id") @NotNull Long sessionId){
        return ResponseEntity.ok(unitService.deleteUnits(sessionId, true));
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "404", description = "When don't find any Unit"),
            @ApiResponse(responseCode = "200", description = "When found Unit",
            content = @Content(schema = @Schema(implementation = UnitDTO.class)))
    })
    @Operation(summary = "Get All Unit by given Session ID")
    @GetMapping("/list-by-session/{sid}")
    @Secured({VIEW, MODIFY, CREATE, FULL_ACCESS})
    public ResponseEntity<?> listBySessionId(@Parameter(description = "Session ID") @PathVariable("sid") Long sid) {
        List<UnitDTO> list = unitService.listBySessionId(sid);
        if (!list.isEmpty()) {
            return ResponseEntity.ok(list);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Don't find any Unit!");
        }
    }

    @Operation(
            summary = "Get all class Units for day-nth of total days of the class schedule",
            description = "Get list of Units in a date clicked in the class schedule table by the user"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "404", description = "No Such Value", content = @Content(schema = @Schema(defaultValue = "Day [-] of Training class id[-] not found!!!"))),
            @ApiResponse(responseCode = "200", description = "Return Sample", content = @Content(schema = @Schema(implementation = UnitDTO.class)))
    })
    @GetMapping("/class-units-for-a-date")
    @Secured({VIEW, MODIFY, CREATE, FULL_ACCESS})
    public ResponseEntity<?> getAllUnitsForADate(
            @Parameter(description = "TrainingClass id", example = "1") @Param("id") Long id,
            @Parameter(description = "day-nth of total days of the class schedule", example = "1") @Param("dayNth") int dayNth
    ) {
        try{
            return ResponseEntity.ok(unitService.getAllUnitsForADateByTrainingClassId(id, dayNth));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Day [" + dayNth + "] of Training class id[" + id + "] not found!!!");
        }
    }
}
