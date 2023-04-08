package com.mockproject.controller;

import com.mockproject.dto.UnitDetailDTO;
import com.mockproject.entity.CustomUserDetails;
import com.mockproject.entity.UnitDetail;
import com.mockproject.service.interfaces.IUnitDetailService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Tag(name = "Unit Detail", description = "API related Unit detail")
@SecurityRequirement(name = "Authorization")
@Slf4j
@RequestMapping(value = "/api/unit-detail")
public class UnitDetailController {

    public static final String VIEW = "ROLE_View_Syllabus";
    public static final String MODIFY = "ROLE_Modify_Syllabus";
    public static final String CREATE = "ROLE_Create_Syllabus";
    public static final String FULL_ACCESS = "ROLE_Full access_Syllabus";

    private final IUnitDetailService unitDetailService;

    @GetMapping("/{unitId}")
    @Operation(summary = "Get all unit detail by unit id")
    @Secured({VIEW, MODIFY, CREATE, FULL_ACCESS})
    public ResponseEntity<List<UnitDetailDTO>> getAllUnitDetailByUnitId(@PathVariable("unitId") @NotNull Long unitId){
        List<UnitDetailDTO> listUnitDetail = unitDetailService.getAllUnitDetailByUnitId(unitId, true);
        return ResponseEntity.ok(listUnitDetail);
    }

    @PostMapping("/create/{id}")
    @Operation(summary = "Create unit detail")
    @Secured({CREATE, FULL_ACCESS})
    public ResponseEntity<Boolean> createUnitDetail(@PathVariable("id") @Parameter(description = "Unit id") @NotNull long unitId, @Valid @RequestBody List<UnitDetailDTO> listUnitDetail){
        CustomUserDetails user = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return ResponseEntity.ok(unitDetailService.createUnitDetail(unitId, listUnitDetail, user.getUser()));
    }

    @PutMapping("/edit")
    @Operation(summary = "Edit unit detail")
    @Secured({MODIFY, FULL_ACCESS})
    public ResponseEntity<UnitDetail> editUnitDetail(@Valid @RequestBody UnitDetailDTO unitDetailDTO) throws IOException {
        UnitDetail updateUnitDetail = unitDetailService.editUnitDetail(unitDetailDTO, true);
        return ResponseEntity.ok(updateUnitDetail);
    }

    @PutMapping("/delete/{id}")
    @Operation(summary = "Delete unit detail")
    @Secured({MODIFY, FULL_ACCESS})
    public ResponseEntity<Boolean> deleteUnitDetail(@PathVariable("id") @Parameter(description = "Unit detail id") @NotNull Long unitDetailId){
        return ResponseEntity.ok(unitDetailService.deleteUnitDetail(unitDetailId, true));
    }

    @PutMapping("/multi-delete/{id}")
    @Operation(summary = "Delete list unit detail by unit id")
    @Secured({MODIFY, FULL_ACCESS})
    public ResponseEntity<Boolean> deleteUnitDetails(@PathVariable("id") @Parameter(description = "Unit id") @NotNull Long unitId){
        return ResponseEntity.ok(unitDetailService.deleteUnitDetails(unitId, true));
    }

    @PutMapping("/toggle/{id}")
    @Operation(summary = "Change state on/off of unit detail  by unit detail by id")
    @Secured({MODIFY, FULL_ACCESS})
    public ResponseEntity<Boolean> toggleUnitDetailType(@PathVariable("id") @Parameter(description = "Unit detail id") @NotNull long unitDetailId){
        return ResponseEntity.ok(unitDetailService.toggleUnitDetailType(unitDetailId, true));
    }
}
