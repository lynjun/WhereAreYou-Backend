package com.example.whereareyou.searchHistory.request;

import lombok.Data;

/**
 * packageName    : com.example.whereareyou.vo.request.searchHistory
 * fileName       : RequestDeleteSearchHistory
 * author         : pjh57
 * date           : 2023-10-11
 * description    : 사용자 검색 기록 삭제 Request
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023-10-11        pjh57       최초 생성
 */
@Data
public class RequestDeleteSearchHistory {
    private String memberId;
    private String searchHistoryId;
}
