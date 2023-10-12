package com.example.whereareyou.repository;

import com.example.whereareyou.domain.RefreshToken;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, String> {

    RefreshToken findByRefreshToken(String token);
    RefreshToken findByMemberId(String memberId);
}
