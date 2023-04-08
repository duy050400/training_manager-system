package com.mockproject.exception.file;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.ZoneId;
import java.time.ZonedDateTime;

@ControllerAdvice
public class FileExceptionHandler {

    @ExceptionHandler(value = {FileRequestException.class})
    public ResponseEntity<Object> handleFileRequestException(FileRequestException e) {
        HttpStatus badRequest = HttpStatus.BAD_REQUEST;
        FileException exception = new FileException(
                                            e.getMessage(),
                                            badRequest,
                                            ZonedDateTime.now(ZoneId.of("VST"))
                                        );
        return new ResponseEntity<>(exception, badRequest);
    }

    @ExceptionHandler(value = {FileFormatException.class})
    public ResponseEntity<Object> handleFileFormatException(FileFormatException e) {
        HttpStatus notSupportMediaType = HttpStatus.UNSUPPORTED_MEDIA_TYPE;
        FileException exception = new FileException(
                e.getMessage(),
                notSupportMediaType,
                ZonedDateTime.now(ZoneId.of("VST"))
        );
        return new ResponseEntity<>(exception, notSupportMediaType);
    }
}
