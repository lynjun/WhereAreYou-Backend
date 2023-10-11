package com.example.whereareyou.controller;

import com.example.whereareyou.service.SearchHistoryService;
import com.example.whereareyou.vo.request.searchHistory.RequestSearchHistory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public ResponseEntity<Boolean> setSearchHistory(@RequestBody RequestSearchHistory requestSearchHistory){
        searchHistoryService.setSearchHistory(requestSearchHistory);

        return ResponseEntity.status(HttpStatus.OK).body(true);
    }
}
