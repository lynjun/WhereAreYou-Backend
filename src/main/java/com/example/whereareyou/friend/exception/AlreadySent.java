package com.example.whereareyou.friend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class AlreadySent extends RuntimeException{
    public AlreadySent(String m){
        super(m);
    }
}
