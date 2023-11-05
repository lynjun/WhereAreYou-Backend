package com.example.whereareyou.repository;

import com.example.whereareyou.domain.Friend;
import com.example.whereareyou.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FriendRepository extends JpaRepository<Friend, String > {
    List<Friend> findByOwner(Member memberId);
}
