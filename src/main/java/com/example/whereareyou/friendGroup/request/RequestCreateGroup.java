package com.example.whereareyou.friendGroup.request;

import lombok.Data;

import java.util.List;

/**
 * packageName    : com.example.whereareyou.friendGroup.request
 * fileName       : RequestCreateGroup
 * author         : pjh57
 * date           : 2023-11-20
 * description    : 그룹 생성 Request
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023-11-20        pjh57       최초 생성
 */
@Data
public class RequestCreateGroup {
    private String name;
    private String ownerId;
    private List<String> groupMemberId;
}
