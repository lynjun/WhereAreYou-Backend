package com.example.whereareyou.memberInfo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidRequestTimeException extends RuntimeException {
    public InvalidRequestTimeException(String message) {
        super(message);
    }
}