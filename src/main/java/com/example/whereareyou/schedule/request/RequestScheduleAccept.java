package com.example.whereareyou.schedule.request;

import lombok.Data;

@Data
public class RequestScheduleAccept {
    private String acceptMemberId;
    private String scheduleId;
}
