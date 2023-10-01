package com.example.whereareyou.exception.customexception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class UserIdDuplicatedException extends RuntimeException{
    public UserIdDuplicatedException(String m){
        super(m);
    }
}
