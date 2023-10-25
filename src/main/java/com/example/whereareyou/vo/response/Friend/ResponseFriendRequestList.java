package com.example.whereareyou.vo.response.Friend;

import com.example.whereareyou.dto.FriendRequestList;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.HashMap;
import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseFriendRequestList {

    private List<FriendRequestList> friendsRequestList;
}
