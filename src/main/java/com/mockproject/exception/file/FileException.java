package com.mockproject.exception.file;

import com.mockproject.exception.ObjectException;
import org.springframework.http.HttpStatus;

import java.time.ZonedDateTime;

public class FileException extends ObjectException {
    public FileException(String message, HttpStatus httpStatus, ZonedDateTime timestamp) {
        super(message, httpStatus, timestamp);
    }

}
