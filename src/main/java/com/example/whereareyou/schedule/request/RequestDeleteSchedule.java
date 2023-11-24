package com.example.whereareyou.schedule.request;

import lombok.Data;

@Data
public class RequestDeleteSchedule {
    private String creatorId;
    private String scheduleId;
}
