package com.example.whereareyou.exception.customexception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class AlreadyFriendsException extends RuntimeException{
    public AlreadyFriendsException(String m){
        super(m);
    }
}

