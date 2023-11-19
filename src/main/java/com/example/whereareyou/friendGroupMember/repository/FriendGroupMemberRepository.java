package com.example.whereareyou.friendGroupMember.repository;

import com.example.whereareyou.friendGroup.domain.FriendGroup;
import com.example.whereareyou.friendGroupMember.domain.FriendGroupMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * packageName    : com.example.whereareyou.friendGroupMember.repository
 * fileName       : FriendGroupMemberRepository
 * author         : pjh57
 * date           : 2023-11-20
 * description    : 그룹 친구 목록 Repository
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023-11-20        pjh57       최초 생성
 */
@Repository
public interface FriendGroupMemberRepository extends JpaRepository<FriendGroupMember, String> {
    List<FriendGroupMember> findByFriendGroup(FriendGroup friendGroup);
}
