package com.example.whereareyou.exception.customexception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * packageName    : project.whereareyou.exception.customexception
 * fileName       : ScheduleController
 * author         : pjh57
 * date           : 2023-09-16
 * description    : UserNotFoundException
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023-09-16        pjh57       최초 생성
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class NotCreatedScheduleByMemberException extends RuntimeException{
    public NotCreatedScheduleByMemberException(String m) {
        super(m);
    }
}