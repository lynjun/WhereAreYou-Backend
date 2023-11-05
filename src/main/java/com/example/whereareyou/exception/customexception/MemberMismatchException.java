package com.example.whereareyou.exception.customexception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class MemberMismatchException extends RuntimeException{
    public MemberMismatchException(String m) {
        super(m);
    }
}