package com.example.whereareyou.vo.response.schedule;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * packageName    : project.whereareyou.vo.response.schedule
 * fileName       : ResponseDetailSchedule
 * author         : pjh57
 * date           : 2023-09-21
 * description    : 일별 일정 상세 정보 Response
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023-09-21        pjh57       최초 생성
 */
@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseDetailSchedule {
    private LocalDateTime start;
    private LocalDateTime end;
    private String title;
    private String place;
    private String memo;
    private List<String> friendsIdListDTO;
}
