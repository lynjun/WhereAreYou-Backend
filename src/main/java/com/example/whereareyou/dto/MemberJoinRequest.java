package com.example.whereareyou.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class MemberJoinRequest {

    private String userName;
    private String userId;
    private String password;
    private String email;

}

