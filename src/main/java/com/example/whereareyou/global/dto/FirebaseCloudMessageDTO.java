package com.example.whereareyou.global.dto;

import lombok.Data;

@Data
public class FirebaseCloudMessageDTO {
    String targetToken;
    String title;
    String body;
}
