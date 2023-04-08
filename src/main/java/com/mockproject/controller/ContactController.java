package com.mockproject.controller;

import com.mockproject.dto.ContactDTO;
import com.mockproject.service.interfaces.IContactService;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@Tag(name = "Contact API")
@RequestMapping("api/contact")
@SecurityRequirement(name = "Authorization")
public class ContactController {

    public static final String VIEW = "ROLE_View_Class";
    public static final String MODIFY = "ROLE_Modify_Class";
    public static final String CREATE = "ROLE_Create_Class";
    public static final String FULL_ACCESS = "ROLE_Full access_Class";

    private final IContactService service;

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "When get list Contact successfully",
                        content = @Content(schema = @Schema(implementation = ContactDTO.class))),
            @ApiResponse(responseCode = "404", description = "When don't find any contact!")
    })
    @Operation(summary = "Get all Contact have status = True")
    @GetMapping("")
    @Secured({VIEW, MODIFY, CREATE, FULL_ACCESS})
    public ResponseEntity<?> listAll(){
        List<ContactDTO> list = service.listAllTrue();
        if(!list.isEmpty()){
            return ResponseEntity.ok(list);
        }else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Don't find any Contact");
        }
    }



    @Operation(summary = "Get class's Contact by TrainingClass id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "404", description = "No Such Value", content = @Content(schema = @Schema(defaultValue = "Training class id[-] not found!!!"))),
            @ApiResponse(responseCode = "200", description = "Return Sample", content = @Content(schema = @Schema(implementation = ContactDTO.class)))
    })
    @GetMapping("/class-contact")
    @Secured({VIEW, MODIFY, CREATE, FULL_ACCESS})
    public ResponseEntity<?> getClassContact(@Parameter(description = "TrainingClass id", example = "1") @Param("id") Long id) {
        try{
            return ResponseEntity.ok(service.getContactByTrainingClassId(id));
        } catch (NullPointerException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Contact from training class id[" + id + "] disabled!!!");
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Training class id[" + id + "] not found!!!");
        }
    }
}
