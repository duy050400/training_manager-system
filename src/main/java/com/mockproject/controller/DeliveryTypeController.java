package com.mockproject.controller;

import com.mockproject.dto.DeliveryTypeDTO;
import com.mockproject.service.interfaces.IDeliveryTypeService;
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

@RestController
@Tag(name = "Delivery API")
@RequestMapping(value = "/api/delivery")
@Tag(name = "Delivery type", description = "API related delivery type")
@SecurityRequirement(name = "Authorization")
@RequiredArgsConstructor
@SecurityRequirement(name = "Authorization")
public class DeliveryTypeController {

    public static final String VIEW = "ROLE_View_Syllabus";
    public static final String MODIFY = "ROLE_Modify_Syllabus";
    public static final String CREATE = "ROLE_Create_Syllabus";
    public static final String FULL_ACCESS = "ROLE_Full access_Syllabus";

    private final IDeliveryTypeService deliveryTypeService;

    @GetMapping("")
    @Operation(summary = "Get all delivery type")
    @Secured({VIEW, MODIFY, CREATE, FULL_ACCESS})
    public ResponseEntity<List<DeliveryTypeDTO>> getAll(){
        List<DeliveryTypeDTO> deliveryTypeDTOList = deliveryTypeService.getDeliveryTypes(true);
        return ResponseEntity.ok(deliveryTypeDTOList);
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "404", description = "When don't find any delivery type"),
            @ApiResponse(responseCode = "200", description = "When found delivery type",
            content = @Content(schema = @Schema(implementation = DeliveryTypeDTO.class)))
    })
    @Operation(summary = "Get Delivery Type by given ID")
    @GetMapping("{id}")
    @Secured({VIEW, MODIFY, CREATE, FULL_ACCESS})
    public ResponseEntity<?> getById(@Parameter(description = "Delivery Type ID") @PathVariable("id") Long id) {
        DeliveryTypeDTO deliveryTypeDTO = deliveryTypeService.getDeliveryType(id, true);
        if (deliveryTypeDTO != null) {
            return ResponseEntity.ok(deliveryTypeDTO);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Don't find any Delivery Type have ID = " + id);
        }
    }

    @Operation(summary = "Get all class's DeliveryTypes by TrainingClass id ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "404", description = "No Such Value", content = @Content(schema = @Schema(defaultValue = "Training class id[-] not found!!!"))),
            @ApiResponse(responseCode = "200", description = "Return Sample", content = @Content(schema = @Schema(implementation = DeliveryTypeDTO.class)))
    })
    @GetMapping("/class-delivery-types")
    @Secured({VIEW, MODIFY, CREATE, FULL_ACCESS})
    public ResponseEntity<?> getAllDeliveryTypes(@Parameter(description = "TrainingClass id", example = "1") @Param("id") Long id) {
        try{
            return ResponseEntity.ok(deliveryTypeService.getAllDeliveryTypesByTrainingClassId(id));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Training class id[" + id + "] not found!!!");
        }
    }

}
