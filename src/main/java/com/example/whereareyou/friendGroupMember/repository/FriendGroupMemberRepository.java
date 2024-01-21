package com.example.whereareyou.friendGroupMember.repository;

import com.example.whereareyou.friendGroup.domain.FriendGroup;
import com.example.whereareyou.friendGroupMember.domain.FriendGroupMember;
import com.example.whereareyou.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

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

    Optional<FriendGroupMember> findByFriendGroupAndMember(FriendGroup friendGroup, Member member);

    long countByFriendGroup(FriendGroup friendGroup);

    @Query("select f from FriendGroupMember f join f.member on f.friendGroup.owner =:owner and f.member =:friend")
    List<FriendGroupMember> findByOwnerAndFriend(@Param("owner") Member owner, @Param("friend") Member friend);

    @Transactional
    @Modifying
    @Query("delete from FriendGroupMember f where f.id in :ids")
    void deleteByAllId(@Param("ids") List<String> ids);
}
