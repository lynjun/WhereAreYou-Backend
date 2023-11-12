package com.example.whereareyou.schedule.request;

import lombok.Data;

/**
 * packageName    : project.whereareyou.vo.request.schedule
 * fileName       : RequestDeleteSchedule
 * author         : pjh57
 * date           : 2023-09-16
 * description    : 일정 삭제 Request
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023-09-16        pjh57       최초 생성
 */
@Data
public class RequestDeleteSchedule {
    private String creatorId;
    private String scheduleId;
}
