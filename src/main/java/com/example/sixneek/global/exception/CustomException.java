package com.example.sixneek.global.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class CustomException extends RuntimeException {
    HttpStatus status;
    public CustomException(String message) {
        super(message);
        this.status = HttpStatus.BAD_REQUEST;
    }
    public CustomException(HttpStatus status, String message) {
        super(message);
        this.status = status;
    }
}
