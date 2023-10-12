package com.example.whereareyou.vo.response.searchHistory;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * packageName    : com.example.whereareyou.vo.response.searchHistory
 * fileName       : ResponseSaveSearchHistory
 * author         : pjh57
 * date           : 2023-10-11
 * description    : 사용자 검색 기록 입력 Response
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023-10-11        pjh57       최초 생성
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@AllArgsConstructor
public class ResponseSaveSearchHistory {
    private String searchHistoryId;
}
