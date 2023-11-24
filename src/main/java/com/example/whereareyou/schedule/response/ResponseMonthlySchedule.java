package com.example.whereareyou.schedule.response;

import com.example.whereareyou.schedule.dto.MonthlyScheduleResponseDTO;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseMonthlySchedule {
    private Integer year;
    private Integer month;
    private List<MonthlyScheduleResponseDTO> schedules;
}
