package com.example.whereareyou.friendGroupMember.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * packageName    : com.example.whereareyou.friendGroup.exception
 * fileName       : FriendGroupNotFoundException
 * author         : pjh57
 * date           : 2023-11-20
 * description    : 그룹에 Member가 존재하지 않을때 Exception
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023-11-20        pjh57       최초 생성
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class MemberNotInGroupException extends RuntimeException{
    public MemberNotInGroupException(String m) {
        super(m);
    }
}
