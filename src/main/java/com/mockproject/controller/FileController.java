package com.mockproject.controller;

import com.mockproject.dto.FileClassResponseDTO;
import com.mockproject.exception.file.FileFormatException;
import com.mockproject.service.interfaces.IFileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

@RequiredArgsConstructor
@RestController
@Tag(name = "File API")
@RequestMapping("api/file-csv")
@SecurityRequirement(name = "Authorization")
public class FileController {

    public static final String VIEW = "ROLE_View_Class";
    public static final String MODIFY = "ROLE_Modify_Class";
    public static final String CREATE = "ROLE_Create_Class";
    public static final String FULL_ACCESS = "ROLE_Full access_Class";

    private final IFileService service;

    private final ResourceLoader resourceLoader;

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "When read successful and return object",
                    content = @Content(schema = @Schema(implementation = FileClassResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "When can't read the file - Check file  data! or Date not match with format"),
            @ApiResponse(responseCode = "404", description = "When data of entity can not be found - not exist!"),
            @ApiResponse(responseCode = "415", description = "When file doesn't match format - Required .csv file")
    })
    @Operation(summary = "Read create-class-format.csv then return FileResponse Object")
    @PostMapping("")
    @Secured({CREATE, FULL_ACCESS})
    public ResponseEntity<?> readFile(@RequestParam("file") MultipartFile file) throws IOException {
        String fileName = file.getOriginalFilename();
        String fileFormat = fileName.split("\\.")[1];
        if(!fileFormat.equals("csv")){
            throw new FileFormatException("File doesn't .csv format!");
        }
        return ResponseEntity.ok(service.readFileCsv(file));
    }

    @Operation(summary = "A API like a link when access will download Create class Format File")
    @GetMapping("/download/class-format")
    @Secured({CREATE, FULL_ACCESS})
    public ResponseEntity<?> downClassFormatFile() throws IOException {
//        Path path = Paths.get("file-format", "create-class-format.csv");
//        byte[] buffer = Files.readAllBytes(path);

        Resource fileResource = resourceLoader.getResource("classpath:" + "file-format/create-class-format.csv");
        InputStream inputStream = fileResource.getInputStream();
        byte[] buffer = inputStream.readAllBytes();

        ByteArrayResource resource = new ByteArrayResource(buffer);
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=create-class-format.csv");
        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(buffer.length)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
    }
}
