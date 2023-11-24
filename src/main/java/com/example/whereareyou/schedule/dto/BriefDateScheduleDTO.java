package com.example.whereareyou.schedule.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BriefDateScheduleDTO {
    private String scheduleId;
    private String title;
    private LocalDateTime start;
    private LocalDateTime end;
}
