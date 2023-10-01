package com.example.whereareyou.vo.request.schedule;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * packageName    : project.whereareyou.vo.request.schedule
 * fileName       : RequestSaveSchedule
 * author         : pjh57
 * date           : 2023-09-16
 * description    : 일정 추가 Request
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023-09-16        pjh57       최초 생성
 */
@Data
public class RequestSaveSchedule {
    private String memberId;
    private LocalDateTime start;
    private LocalDateTime end;
    private String title;
    private String place;
    private String memo;
    private List<String> memberIdList;
}
