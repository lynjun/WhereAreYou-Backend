package com.example.whereareyou.vo.response.Friend;

import com.example.whereareyou.dto.FriendIdList;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseFriendIdList {

    private List<FriendIdList> friendsIdList;
}
