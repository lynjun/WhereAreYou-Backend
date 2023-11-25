package com.example.whereareyou.schedule.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class NotCreatedScheduleByMemberException extends RuntimeException{
    public NotCreatedScheduleByMemberException(String m) {
        super(m);
    }
}