package com.mockproject.controller;

import com.mockproject.dto.AttendeeDTO;
import com.mockproject.service.interfaces.IAttendeeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequiredArgsConstructor
@Tag(name = "Attendee API")
@RequestMapping("api/attendee")
@SecurityRequirement(name = "Authorization")
@Slf4j
public class AttendeeController {

    private final IAttendeeService attendeeService;

    @ApiResponses(value = {
            @ApiResponse(responseCode = "404", description = "When don't find any attendee"),
            @ApiResponse(responseCode = "200", description = "When found attendee",
                    content = @Content(schema = @Schema(implementation = AttendeeDTO.class)))
    })
    @Operation(summary = "Get All User have Status = True")
    @GetMapping("")
    public ResponseEntity<?> listAllTrue() {
        List<AttendeeDTO> list = attendeeService.listAllTrue();
        if (!list.isEmpty()) {
            return ResponseEntity.ok(attendeeService.listAllTrue());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Don't find any attendee");
        }
    }

    @GetMapping("/list")
    @Operation(
            summary = "Get attendee list"
    )
    public ResponseEntity<?> getAllAttendee() {
        return ResponseEntity.ok(attendeeService.getAllAttendee(true));
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Get attendee by ID"
    )
    public ResponseEntity<?> getAttendeeById(
            @PathVariable("id")
            @Parameter(
                    description = "<b>Insert ID to get attendee<b>",
                    example = "1"
            ) Long id) {
        return ResponseEntity.ok(attendeeService.getAttendeeById(true, id));
    }


    @Operation(summary = "Get class's Attendee type (Fresher,...) by TrainingClass id ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "404", description = "No Such Value", content = @Content(schema = @Schema(defaultValue = "Training class id[-] not found!!!"))),
            @ApiResponse(responseCode = "200", description = "Return Sample", content = @Content(schema = @Schema(implementation = AttendeeDTO.class)))
    })
    @GetMapping("/class-attendee")
    public ResponseEntity<?> getAttendeeName(@Parameter(description = "TrainingClass id", example = "1") @Param("id") Long id) {
        try {
            return ResponseEntity.ok(attendeeService.getAttendeeByTrainingClassId(id));
        } catch (NullPointerException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Attendee from training class id[" + id + "] disabled!!!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Training class id[" + id + "] not found!!!");
        }
    }

}

