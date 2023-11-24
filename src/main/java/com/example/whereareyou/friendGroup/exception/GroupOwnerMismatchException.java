package com.example.whereareyou.friendGroup.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * packageName    : com.example.whereareyou.friendGroup.exception
 * fileName       : FriendGroupNotFoundException
 * author         : pjh57
 * date           : 2023-11-20
 * description    : 해당 멤버가 생성한 그룹이 아닐 때 발생하는 Exception
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023-11-20        pjh57       최초 생성
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class GroupOwnerMismatchException extends RuntimeException{
    public GroupOwnerMismatchException(String m) {
        super(m);
    }
}
