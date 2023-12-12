package com.example.whereareyou.global.repository;

import com.example.whereareyou.global.domain.FcmToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface FcmTokenRepository extends JpaRepository<FcmToken, String> {
    Optional<FcmToken> findByMemberId(String memberId);

    @Transactional
    void deleteByMemberId(String memberId);
}
