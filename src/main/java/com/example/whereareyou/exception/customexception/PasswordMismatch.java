package com.example.whereareyou.exception.customexception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class PasswordMismatch extends RuntimeException{
    public PasswordMismatch(String m){
        super(m);
    }
}