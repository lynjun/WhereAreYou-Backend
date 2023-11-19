package com.example.whereareyou.memberInfo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * packageName    : com.example.whereareyou.memberInfo.exception
 * fileName       : InvalidRequestTimeException
 * author         : pjh57
 * date           : 2023-11-20
 * description    : 실시간 위도 경도 요청 시간 범위 Exception
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023-11-20        pjh57       최초 생성
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidRequestTimeException extends RuntimeException {
    public InvalidRequestTimeException(String message) {
        super(message);
    }
}