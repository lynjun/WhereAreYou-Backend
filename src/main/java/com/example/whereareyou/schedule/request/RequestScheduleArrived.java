package com.example.whereareyou.schedule.request;

import lombok.Data;

/**
 * packageName    : com.example.whereareyou.vo.request.schedule
 * fileName       : RequestScheduleArrived
 * author         : pjh57
 * date           : 2023-10-11
 * description    : 도착 여부 Request
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023-10-11        pjh57       최초 생성
 */
@Data
public class RequestScheduleArrived {
    private String arrivedMemberId;
    private String scheduleId;
}
