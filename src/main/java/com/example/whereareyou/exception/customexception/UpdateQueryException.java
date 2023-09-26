package com.example.whereareyou.exception.customexception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * packageName    : project.whereareyou.exception.customexception
 * fileName       : ScheduleController
 * author         : pjh57
 * date           : 2023-09-16
 * description    : UpdateQueryException
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023-09-16        pjh57       최초 생성
 */
@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class UpdateQueryException extends RuntimeException{
    public UpdateQueryException(String m) {
        super(m);
    }
}