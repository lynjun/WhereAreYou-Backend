package com.example.whereareyou.memberSchedule.request;

import lombok.Data;

/**
 * packageName    : com.example.whereareyou.memberSchedule.request
 * fileName       : RequestRefuseSchedule
 * author         : pjh57
 * date           : 2023-11-05
 * description    : 일정 거절 Request
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023-11-05        pjh57       최초 생성
 */
@Data
public class RequestRefuseSchedule {
    private String refuseMemberId;
    private String scheduleId;
}
