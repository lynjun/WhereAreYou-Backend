package com.example.whereareyou.member.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class SelfSearchException extends RuntimeException{
    public SelfSearchException(String m){
        super(m);
    }
}
