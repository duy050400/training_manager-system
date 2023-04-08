package com.mockproject.exception.entity;

public class DateNotValidExption extends RuntimeException {

    public DateNotValidExption(String message) {
        super(message);
    }

    public DateNotValidExption(String message, Throwable cause) {
        super(message, cause);
    }
}
