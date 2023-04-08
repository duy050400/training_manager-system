package com.mockproject.controller;

import com.mockproject.dto.ClassScheduleDTO;
import com.mockproject.dto.SearchByDTO;
import com.mockproject.dto.TrainingClassFilterRequestDTO;
import com.mockproject.service.interfaces.IClassScheduleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Tag(name = "Class Schedule API")
@RequestMapping("/api/class-schedule")
@SecurityRequirement(name = "Authorization")
@Slf4j
public class ClassScheduleController {

    private final IClassScheduleService classScheduleService;
    public static final String VIEW = "ROLE_View_Class";
    public static final String MODIFY = "ROLE_Modify_Class";
    public static final String CREATE = "ROLE_Create_Class";
    public static final String FULL_ACCESS = "ROLE_Full access_Class";

    @PostMapping("/day")
    @Secured({VIEW, MODIFY, CREATE, FULL_ACCESS})
    @Operation(summary = "Get training class for the typical day")
    public ResponseEntity getTrainingClassByDay(@RequestBody TrainingClassFilterRequestDTO filterRequestDTO) {
        var trainingClass = classScheduleService.getTrainingClassByDay(filterRequestDTO);
        if (trainingClass.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Don't have any training class");
        } else {
            return ResponseEntity.ok(trainingClass);
        }
    }

    @PostMapping("/week")
    @Secured({VIEW, MODIFY, CREATE, FULL_ACCESS, CREATE})
    @Operation(summary = "Get a class schedule for a week")
    public ResponseEntity getTrainingClassByWeek(@RequestBody TrainingClassFilterRequestDTO filterRequestDTO) {
        var trainingClass = classScheduleService.getTrainingClassByWeek(filterRequestDTO);
        if (trainingClass.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Don't have any training class");
        } else {
            System.out.println(trainingClass);
            return ResponseEntity.ok(trainingClass);
        }
    }

    @PostMapping("/search/day")
    @Secured({VIEW, MODIFY, CREATE, FULL_ACCESS, CREATE})
    @Operation(summary = "Search training class in day by text")
    public ResponseEntity searchTrainingClassInDay(@RequestBody @Valid SearchByDTO searchByDTO) {
        var trainingClass = classScheduleService.searchTrainingClassInDate(searchByDTO.getSearchText(), searchByDTO.getNowDate());
        if (trainingClass.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Don't have any training class");
        } else {
            return ResponseEntity.ok(trainingClass);
        }
    }

    @PostMapping("/search/week")
    @Secured({VIEW, MODIFY, CREATE, FULL_ACCESS})
    @Operation(summary = "Search training class in week by text")
    public ResponseEntity searchTrainingClassInWeek(@RequestBody @Valid SearchByDTO searchByDTO) {
        var trainingClass = classScheduleService.searchTrainingClassInWeek(searchByDTO.getSearchText(), searchByDTO.getStartDate(), searchByDTO.getEndDate());
        if (trainingClass.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Don't have any training class");
        } else {
            return ResponseEntity.ok(trainingClass);
        }
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "When list of ClassShedule have been saved success!"),
            @ApiResponse(responseCode = "400", description = "When Saving fail!")
    })
    @Operation(summary = "Save list of ClassSchedule by given Training Class")
    @PostMapping("list/training-class/{tcId}")
    @Secured({MODIFY, CREATE, FULL_ACCESS})
    public ResponseEntity<?> createListSchedule(@io.swagger.v3.oas.annotations.parameters.RequestBody(
            content = @Content(examples = @ExampleObject(value = "[\"2023-03-14\",\"2023-03-15\"]")))
                                                @Valid @RequestBody List<LocalDate> listDate,
                                                @Parameter(description = "Training Class ID when call create Training Class API return")
                                                @PathVariable("tcId") Long tcId) {
        if (classScheduleService.saveClassScheduleForTrainingClass(listDate, tcId)) {
            return new ResponseEntity<>("List of Date have been saved!", HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>("Saving Fail!", HttpStatus.BAD_REQUEST);
        }
    }


    @Operation(summary = "Get class schedule by TrainingClass id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "404", description = "No Such Value", content = @Content(schema = @Schema(defaultValue = "Training class id[-] not found!!!"))),
            @ApiResponse(responseCode = "200", description = "Return Sample", content = @Content(schema = @Schema(implementation = ClassScheduleDTO.class)))
    })
    @GetMapping("/in-class")
    @Secured({VIEW, MODIFY, CREATE, FULL_ACCESS})
    public ResponseEntity<?> getClassSchedule(@Parameter(description = "TrainingClass id", example = "1") @Param("id") Long id) {
        try {
            return ResponseEntity.ok(classScheduleService.getClassScheduleByTrainingClassId(id));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Training class id[" + id + "] not found!!!");
        }
    }
}
