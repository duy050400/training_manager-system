package com.mockproject.exception.entity;

import com.mockproject.exception.ObjectException;
import org.springframework.http.HttpStatus;

import java.time.ZonedDateTime;

public class EntityException extends ObjectException {
    public EntityException(String message, HttpStatus httpStatus, ZonedDateTime timestamp) {
        super(message, httpStatus, timestamp);
    }
}
