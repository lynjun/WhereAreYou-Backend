package com.example.whereareyou.friendGroup.request;

import lombok.Data;

/**
 * packageName    : com.example.whereareyou.friendGroup.request
 * fileName       : RequestDeleteGroup
 * author         : pjh57
 * date           : 2023-11-20
 * description    : 그룹 삭제 Request
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023-11-20        pjh57       최초 생성
 */
@Data
public class RequestDeleteGroup {
    private String ownerId;
    private String groupId;
}
