package com.example.whereareyou.dto;

import lombok.Data;

/**
 * packageName    : project.whereareyou.dto
 * fileName       : MonthlyScheduleResponseDTO
 * author         : pjh57
 * date           : 2023-09-17
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023-09-17        pjh57       최초 생성
 */
@Data
public class MonthlyScheduleResponseDTO {
    private Integer date;
    private Boolean hasSchedule;
    private Integer amountSchedule;
}
