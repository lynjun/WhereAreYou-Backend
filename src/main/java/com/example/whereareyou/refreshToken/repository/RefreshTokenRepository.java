package com.example.whereareyou.refreshToken.repository;

import com.example.whereareyou.refreshToken.domain.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, String> {

    RefreshToken findByMemberId(String memberId);

    @Transactional
    void deleteByMemberId(String memberId);
}
