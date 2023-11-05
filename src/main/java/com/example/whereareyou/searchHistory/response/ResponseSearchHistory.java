package com.example.whereareyou.searchHistory.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.ArrayList;

/**
 * packageName    : com.example.whereareyou.vo.response.searchHistory
 * fileName       : ResponseSearchHistory
 * author         : pjh57
 * date           : 2023-10-11
 * description    : 사용자 검색 기록 조회
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023-10-11        pjh57       최초 생성
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseSearchHistory {
    private ArrayList<String> searchHistoryList;
}