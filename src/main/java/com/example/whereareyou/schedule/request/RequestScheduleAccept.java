package com.example.whereareyou.schedule.request;

import lombok.Data;

/**
 * packageName    : project.whereareyou.vo.request.schedule
 * fileName       : RequestScheduleAccept
 * author         : pjh57
 * date           : 2023-09-25
 * description    : 일정 수락 Request
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023-09-25        pjh57       최초 생성
 */
@Data
public class RequestScheduleAccept {
    private String acceptMemberId;
    private String scheduleId;
}
