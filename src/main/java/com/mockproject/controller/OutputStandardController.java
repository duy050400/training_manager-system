package com.mockproject.controller;

import com.mockproject.dto.OutputStandardDTO;
import com.mockproject.service.interfaces.IOutputStandardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Tag(name = "Output Standard", description = "API related Output standard")
@RequestMapping(value = "/api/osd")
@SecurityRequirement(name = "Authorization")
public class OutputStandardController {

    public static final String VIEW = "ROLE_View_Syllabus";
    public static final String MODIFY = "ROLE_Modify_Syllabus";
    public static final String CREATE = "ROLE_Create_Syllabus";
    public static final String FULL_ACCESS = "ROLE_Full access_Syllabus";

    private final IOutputStandardService outputStandardService;

    @GetMapping("/syllabus/{syllabusId}")
    @Operation(
            summary = "Get output standard by syllabus ID"
    )
    @Secured({VIEW, MODIFY, CREATE, FULL_ACCESS})
    public ResponseEntity<?> getOsdBySyllabusId(
            @PathVariable("syllabusId")
            @Parameter(
                    description = "<b>Insert syllabus ID to get output standard<b>",
                    example = "7"
            ) long id) {
        return ResponseEntity.ok(outputStandardService.getOsdBySyllabusId(true, id));
    }

    @GetMapping("/{outputStandardId}")
    @Operation(summary = "Get output standard by output standard id")
    @Secured({VIEW, MODIFY, CREATE, FULL_ACCESS})
    public ResponseEntity<OutputStandardDTO> getOutputStandardById(@PathVariable("outputStandardId") Long id){
        OutputStandardDTO outputStandardDTO = outputStandardService.getOutputStandardById(id, true);
        return ResponseEntity.ok(outputStandardDTO);
    }

    @GetMapping("")
    @Operation(summary = "Get all output standard")
    @Secured({VIEW, MODIFY, CREATE, FULL_ACCESS})
    public ResponseEntity<List<OutputStandardDTO>> getAll(){
        List<OutputStandardDTO> outputStandardDTOList = outputStandardService.getOutputStandard(true);
        return ResponseEntity.ok(outputStandardDTOList);
    }
}
