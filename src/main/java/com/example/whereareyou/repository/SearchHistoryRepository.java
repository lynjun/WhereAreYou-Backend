package com.example.whereareyou.repository;

import com.example.whereareyou.domain.Member;
import com.example.whereareyou.domain.SearchHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * packageName    : com.example.whereareyou.repository
 * fileName       : SearchHistoryRepository
 * author         : pjh57
 * date           : 2023-10-11
 * description    : 검색 Repository
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023-10-11        pjh57       최초 생성
 */
@Repository
public interface SearchHistoryRepository extends JpaRepository<SearchHistory, String> {
    List<SearchHistory> findByMember(Member member);
}
