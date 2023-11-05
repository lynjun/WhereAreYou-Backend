package com.example.whereareyou.schedule.request;

import lombok.Data;

/**
 * packageName    : com.example.whereareyou.vo.request.schedule
 * fileName       : RequestScheduleClosed
 * author         : pjh57
 * date           : 2023-10-10
 * description    : 일정 종료 Request
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023-10-10        pjh57       최초 생성
 */
@Data
public class RequestScheduleClosed {
    private String creatorId;
    private String scheduleId;
}
