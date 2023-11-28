package com.example.whereareyou.friend.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseFriendIdList {

    private List<String> friendsIdList;
}
