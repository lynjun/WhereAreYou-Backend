package com.example.whereareyou.searchHistory.request;

import lombok.Data;

@Data
public class RequestDeleteSearchHistory {
    private String memberId;
    private String searchHistoryId;
}
