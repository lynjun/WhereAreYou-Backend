package com.example.whereareyou.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Getter
@NoArgsConstructor
public class CheckPasswordRequest {

    private String userId;

    private String password;

    private String checkPassword;
}
