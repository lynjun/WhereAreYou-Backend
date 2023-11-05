package com.example.whereareyou.memberSchedule.request;

import lombok.Data;

import java.util.List;

/**
 * packageName    : com.example.whereareyou.memberSchedule.request
 * fileName       : RequestModifyMemberSchedule
 * author         : pjh57
 * date           : 2023-11-05
 * description    : 일정 친구 수정 Request
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023-11-05        pjh57       최초 생성
 */
@Data
public class RequestModifyMemberSchedule {
    private String creatorId;
    private String scheduleId;
    private List<String> friendId;
}
