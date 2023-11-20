package com.example.whereareyou.global.repository;

import com.example.whereareyou.global.domain.FcmToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * packageName    : com.example.whereareyou.global.repository
 * fileName       : FcmTokenRepository
 * author         : pjh57
 * date           : 2023-11-20
 * description    : FCM 토큰 Repository
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023-11-20        pjh57       최초 생성
 */
@Repository
public interface FcmTokenRepository extends JpaRepository<FcmToken, String> {
    Optional<FcmToken> findByMemberId(String memberId);
}
