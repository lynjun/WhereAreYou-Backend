package com.example.whereareyou.schedule.request;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class RequestModifySchedule {
    private String creatorId;
    private String scheduleId;
    private LocalDateTime appointmentTime;
    private String title;
    private String place;
    private String memo;
    private Double destinationLatitude;
    private Double destinationLongitude;
}
