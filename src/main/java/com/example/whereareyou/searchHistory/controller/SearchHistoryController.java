package com.example.whereareyou.searchHistory.controller;

import com.example.whereareyou.searchHistory.service.SearchHistoryService;
import com.example.whereareyou.searchHistory.request.RequestDeleteSearchHistory;
import com.example.whereareyou.searchHistory.request.RequestSearchHistory;
import com.example.whereareyou.searchHistory.response.ResponseSaveSearchHistory;
import com.example.whereareyou.searchHistory.response.ResponseSearchHistory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * packageName    : com.example.whereareyou.controller
 * fileName       : SearchHistoryController
 * author         : pjh57
 * date           : 2023-10-11
 * description    : 검색
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023-10-11        pjh57       최초 생성
 */
@RestController
@RequestMapping("/search")
public class SearchHistoryController {
    private final SearchHistoryService searchHistoryService;

    @Autowired
    public SearchHistoryController(SearchHistoryService searchHistoryService) {
        this.searchHistoryService = searchHistoryService;
    }

    /**
     * 사용자 검색 기록 입력
     *
     * @param requestSearchHistory the request search history
     * @return the response entity
     */
    @PostMapping()
    public ResponseEntity<ResponseSaveSearchHistory> setSearchHistory(@RequestBody RequestSearchHistory requestSearchHistory){
        ResponseSaveSearchHistory responseSaveSearchHistory = searchHistoryService.setSearchHistory(requestSearchHistory);

        return ResponseEntity.status(HttpStatus.OK).body(responseSaveSearchHistory);
    }

    /**
     * 사용자 검색 기록 조회
     *
     * @param memberId the member id
     * @return the response entity
     */
    @GetMapping()
    public ResponseEntity<ResponseSearchHistory> getSearchHistory(@RequestParam String memberId){
        ResponseSearchHistory responseSearchHistory = searchHistoryService.getSearchHistory(memberId);

        return ResponseEntity.status(HttpStatus.OK).body(responseSearchHistory);
    }

    /**
     * 사용자 검색 기록 삭제
     *
     * @param requestDeleteSearchHistory the request delete search history
     * @return the response entity
     */
    @DeleteMapping()
    public ResponseEntity<Boolean> deleteSearchHistory(@RequestBody RequestDeleteSearchHistory requestDeleteSearchHistory){
        searchHistoryService.deleteSearchHistory(requestDeleteSearchHistory);

        return ResponseEntity.status(HttpStatus.OK).body(true);
    }
}
