package com.example.whereareyou.friendGroup.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

/**
 * packageName    : com.example.whereareyou.friendGroup.response
 * fileName       : ResponseCreateGroup
 * author         : pjh57
 * date           : 2023-11-20
 * description    : 그룹 생성 Response
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023-11-20        pjh57       최초 생성
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseCreateGroup {
    private String groupId;
}
