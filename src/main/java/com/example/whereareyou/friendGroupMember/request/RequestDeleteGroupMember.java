package com.example.whereareyou.friendGroupMember.request;

import lombok.Data;

/**
 * packageName    : com.example.whereareyou.friendGroupMember.request
 * fileName       : RequestDeleteGroupMember
 * author         : pjh57
 * date           : 2023-11-20
 * description    : 그룹 멤버 삭제 Request
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023-11-20        pjh57       최초 생성
 */
@Data
public class RequestDeleteGroupMember {
    private String ownerId;
    private String deleteMemberId;
    private String groupId;
}
