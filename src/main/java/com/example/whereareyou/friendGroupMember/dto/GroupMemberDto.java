package com.example.whereareyou.friendGroupMember.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * packageName    : com.example.whereareyou.friendGroupMember.dto
 * fileName       : GroupMemberDto
 * author         : pjh57
 * date           : 2023-11-20
 * description    : 그룹 생성을 위한 DTO
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023-11-20        pjh57       최초 생성
 */
@Data
@AllArgsConstructor
public class GroupMemberDto {
    private String memberId;
    private String userName;
}
