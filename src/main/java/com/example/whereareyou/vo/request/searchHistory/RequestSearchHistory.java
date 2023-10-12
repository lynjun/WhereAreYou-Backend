package com.example.whereareyou.vo.request.searchHistory;

import lombok.Data;

/**
 * packageName    : com.example.whereareyou.vo.request.searchHistory
 * fileName       : RequestSearchHistory
 * author         : pjh57
 * date           : 2023-10-11
 * description    : 사용자 검색 기록 입력 Request
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023-10-11        pjh57       최초 생성
 */
@Data
public class RequestSearchHistory {
    private String memberId;
    private String searchHistory;
}
