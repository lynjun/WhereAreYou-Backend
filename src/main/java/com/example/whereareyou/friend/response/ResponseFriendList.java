package com.example.whereareyou.friend.response;

import com.example.whereareyou.friend.dto.FriendList;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseFriendList {

    private List<FriendList> friendsList;
}
