package com.example.whereareyou.schedule.request;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class RequestModifySchedule {
    private String creatorId;
    private String scheduleId;
    private LocalDateTime start;
    private LocalDateTime end;
    private String title;
    private String place;
    private String memo;
}
