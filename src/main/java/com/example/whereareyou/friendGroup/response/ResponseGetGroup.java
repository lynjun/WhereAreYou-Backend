package com.example.whereareyou.friendGroup.response;

import com.example.whereareyou.friendGroupMember.dto.GroupMemberDto;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

/**
 * packageName    : com.example.whereareyou.friendGroup.response
 * fileName       : ResponseGetGroup
 * author         : pjh57
 * date           : 2023-11-20
 * description    : 그룹 목록 Response
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023-11-20        pjh57       최초 생성
 */
@Data
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseGetGroup {
    private String name;
    private List<GroupMemberDto> members;
}
