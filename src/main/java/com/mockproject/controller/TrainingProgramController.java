package com.mockproject.controller;

import com.mockproject.dto.ReadFileDto;
import com.mockproject.dto.SearchTPDTO;
import com.mockproject.dto.TrainingProgramAddDto;
import com.mockproject.dto.TrainingProgramDTO;
import com.mockproject.entity.TrainingProgram;
import com.mockproject.service.interfaces.IFileService;
import com.mockproject.service.interfaces.ITrainingProgramService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Tag(name = "Training Program API")
@RequestMapping("api/training-program")
@SecurityRequirement(name = "Authorization")
@Slf4j
public class TrainingProgramController {

    public static final String VIEW = "ROLE_View_Training program";
    public static final String MODIFY = "ROLE_Modify_Training program";
    public static final String CREATE = "ROLE_Create_Training program";
    public static final String FULL_ACCESS = "ROLE_Full access_Training program";

    private final ITrainingProgramService trainingProgramService;
    private final IFileService fileService;
    private final ResourceLoader resourceLoader;

    @ApiResponses(value = {
            @ApiResponse(responseCode = "404", description = "When don't find any Training Program"),
            @ApiResponse(responseCode = "200", description = "When find training program and return list program",
                    content = @Content(schema = @Schema(implementation = TrainingProgramDTO.class)))
    })
    @Operation(summary = "Get all Training Program")
    @GetMapping("/list")
    @Secured({VIEW, MODIFY, CREATE, FULL_ACCESS})
    public ResponseEntity<?> getAllTrainingProgram(@RequestParam(defaultValue = "0") Integer pageNo,
                                                   @RequestParam(defaultValue = "10") Integer pageSize) {
        Page<TrainingProgramDTO> list = trainingProgramService.getAll(pageNo, pageSize);
        if (!list.isEmpty()) {
            return ResponseEntity.ok(list);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Don't find any Training Program!");
        }
    }
//    @PostMapping("/search-training-program-by-keywords")
//    @Secured({VIEW, MODIFY, CREATE, FULL_ACCESS})
//    public ResponseEntity<?> searchTrainingProgramByKeyWords(@RequestBody SearchTPDTO searchList) {
//            List<TrainingProgramDTO> list = trainingProgramService.searchByNameOrCreator(searchList);
//
//        if (!list.isEmpty()) {
//            return ResponseEntity.ok(list);
//        } else {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Don't find any Training Program!");
//        }
//    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "404", description = "When don't find any Training Program"),
            @ApiResponse(responseCode = "200", description = "When find training program and return list program",
                    content = @Content(schema = @Schema(implementation = TrainingProgramDTO.class)))
    })
    @Operation(summary = "Get Training Program by searching name")
    @GetMapping("/search-name")
    @Secured({VIEW, MODIFY, CREATE, FULL_ACCESS})
    public ResponseEntity<?> searchByName(@Parameter(description = "Training Program Name want to search") @RequestParam(defaultValue = "") String name) {
        List<TrainingProgramDTO> list = trainingProgramService.searchByName(name);
//        List<TrainingProgramDTO> list = trainingProgramService.searchByNameOrCreator(search);
        if (!list.isEmpty()) {
            return ResponseEntity.ok(list);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Don't find any Training Program!");
        }
    }

    @PostMapping(value = "/uploadCsv", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Secured({CREATE, FULL_ACCESS})
    public ResponseEntity readFileCsv(@Valid @ModelAttribute ReadFileDto readFileDto) {
        MultipartFile file = readFileDto.getFile();
        List<TrainingProgram> trainingProgramList = new ArrayList<>();
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("File is empty");
        } else {
            String fileName = file.getOriginalFilename();
            if (!fileName.split("\\.")[1].equals("csv")) {
                return ResponseEntity.badRequest().body("File is not csv");
            } else {
                trainingProgramService.addFromFileCsv(file, readFileDto);
            }

        }
        return ResponseEntity.ok().body("uploadFile Success");

    }

    @GetMapping("/downloadFile/csv")
    @Secured({CREATE, FULL_ACCESS})
    public ResponseEntity<?> downLoadFileExample() {
        Resource fileResource = resourceLoader.getResource("classpath:file-format/TrainingProgram.csv");
        try {
            InputStream inputStream = fileResource.getInputStream();
            byte[] buffer = inputStream.readAllBytes();

            ByteArrayResource resource = new ByteArrayResource(buffer);
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=TrainingProgram.csv");
            return ResponseEntity.ok()
                    .headers(headers)
                    .contentLength(buffer.length)
                    .contentType(MediaType.parseMediaType("application/csv"))
                    .body(resource);
        }catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @PostMapping("/saveTrainingProgram")
    @Secured({MODIFY, CREATE, FULL_ACCESS})
    public ResponseEntity<?> saveTrain(@Valid @RequestBody TrainingProgramAddDto trainingProgramDTO) {
        trainingProgramService.save(trainingProgramDTO, null, null);
        return ResponseEntity.ok("Add training program successfully");
    }

    @GetMapping("/{id}")
    @Secured({VIEW, MODIFY, CREATE, FULL_ACCESS})
    @Operation(summary = "Get training program by ID")
    public ResponseEntity<TrainingProgramDTO> getTrainingProgramByID(@PathVariable("id") Long id) {
        return ResponseEntity.ok(trainingProgramService.getTrainingProgramById(id));
    }

    @PutMapping("/de-active-training-program/{trainingProgramID}")
    @Secured({MODIFY, FULL_ACCESS})
    public ResponseEntity<?> de_activeTrainingProgramByID(@PathVariable Long trainingProgramID) {
        if (trainingProgramService.de_activeTrainingProgram(trainingProgramID)) {
            return ResponseEntity.ok("De-active training program successfully.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Training program not found");
        }
    }

    @PutMapping("/active-training-program/{trainingProgramID}")
    @Secured({MODIFY, FULL_ACCESS})
    public ResponseEntity<?> activeTrainingProgramByID(@PathVariable Long trainingProgramID) {
        if (trainingProgramService.activeTrainingProgram(trainingProgramID)) {
            return ResponseEntity.ok("Active training program successfully.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Training program not found");
        }
    }

    @PutMapping("/delete-training-program/{trainingProgramID}")
    @Secured({MODIFY, FULL_ACCESS})
    public ResponseEntity<?> deleteTrainingProgramByID(@PathVariable Long trainingProgramID) {
        if (trainingProgramService.deleteTrainingProgram(trainingProgramID)) {
            return ResponseEntity.ok("Delete training program successfully.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Training program not found");
        }
    }

    @PutMapping("/restore-status-training-program")
    @Secured({MODIFY, FULL_ACCESS})
    public ResponseEntity<?> restoreAllTrainingPrograms() {
        trainingProgramService.restoreAllTrainingPrograms();
        return ResponseEntity.ok("Restore all training programs successfully.");
    }

    @PostMapping("/search-training-program-by-keywords")
    @Secured({VIEW, MODIFY, CREATE, FULL_ACCESS})
    public ResponseEntity<?> searchTrainingProgramByKeyWords(@RequestParam(defaultValue = "0") Integer pageNo,
                                                             @RequestParam(defaultValue = "10") Integer pageSize,
                                                             @RequestBody SearchTPDTO searchList) {
        if(searchList.getSearch() == null || searchList.getSearch().size() == 0){
            List<String> searchText = new ArrayList<>();
            searchText.add("");
            searchList.setSearch( searchText);
        }
        Page<TrainingProgramDTO> list = trainingProgramService.searchByNameOrCreator(searchList,pageNo,pageSize);
        if (!list.isEmpty()) {
            return ResponseEntity.ok(list);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Don't find any Training Program!");
        }
    }

    @PostMapping("/duplicate-training-program/{trainingProgramID}")
    public ResponseEntity<?> duplicateTrainingProgram(@PathVariable Long trainingProgramID) {
        if (trainingProgramService.duplicateProgram(trainingProgramID)) {
            return ResponseEntity.ok("Duplicate training program successfully");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Don't find any Training Program!");
        }
    }

    @PutMapping("/edit-training-program/{trainingProgramID}/{newTrainingProgramName}")
    public ResponseEntity<?> editTrainingProgram(@PathVariable Long trainingProgramID, @PathVariable String newTrainingProgramName) {

        if (trainingProgramService.editProgram(trainingProgramID, newTrainingProgramName).isPresent()) {
            return ResponseEntity.ok("Edit training program successfully");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Don't find any Training Program!");
        }
    }
}
