package com.example.whereareyou.service;

import com.example.whereareyou.domain.Member;
import com.example.whereareyou.domain.SearchHistory;
import com.example.whereareyou.exception.customexception.UserNotFoundException;
import com.example.whereareyou.repository.MemberRepository;
import com.example.whereareyou.repository.SearchHistoryRepository;
import com.example.whereareyou.vo.request.searchHistory.RequestSearchHistory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

/**
 * packageName    : com.example.whereareyou.service
 * fileName       : SearchHistoryService
 * author         : pjh57
 * date           : 2023-10-11
 * description    : 검색 Service
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023-10-11        pjh57       최초 생성
 */
@Service
@Transactional
public class SearchHistoryService {
    private final SearchHistoryRepository searchHistoryRepository;
    private final MemberRepository memberRepository;

    @Autowired
    public SearchHistoryService(SearchHistoryRepository searchHistoryRepository, MemberRepository memberRepository) {
        this.searchHistoryRepository = searchHistoryRepository;
        this.memberRepository = memberRepository;
    }

    /**
     * Set search history.
     *
     * @param requestSearchHistory the request search history
     */
    public void setSearchHistory(RequestSearchHistory requestSearchHistory){
        /*
         예외처리
         404 ScheduleNotFoundException: scheduleId Not Found
         401: Unauthorized (추후에 추가할 예정)
         500: Server
        */
        Member findMember = memberRepository.findById(requestSearchHistory.getMemberId())
                .orElseThrow(() -> new UserNotFoundException("존재하지 않는 memberId입니다."));

        SearchHistory searchHistory = SearchHistory.builder()
                .searchHistory(requestSearchHistory.getSearchHistory())
                .member(findMember)
                .build();

        searchHistoryRepository.save(searchHistory);
    }
}
