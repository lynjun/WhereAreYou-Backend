package com.example.whereareyou.vo.response.member;

import com.example.whereareyou.dto.FriendLoginList;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseLogin {

    private String accessToken;
    private String refreshToken;
    private String memberId;
    private List<FriendLoginList> friendList;

}
