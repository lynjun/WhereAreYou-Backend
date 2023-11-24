package com.example.whereareyou.global.request;

import lombok.Data;

/**
 * packageName    : com.example.whereareyou.global.request
 * fileName       : FcmTokenRequest
 * author         : pjh57
 * date           : 2023-11-20
 * description    : FcmToken 저장 Request
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023-11-20        pjh57       최초 생성
 */
@Data
public class RequestFcmToken {
    private String memberId;
    private String targetToken;
}
