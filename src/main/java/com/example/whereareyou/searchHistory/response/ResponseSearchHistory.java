package com.example.whereareyou.searchHistory.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.ArrayList;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseSearchHistory {
    private ArrayList<String> searchHistoryList;
}