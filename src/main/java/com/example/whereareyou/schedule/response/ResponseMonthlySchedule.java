package com.example.whereareyou.schedule.response;

import com.example.whereareyou.schedule.dto.MonthlyScheduleResponseDTO;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;

/**
 * packageName    : project.whereareyou.vo.response.schedule
 * fileName       : ResponseMonthlySchedule
 * author         : pjh57
 * date           : 2023-09-17
 * description    : 월별 일정 정보 Response
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023-09-17        pjh57       최초 생성
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseMonthlySchedule {
    private Integer year;
    private Integer month;
    private List<MonthlyScheduleResponseDTO> schedules;
}
