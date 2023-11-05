package com.example.whereareyou.friend.response;

import com.example.whereareyou.dto.FriendRequestList;
import com.example.whereareyou.dto.ScheduleList;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseFriendRequestList {

    private List<FriendRequestList> friendsRequestList;
    private int todaySchedule;
}
