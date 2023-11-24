package com.example.whereareyou.friendGroup.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * packageName    : com.example.whereareyou.friendGroup.exception
 * fileName       : FriendGroupNotFoundException
 * author         : pjh57
 * date           : 2023-11-20
 * description    : 그룹이 존재하지 않을때 Exception
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023-11-20        pjh57       최초 생성
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class FriendGroupNotFoundException extends RuntimeException{
    public FriendGroupNotFoundException(String m) {
        super(m);
    }
}
