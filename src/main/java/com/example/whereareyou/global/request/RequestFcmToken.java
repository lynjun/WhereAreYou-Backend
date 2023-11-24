package com.example.whereareyou.global.request;

import lombok.Data;

@Data
public class RequestFcmToken {
    private String memberId;
    private String targetToken;
}
