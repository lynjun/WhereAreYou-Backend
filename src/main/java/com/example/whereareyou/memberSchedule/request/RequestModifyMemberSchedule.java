package com.example.whereareyou.memberSchedule.request;

import lombok.Data;

import java.util.List;

@Data
public class RequestModifyMemberSchedule {
    private String creatorId;
    private String scheduleId;
    private List<String> friendId;
}
