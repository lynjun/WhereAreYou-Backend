package com.example.whereareyou.schedule.request;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class RequestSaveSchedule {
    private String memberId;
    private LocalDateTime appointmentTime;
    private String title;
    private String place;
    private String roadName;
    private String memo;
    private Double destinationLatitude;
    private Double destinationLongitude;
    private List<String> memberIdList;
}
