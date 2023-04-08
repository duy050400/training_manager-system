package com.mockproject.controller;

import com.mockproject.dto.TrainingMaterialDTO;
import com.mockproject.entity.CustomUserDetails;
import com.mockproject.service.interfaces.ITrainingMaterialService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.zip.DataFormatException;

@RestController
@RequestMapping(value = "/api/training-material")
@Tag(name = "Training material", description = "API realted Training material")
@RequiredArgsConstructor
@SecurityRequirement(name = "Authorization")
@Slf4j
public class TrainingMaterialController {

    public static final String VIEW = "ROLE_View_Learning material";
    public static final String MODIFY = "ROLE_Modify_Learning material";
    public static final String CREATE = "ROLE_Create_Learning material";
    public static final String FULL_ACCESS = "ROLE_Full access_Learning material";

    private final ITrainingMaterialService trainingMaterialService;

    @GetMapping("get-all/{unitDetailId}")
    @Operation(summary = "Get files training material by unit detail id")
    @Secured({VIEW, MODIFY, CREATE, FULL_ACCESS})
    public ResponseEntity<List<TrainingMaterialDTO>> getFiles(@PathVariable("unitDetailId") @NotNull Long unitDetailId){
        return ResponseEntity.ok(trainingMaterialService.getFiles(unitDetailId, true));
    }

    @PostMapping("/upload-file/{unitDetailID}")
    @Operation(summary = "Upload file by unit detail id")
    @Secured({CREATE, FULL_ACCESS})
    public ResponseEntity<List<TrainingMaterialDTO>> uploadFile(@PathVariable("unitDetailID") @NotBlank Long unitDetailID,@Valid @RequestBody List<TrainingMaterialDTO> trainingMaterialDTOList){
        CustomUserDetails user = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        return ResponseEntity.ok(trainingMaterialService.uploadFile(trainingMaterialDTOList,
                user.getUser(), unitDetailID));
    }

    @GetMapping("/get-file/{id}")
    @Operation(summary = "Get file by Training material id")
    @Secured({VIEW, MODIFY, CREATE, FULL_ACCESS})
    public ResponseEntity<TrainingMaterialDTO> getFile(@PathVariable("id") @Parameter(description = "Traning material id") @NotNull Long id) throws DataFormatException, IOException {
        TrainingMaterialDTO trainingMaterialDTO = trainingMaterialService.getFile(id, true);
        return ResponseEntity.ok()
               // .contentType(MediaType.valueOf(trainingMaterialDTO.getType()))
                .body(trainingMaterialDTO);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update file by training material id")
    @Secured({MODIFY, FULL_ACCESS})
    public ResponseEntity<TrainingMaterialDTO> updateFile(@PathVariable("id") @Parameter(description = "Training material id") @NotBlank Long id,
            @Valid @RequestBody TrainingMaterialDTO dto
            ) throws IOException {
        CustomUserDetails user = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return ResponseEntity.ok(trainingMaterialService.updateFile(id, dto, user.getUser(), true));
    }

    @PutMapping("/delete/{id}")
    @Operation(summary = "Delete file by training material id")
    @Secured({MODIFY, FULL_ACCESS})
    public ResponseEntity<Boolean> deleteFile(@PathVariable("id") @Parameter(description = "Training material id") @NotNull Long trainingMaterialId){
        return ResponseEntity.ok(trainingMaterialService.deleteTrainingMaterial(trainingMaterialId, true));
    }

    @PutMapping("/multi-delete/{id}")
    @Operation(summary = "Delete file by Unit detail id")
    @Secured({MODIFY, FULL_ACCESS})
    public ResponseEntity<Boolean> deleteFiles(@PathVariable("id") @Parameter(description = "Unit detail id") @NotBlank Long unitDetailId){
        return ResponseEntity.ok(trainingMaterialService.deleteTrainingMaterials(unitDetailId, true));
    }
}

