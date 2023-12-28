package com.example.whereareyou.member.repository;

import com.example.whereareyou.member.domain.Member;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * packageName    : project.whereareyou.repository
 * fileName       : MemberRepository
 * author         : pjh57
 * date           : 2023-09-16
 * description    : MemberRepository
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023-09-16        pjh57       최초 생성
 */
@Repository
public interface MemberRepository extends JpaRepository<Member, String> {

    Optional<Member>findByUserId(String userId);

    Optional<Member> findByEmail(String email);

    @Query("SELECT m FROM Member m WHERE m.id IN :friendIds")
    List<Member> findById(List<String> friendIds);

    @Modifying
    @Transactional
    @Query("delete from Friend f where f.owner =:memberId")
    void deleteFriendOwnerByMemberId(Member memberId);

    @Modifying
    @Transactional
    @Query("delete from Friend f where f.friends =:memberId")
    void deleteFriendFriendsByMemberId(Member memberId);

    @Modifying
    @Transactional
    @Query("delete from FriendGroup f where f.owner =:memberId")
    void deleteFriendGroupByMemberId(Member memberId);

    @Modifying
    @Transactional
    @Query("delete from FriendGroupMember f where f.member =:memberId")
    void deleteFriendGroupMemberByMemberId(Member memberId);

    @Modifying
    @Transactional
    @Query("delete from SearchHistory s where s.member =:memberId")
    void deleteSearchHistoryByMemberId(Member memberId);

    @Modifying
    @Transactional
    @Query("delete from FriendRequest f where f.senderId =:memberId")
    void deleteFriendRequestSenderIdByMemberId(Member memberId);

    @Modifying
    @Transactional
    @Query("delete from FriendRequest f where f.receiverId =:memberId")
    void deleteFriendRequestReceiverByMemberId(Member memberId);

    @Modifying
    @Transactional
    @Query("delete from Member m where m.id =:member")
    void deleteById(@NotNull @Param("member") String member);


}
