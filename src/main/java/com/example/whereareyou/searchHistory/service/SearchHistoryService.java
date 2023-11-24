package com.example.whereareyou.searchHistory.service;

import com.example.whereareyou.member.domain.Member;
import com.example.whereareyou.searchHistory.domain.SearchHistory;
import com.example.whereareyou.searchHistory.exception.SearchHistoryNotFoundException;
import com.example.whereareyou.member.exception.UserNotFoundException;
import com.example.whereareyou.member.repository.MemberRepository;
import com.example.whereareyou.searchHistory.repository.SearchHistoryRepository;
import com.example.whereareyou.searchHistory.request.RequestDeleteSearchHistory;
import com.example.whereareyou.searchHistory.request.RequestSearchHistory;
import com.example.whereareyou.searchHistory.response.ResponseSaveSearchHistory;
import com.example.whereareyou.searchHistory.response.ResponseSearchHistory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.example.whereareyou.global.constant.ExceptionConstant.USER_NOT_FOUND_EXCEPTION_MESSAGE;

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
    public ResponseSaveSearchHistory setSearchHistory(RequestSearchHistory requestSearchHistory) {
        Member findMember = returnMember(requestSearchHistory.getMemberId());

        SearchHistory savedSearchHistory = saveSearchHistory(requestSearchHistory, findMember);

        return new ResponseSaveSearchHistory(savedSearchHistory.getId());
    }

    private Member returnMember(String memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND_EXCEPTION_MESSAGE));
    }

    private SearchHistory saveSearchHistory(RequestSearchHistory requestSearchHistory, Member findMember) {
        SearchHistory searchHistory = SearchHistory.builder()
                .searchHistory(requestSearchHistory.getSearchHistory())
                .member(findMember)
                .build();

        return searchHistoryRepository.save(searchHistory);
    }

    /**
     * Gets search history.
     *
     * @param memberId the member id
     * @return the search history
     */
    public ResponseSearchHistory getSearchHistory(String memberId) {
        /*
         예외처리
         404 ScheduleNotFoundException: scheduleId Not Found
         401: Unauthorized (추후에 추가할 예정)
         500: Server
        */
        Member findMember = memberRepository.findById(memberId)
                .orElseThrow(() -> new UserNotFoundException("존재하지 않는 memberId입니다."));

        List<SearchHistory> histories = searchHistoryRepository.findByMember(findMember);
        List<String> searchHistoryList = histories.stream()
                .map(SearchHistory::getSearchHistory)
                .collect(Collectors.toList());

        ResponseSearchHistory response = new ResponseSearchHistory();
        response.setSearchHistoryList(new ArrayList<>(searchHistoryList));

        return response;
    }

    /**
     * Delete search history.
     *
     * @param requestDeleteSearchHistory the request delete search history
     */
    public void deleteSearchHistory(RequestDeleteSearchHistory requestDeleteSearchHistory){
        /*
         예외처리
         404 MemberNotFoundException: memberId Not Found
         404 SearchHistoryNotFoundException: searchHistoryId Not Found
         401: Unauthorized (추후에 추가할 예정)
         500: Server
        */
        memberRepository.findById(requestDeleteSearchHistory.getMemberId())
                .orElseThrow(() -> new UserNotFoundException("존재하지 않는 memberId입니다."));

        SearchHistory searchHistory = searchHistoryRepository.findById(requestDeleteSearchHistory.getSearchHistoryId())
                .orElseThrow(() -> new SearchHistoryNotFoundException("존재하지 않는 searchHistoryId입니다."));

        searchHistoryRepository.delete(searchHistory);
    }
}
