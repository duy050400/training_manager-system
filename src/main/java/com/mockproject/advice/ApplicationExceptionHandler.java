package com.mockproject.advice;

import com.mockproject.exception.FileException;
import com.mockproject.exception.SyllabusException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.server.ResponseStatusException;
import org.webjars.NotFoundException;

import java.io.IOException;
import java.security.InvalidParameterException;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
@Slf4j
public class ApplicationExceptionHandler {

    @ExceptionHandler(InvalidParameterException.class)
    public String handleRowOfPage(InvalidParameterException ex) { return "Error Message : " + ex.getMessage(); }

    @ExceptionHandler(NotFoundException.class)
    public String handleNotFound(NotFoundException ex) {
        return "Error Message : " + ex.getMessage();
    }

    @ExceptionHandler(IOException.class)
    public String handleIOException(IOException ex){
        return "Error Message : " + ex.getMessage();
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public Map<String, String> handleBadRequest(MethodArgumentTypeMismatchException ex) {
        Map<String, String> errorMap = new HashMap<>();
        errorMap.put("Error Paramater", ex.getParameter().toString());
        errorMap.put("Error Property Name", ex.getPropertyName());
        errorMap.put("Error Message", ex.getMessage());
        return errorMap;
    }

    @ExceptionHandler(ResponseStatusException.class)
    public String handleNoContent(ResponseStatusException ex) {
        return "Error Message : " + ex.getMessage();
    }

    @ExceptionHandler(ArrayIndexOutOfBoundsException.class)
    public String handleOutOfIndex(ArrayIndexOutOfBoundsException ex) {
        return "Error Message : " + ex.getMessage();
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Object> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
        Throwable mostSpecificCause = ex.getMostSpecificCause();
        if (mostSpecificCause instanceof DateTimeParseException) {
            int startIndex= mostSpecificCause.getMessage().indexOf("'")+1;
            int endIndex = mostSpecificCause.getMessage().indexOf("'",startIndex);
            return ResponseEntity
                    .badRequest()
                    .body("Invalid date format: " + mostSpecificCause.getMessage().substring(startIndex,endIndex));
        } else if(mostSpecificCause instanceof IllegalArgumentException){
//            if (mostSpecificCause.getMessage().contains("java.sql.Time")){
            return ResponseEntity
                    .badRequest()
                    .body("Invalid Time Format !");

        }else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @ExceptionHandler(FileException.class)
    public ResponseEntity handleFileException(FileException e){
        return ResponseEntity.status(e.getStatusCode()).body(e.getMessage());
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> handleRequestBodyError(MethodArgumentNotValidException ex){
        log.error("Exception caught in handleRequestBodyError :  {} " ,ex.getMessage(),  ex);
        var error = ex.getBindingResult().getAllErrors().stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .sorted()
                .collect(Collectors.joining(",\n"));
        log.error("errorList : {}", error);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(BindException.class)
    public ResponseEntity<String> handleRequestBodyError(BindException ex){
        log.error("Exception caught in handleRequestBodyError :  {} " ,ex.getMessage(),  ex);
        var error = ex.getBindingResult().getAllErrors().stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .sorted()
                .collect(Collectors.joining(",\n"));
        log.error("errorList : {}", error);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(SyllabusException.class)
    public ResponseEntity handleSyllabusException(SyllabusException e){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }
}
