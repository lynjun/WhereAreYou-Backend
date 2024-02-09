package com.example.whereareyou.friend.repository;

import com.example.whereareyou.friend.domain.Friend;
import com.example.whereareyou.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface FriendRepository extends JpaRepository<Friend, String > {
    List<Friend> findByOwner(Member memberId);

    @Transactional
    void deleteByOwnerAndFriends(Member memberId, Member friendId);
}