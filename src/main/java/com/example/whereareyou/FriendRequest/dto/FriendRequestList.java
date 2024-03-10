package com.example.whereareyou.FriendRequest.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class FriendRequestList {

    String friendRequestId;
    String senderId;
    LocalDateTime createTime;

}

