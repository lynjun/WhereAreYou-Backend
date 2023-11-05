package com.example.whereareyou.repository;

import com.example.whereareyou.domain.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, String> {

    RefreshToken findByRefreshToken(String token);
    RefreshToken findByMemberId(String memberId);

    @Transactional
    void deleteByMemberId(String memberId);
}
