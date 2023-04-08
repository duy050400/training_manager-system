package com.mockproject.utils;

import org.springframework.web.multipart.MultipartFile;

public class CSVUtils {
    public static String TYPE = "text/csv";

    public static boolean hasCSVFormat(MultipartFile file) {

        if (!TYPE.equals(file.getContentType())) {
            return false;
        }

        return true;
    }
}
