package com.example.whereareyou.refreshToken.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class JwtTokenMismatchException extends RuntimeException{
    public JwtTokenMismatchException(String m) {
        super(m);
    }
}