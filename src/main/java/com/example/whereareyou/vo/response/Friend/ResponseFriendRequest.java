package com.example.whereareyou.vo.response.Friend;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseFriendRequest {

    private String senderId;
    private List<String> friendsRequestList;
}