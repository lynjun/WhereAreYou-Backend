package com.example.whereareyou.friendGroup.request;

import lombok.Data;

/**
 * packageName    : com.example.whereareyou.friendGroup.request
 * fileName       : RequestModifyGroupName
 * author         : pjh57
 * date           : 2023-11-20
 * description    : 그룹 이름 수정 Request
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023-11-20        pjh57       최초 생성
 */
@Data
public class RequestModifyGroupName {
    private String ownerId;
    private String groupId;
    private String name;
}
