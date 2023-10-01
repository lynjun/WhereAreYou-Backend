package com.example.whereareyou.dto;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * packageName    : project.whereareyou.dto
 * fileName       : BriefDateScheduleDTO
 * author         : pjh57
 * date           : 2023-09-17
 * description    : 일별 간략정보 DTO
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023-09-17        pjh57       최초 생성
 */
@Data
public class BriefDateScheduleDTO {
    private String scheduleId;
    private String title;
    private LocalDateTime start;
    private LocalDateTime end;
}
