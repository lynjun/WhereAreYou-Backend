package com.example.whereareyou.vo.response.Friend;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.HashMap;
import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseFriendRequestList {

    private List<String> friendsRequestList;
    private HashMap<String,String> friendRequestMap;
}
