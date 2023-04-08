package com.mockproject.exception.entity;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.ZoneId;
import java.time.ZonedDateTime;

@ControllerAdvice
public class EntityExceptionHandler {

    @ExceptionHandler(value = {EntityNotFoundException.class})
    public ResponseEntity<Object> handleEntityNotFoundException(EntityNotFoundException e) {
        HttpStatus notFound = HttpStatus.NOT_FOUND;
        EntityException exception = new EntityException(
                e.getMessage(),
                notFound,
                ZonedDateTime.now(ZoneId.of("VST"))
        );
        return new ResponseEntity<>(exception, notFound);
    }

    @ExceptionHandler(value = {DateNotValidExption.class})
    public ResponseEntity<Object> handleDateNoteValidException(DateNotValidExption e) {
        HttpStatus badRequest = HttpStatus.BAD_REQUEST;
        EntityException exception = new EntityException(
                e.getMessage(),
                badRequest,
                ZonedDateTime.now(ZoneId.of("VST"))
        );
        return new ResponseEntity<>(exception, badRequest);
    }
}
