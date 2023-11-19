package com.example.whereareyou.friendGroup.repository;

import com.example.whereareyou.friendGroup.domain.FriendGroup;
import com.example.whereareyou.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * packageName    : com.example.whereareyou.friendGroup.repository
 * fileName       : FriendGroupRepository
 * author         : pjh57
 * date           : 2023-11-20
 * description    : 그룹 목록 Repository
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023-11-20        pjh57       최초 생성
 */
@Repository
public interface FriendGroupRepository extends JpaRepository<FriendGroup, String> {
    List<FriendGroup> findByOwner(Member member);
}
