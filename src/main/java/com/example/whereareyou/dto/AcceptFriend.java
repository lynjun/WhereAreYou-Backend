package com.example.whereareyou.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Getter
@NoArgsConstructor
public class AcceptFriend {

    private String friendRequestId;
    private String memberId;
    private String senderId;

}