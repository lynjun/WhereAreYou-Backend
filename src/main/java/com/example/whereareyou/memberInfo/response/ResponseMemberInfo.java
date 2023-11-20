package com.example.whereareyou.memberInfo.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

/**
 * packageName    : com.example.whereareyou.vo.response.memberInfo
 * fileName       : ResponseMemberInfo
 * author         : pjh57
 * date           : 2023-10-11
 * description    : 위도 경도 Response
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023-10-11        pjh57       최초 생성
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseMemberInfo {
    private String memberId;
    private Double latitude;
    private Double longitude;
}
