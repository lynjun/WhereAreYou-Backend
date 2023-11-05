package com.example.whereareyou.schedule.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

/**
 * packageName    : project.whereareyou.vo.response.schedule
 * fileName       : ResponseSaveSchedule
 * author         : pjh57
 * date           : 2023-09-16
 * description    : 일정 추가 Response
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023-09-16        pjh57       최초 생성
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseSaveSchedule {
    private String scheduleId;
}
