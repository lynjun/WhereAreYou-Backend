package com.example.whereareyou.controller;

import com.example.whereareyou.service.SearchHistoryService;
import com.example.whereareyou.vo.request.searchHistory.RequestSearchHistory;
import com.example.whereareyou.vo.response.searchHistory.ResponseSearchHistory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
}
