package com.example.whereareyou.schedule.dto;

import lombok.Data;

@Data
public class MonthlyScheduleResponseDTO {
    private Integer date;
    private Boolean hasSchedule;
    private Integer amountSchedule;
}
