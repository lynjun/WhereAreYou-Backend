package com.example.whereareyou.searchHistory.repository;

import com.example.whereareyou.member.domain.Member;
import com.example.whereareyou.searchHistory.domain.SearchHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SearchHistoryRepository extends JpaRepository<SearchHistory, String> {
    List<SearchHistory> findByMember(Member member);
}
