package com.example.whereareyou.memberInfo.request;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * packageName    : com.example.whereareyou.vo.request.memberInfo
 * fileName       : RequestMemberInfo
 * author         : pjh57
 * date           : 2023-10-11
 * description    : 위도 경도 Request
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023-10-11        pjh57       최초 생성
 */
@Data
public class RequestMemberInfo {
    private String memberId;
    private Double latitude;
    private Double longitude;
}
