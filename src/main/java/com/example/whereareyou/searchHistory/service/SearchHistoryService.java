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

import static com.example.whereareyou.global.constant.ExceptionConstant.SEARCH_HISTORY_NOT_FOUND_EXCEPTION_MESSAGE;
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
        Member findMember = returnMember(memberId);

        List<String> searchHistoryList = findSearchHistories(findMember);

        return setResponseSearchHistories(searchHistoryList);
    }

    private List<String> findSearchHistories(Member findMember) {
        List<SearchHistory> histories = searchHistoryRepository.findByMember(findMember);
        return histories.stream()
                .map(SearchHistory::getSearchHistory)
                .collect(Collectors.toList());
    }

    private ResponseSearchHistory setResponseSearchHistories(List<String> searchHistoryList) {
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
        returnMember(requestDeleteSearchHistory.getMemberId());

        SearchHistory searchHistory = returnSearchHistory(requestDeleteSearchHistory.getSearchHistoryId());

        searchHistoryRepository.delete(searchHistory);
    }

    private SearchHistory returnSearchHistory(String searchHistoryId){
        return searchHistoryRepository.findById(searchHistoryId)
                .orElseThrow(() -> new SearchHistoryNotFoundException(SEARCH_HISTORY_NOT_FOUND_EXCEPTION_MESSAGE));
    }
}
