package com.example.whereareyou.friend.response;

import com.example.whereareyou.friend.dto.FriendIdList;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseFriendIdList {

    private List<FriendIdList> friendsIdList;
}
