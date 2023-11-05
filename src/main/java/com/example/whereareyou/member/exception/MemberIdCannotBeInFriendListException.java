package com.example.whereareyou.member.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * packageName    : project.whereareyou.exception.customexception
 * fileName       : ScheduleController
 * author         : pjh57
 * date           : 2023-09-16
 * description    : MemberIdCannotBeInFriendListException
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023-09-16        pjh57       최초 생성
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class MemberIdCannotBeInFriendListException extends RuntimeException{
    public MemberIdCannotBeInFriendListException(String m) {
        super(m);
    }
}