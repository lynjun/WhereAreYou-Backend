package com.example.whereareyou.memberSchedule.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class CreatorCannotRefuseSchedule extends RuntimeException{
    public CreatorCannotRefuseSchedule(String message){
        super(message);
    }
}
