package com.example.whereareyou.member.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Getter
@NoArgsConstructor
public class PasswordReset {

    private String userId;

    private String email;

    private String code;
}
