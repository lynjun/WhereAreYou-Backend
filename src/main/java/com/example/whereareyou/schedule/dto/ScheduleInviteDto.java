package com.example.whereareyou.schedule.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ScheduleInviteDto {

    private String scheduleId;
    private String title;
    private String userName;
    private LocalDateTime start;
    private LocalDateTime createTime;

}
