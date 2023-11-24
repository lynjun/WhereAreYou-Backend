package com.example.whereareyou.memberInfo.request;

import lombok.Data;

import java.util.List;

/**
 * packageName    : com.example.whereareyou.memberInfo.request
 * fileName       : RequestGetMemberInfo
 * author         : pjh57
 * date           : 2023-11-20
 * description    : 사용자 실시간 위도 경도 조회 Request
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023-11-20        pjh57       최초 생성
 */
@Data
public class RequestGetMemberInfo {
    private List<String> memberId;
    private String scheduleId;
}
