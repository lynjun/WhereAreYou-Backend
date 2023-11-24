package com.example.whereareyou.schedule.request;

import lombok.Data;

@Data
public class RequestScheduleArrived {
    private String arrivedMemberId;
    private String scheduleId;
}
