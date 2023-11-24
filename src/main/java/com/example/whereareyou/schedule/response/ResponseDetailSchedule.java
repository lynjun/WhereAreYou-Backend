package com.example.whereareyou.schedule.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseDetailSchedule {
    private String creatorId;
    private LocalDateTime start;
    private LocalDateTime end;
    private String title;
    private String place;
    private String memo;
    private Double destinationLatitude;
    private Double destinationLongitude;
    private List<String> friendsIdListDTO;
}
