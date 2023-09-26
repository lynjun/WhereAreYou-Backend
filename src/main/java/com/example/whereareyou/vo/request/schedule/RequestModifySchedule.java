package com.example.whereareyou.vo.request.schedule;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * packageName    : project.whereareyou.vo.request.schedule
 * fileName       : RequestModifySchedule
 * author         : pjh57
 * date           : 2023-09-16
 * description    : 일정 수정 Request
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023-09-16        pjh57       최초 생성
 */
@Data
public class RequestModifySchedule {
    private String memberId;
    private String scheduleId;
    private LocalDateTime start;
    private LocalDateTime end;
    private String title;
    private String place;
    private String memo;
    private List<String> memberIdList;
}
