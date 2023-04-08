package com.mockproject.service.interfaces;

import com.mockproject.dto.FileClassResponseDTO;
import org.apache.commons.csv.CSVParser;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

public interface   IFileService {
    public CSVParser readFile(MultipartFile file, String encodingType,String separator);

    FileClassResponseDTO readFileCsv(MultipartFile file) throws IOException;

    byte[] getCsvFile(File file) throws IOException;


}
