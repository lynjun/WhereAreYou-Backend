package com.example.whereareyou.member.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class UserIdDuplicatedException extends RuntimeException{
    public UserIdDuplicatedException(String m){
        super(m);
    }
}
