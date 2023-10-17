package com.example.whereareyou.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class FriendRequestDto {

    private List<String> memberIdList; // 친추 보낼 아이디들
    private String userId; // 친추 보낸 아이디
}
