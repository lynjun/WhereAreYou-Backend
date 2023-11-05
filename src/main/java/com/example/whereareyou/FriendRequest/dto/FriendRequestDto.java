package com.example.whereareyou.FriendRequest.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class FriendRequestDto {

    private String friendId; // 친추 보낼 아이디
    private String memberId; // 친추 보낸 아이디
}
