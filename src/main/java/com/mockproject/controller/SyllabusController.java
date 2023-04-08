package com.mockproject.controller;

import com.mockproject.dto.SyllabusDTO;
import com.mockproject.dto.TrainingProgramSyllabusDTO;
import com.mockproject.entity.CustomUserDetails;
import com.mockproject.entity.Syllabus;
import com.mockproject.service.interfaces.ISyllabusService;
import com.mockproject.service.interfaces.ITrainingProgramSyllabusService;
import com.mockproject.utils.CSVUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@Tag(name = "Syllabus API")
@RequestMapping(value = "/api/syllabus")
@SecurityRequirement(name = "Authorization")
@Slf4j
public class SyllabusController {

    public static final String VIEW = "ROLE_View_Syllabus";
    public static final String MODIFY = "ROLE_Modify_Syllabus";
    public static final String CREATE = "ROLE_Create_Syllabus";
    public static final String FULL_ACCESS = "ROLE_Full access_Syllabus";

    private final ISyllabusService syllabusService;

    private final ITrainingProgramSyllabusService trainingProgramSyllabusService;

    private final ResourceLoader resourceLoader;


    @GetMapping("/getSyllabusByTrainingProgram/{trainingProgramId}")
    @Operation(summary = "Get all syllabus by training program id")
    @Secured({VIEW, MODIFY, CREATE, FULL_ACCESS})
    public ResponseEntity<List<TrainingProgramSyllabusDTO>> getAllTrainingProgramSyllabus(@PathVariable("trainingProgramId") long id) {
        List<TrainingProgramSyllabusDTO> list = trainingProgramSyllabusService.getAllSyllabusByTrainingProgramId(id, true);
        return ResponseEntity.ok(list);
    }

    @GetMapping("/{syllabusId}")
    @Operation(summary = "Get syllabus by syllabus id")
    @Secured({VIEW, MODIFY, CREATE, FULL_ACCESS})
    public ResponseEntity<SyllabusDTO> getSyllabus(@PathVariable("syllabusId") @NotBlank Long syllabusId){
        SyllabusDTO syllabus = syllabusService.getSyllabusById(syllabusId, true, true);
        return ResponseEntity.ok(syllabus);
    }

    @PutMapping("deactivate/{id}")
    @Operation(summary = "De-active syllabus by syllabus id")
    @Secured({MODIFY, FULL_ACCESS})
    public ResponseEntity<Boolean> deactive(@PathVariable("id") @NotBlank Long syllabusId){
        return ResponseEntity.ok(syllabusService.deactivate(syllabusId, true));
    }

    @PostMapping(value = "/replace")
    @Operation(summary = "Replace Syllabus")
    @Secured({CREATE,FULL_ACCESS})
    public ResponseEntity<Boolean> replace(@RequestBody SyllabusDTO syllabusDTO){
        return ResponseEntity.ok(syllabusService.replace(syllabusDTO, true));
    }

    @PostMapping(value = "/create")
    @Operation(summary = "Create Syllabus")
    @Secured({CREATE,FULL_ACCESS})
    public ResponseEntity<Long> create(@Valid @RequestBody SyllabusDTO syllabus){
        CustomUserDetails user = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long syllabusID = syllabusService.create(syllabus, user.getUser());
        return ResponseEntity.ok(syllabusID);
    }

    @PutMapping("edit")
    @Operation(summary = "Edit syllabus by SyllabusDTO")
    @Secured({MODIFY, FULL_ACCESS})
    public ResponseEntity<Syllabus> editSyllabus(@Valid @RequestBody SyllabusDTO syllabusDTO) throws IOException {
        Syllabus editsyllabus = syllabusService.editSyllabus(syllabusDTO, true);
        return ResponseEntity.ok(editsyllabus);
    }

    @PostMapping("duplicate/{id}")
    @Operation(summary = "Duplicate syllabus by SyllabusID")
    @Secured({CREATE, FULL_ACCESS})
    public ResponseEntity<Long> duplicateSyllabus(@PathVariable("id") @Parameter(description = "Syllabus id") @NotNull Long syllabusId){
        return ResponseEntity.ok(syllabusService.duplicateSyllabus(syllabusId, true, true));
    }

    @PutMapping("delete/{id}")
    @Operation(summary = "Delete syllabus by syllabusId")
    @Secured({MODIFY, FULL_ACCESS})
    public ResponseEntity<Boolean> deleteSyllabus(@PathVariable("id") @Parameter(description = "Syllabus id") @NotNull Long syllabusId){
        return ResponseEntity.ok(syllabusService.deleteSyllabus(syllabusId, true));
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "404", description = "When don't find any syllabus"),
            @ApiResponse(responseCode = "200", description = "When found list of syllabus",
                    content = @Content(schema = @Schema(implementation = SyllabusDTO.class)))
    })
    @Operation(summary = "Get all Syllabus by given Training Program ID")
    @GetMapping("/list-by-training-program/{id}")
    @Secured({VIEW, MODIFY, CREATE, FULL_ACCESS})
    public ResponseEntity<?> listSyllabusByTrainingProgramId(@Parameter(description = "Training Class's ID that want to get Syllabus")
                                                             @PathVariable("id") Long id) {
        List<SyllabusDTO> list = syllabusService.listByTrainingProgramIdTrue(id);
        if (!list.isEmpty()) {
            return ResponseEntity.ok(list);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Don't find any syllabus with Training Program = " + id);
        }
    }

    @GetMapping("/list")
    @Operation(
            summary = "Get syllabus list",
            description = "<b>List of syllabus according to search, sort, filter, and pages<b>"
    )
    @Secured({VIEW, MODIFY, CREATE, FULL_ACCESS})
    public ResponseEntity<?> getListSyllabus(
            @RequestParam(defaultValue = "")
            @Parameter(
                    description = "<b>Created date - Start day (yyyy-mm-dd)<b>",
                    example = "2000-03-13"
            )
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate fromDate,

            @RequestParam(defaultValue = "")
            @Parameter(
                    description = "<b>Created date - End day (yyyy-mm-dd)<b>",
                    example = "3000-03-13"
            )
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate toDate,

            @RequestParam(defaultValue = "")
            @Parameter(
                    description = "<b>Search by syllabus name, code, creator's name, or output standard<b>",
                    example = ""
            )
            List<String> search,

            @RequestParam(defaultValue = "0")
            @Parameter(
                    description = "<b>Insert page number (0 => first page)<b>",
                    example = "0"
            )
            Optional<Integer> page,

            @RequestParam(defaultValue = "10")
            @Parameter(
                    description = "<b>Insert number of rows (10 => 10 rows per page)<b>",
                    example = "10"
            ) Optional<Integer> row,

            @RequestParam(defaultValue = "name,asc")
            @Parameter(
                    description = "<b>Sort by attribute descending/ascending"
                            + "<li>dateCreated,asc => sort by dateCreated ascending</li>"
                            + "<li>creator,desc => sort by creator's name descending</li></u><b>",
                    example = "name,asc"
            )
            String[] sort) {
        return ResponseEntity
                .ok(syllabusService.getListSyllabus(true,  fromDate, toDate, search, sort, page, row));
    }


    @PostMapping(path = "read-file",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Read file")
    @Secured({CREATE, FULL_ACCESS})
    public ResponseEntity readSyllabusCsv(@RequestPart("file")MultipartFile file,
                                                       @Parameter(description = """
                                                               1. Name
                                                               2. Code
                                                               3. Name and Code""") int condition,
                                                       @Parameter(description = """
                                                               1. Allow
                                                               2. Replace
                                                               3. Skip""") int handle) throws IOException {

        if(CSVUtils.hasCSVFormat(file)){
            return ResponseEntity.ok(syllabusService.readFileCsv(file, condition, handle));
        }
        return ResponseEntity.badRequest().body("Please upload a csv file");
    }

    @GetMapping("get-template-file")
    @Operation(summary = "Download file")
    @Secured({CREATE, FULL_ACCESS})
    public ResponseEntity<?> getTemplateFile() throws IOException {

        Resource fileResource = resourceLoader.getResource("classpath:" + "file-format/syllabus-template.csv");
        InputStream inputStream = fileResource.getInputStream();
        byte[] buffer = inputStream.readAllBytes();

        ByteArrayResource resource = new ByteArrayResource(buffer);
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=Syllabus_import.csv");
        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(buffer.length)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
    }

    @GetMapping("/live-search")
    @Secured({VIEW, MODIFY, CREATE, FULL_ACCESS})
    public ResponseEntity<?> liveSearch() {
        var check = syllabusService.liveSearch();
        if (check != null) {
            return ResponseEntity.ok(check);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Syllabus not found");
    }
}
