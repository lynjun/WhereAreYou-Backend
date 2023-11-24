package com.example.whereareyou.searchHistory.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class SearchHistoryNotFoundException extends RuntimeException{
    public SearchHistoryNotFoundException(String m) {
        super(m);
    }
}