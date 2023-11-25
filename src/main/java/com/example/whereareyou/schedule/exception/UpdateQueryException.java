package com.example.whereareyou.schedule.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class UpdateQueryException extends RuntimeException{
    public UpdateQueryException(String m) {
        super(m);
    }
}