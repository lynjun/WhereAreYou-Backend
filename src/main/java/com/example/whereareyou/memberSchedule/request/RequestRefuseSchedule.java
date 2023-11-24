package com.example.whereareyou.memberSchedule.request;

import lombok.Data;

@Data
public class RequestRefuseSchedule {
    private String refuseMemberId;
    private String scheduleId;
}
