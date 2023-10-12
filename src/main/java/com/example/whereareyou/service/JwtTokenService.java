package com.example.whereareyou.service;

import com.example.whereareyou.domain.RefreshToken;
import com.example.whereareyou.dto.TokenDto;
import com.example.whereareyou.exception.customexception.ExpiredJwt;
import com.example.whereareyou.exception.customexception.JwtTokenMismatchException;
import com.example.whereareyou.exception.customexception.TokenNotFound;
import com.example.whereareyou.repository.RefreshTokenRepository;
import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@RequiredArgsConstructor
@Component
public class JwtTokenService {

    private final RefreshTokenRepository refreshTokenRepository;

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.accessTokenExpiration}")
    private long accessTokenExpiration;

    @Value("${jwt.refreshTokenExpiration}")
    private long refreshTokenExpiration;

    // accessToken 생성
    public String generateAccessToken(String memberId) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + accessTokenExpiration);

        return Jwts.builder()
                .setSubject(memberId)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
    }

    // refreshToken 생성
    public String generateRefreshToken(String memberId) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + refreshTokenExpiration);

        String refreshToken = Jwts.builder()
                .setSubject(memberId)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();

        RefreshToken tokenId = refreshTokenRepository.findByMemberId(memberId);

        if(tokenId != null){
            refreshTokenRepository.delete(tokenId);
        }

        RefreshToken token = RefreshToken.builder()
                .memberId(memberId)
                .refreshToken(refreshToken)
                .build();

        refreshTokenRepository.save(token);

        return refreshToken;

    }

    public void validateToken(String token){
        try{
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token);
        } catch(ExpiredJwtException e) {
            throw new ExpiredJwt("만료된 토큰 입니다.");
        } catch(JwtException e) {
            throw new TokenNotFound("존재 하지 않은 토큰 입니다.");
        }
    }

    public String validateUserIdFromRequest(HttpServletRequest request, String userId) throws Exception {
        String jwtToken = request.getHeader(HttpHeaders.AUTHORIZATION);
        String extractedUserId = extractUserId(jwtToken);

        if(!userId.equals(extractedUserId)) {
            throw new JwtTokenMismatchException("accessToken 정보와 userId가 일치하지 않습니다.");
        }

        return extractedUserId;
    }

    private String extractUserId(String jwtToken) throws JwtException {
        if (jwtToken != null && jwtToken.startsWith("Bearer ")) {
            String token = jwtToken.substring(7);

            Jws<Claims> claimsJws = Jwts.parser()
                    .setSigningKey(jwtSecret)
                    .parseClaimsJws(token);

            Claims claims = claimsJws.getBody();
            return claims.getSubject();
        }

        throw new JwtException("Invalid JWT token");
    }

    private String getMemberId(String token){
        Claims claims = Jwts.parser()
                .setSigningKey(jwtSecret)
                .parseClaimsJws(token)
                .getBody();

        return claims.getSubject();
    }

    public TokenDto reissueToken(TokenDto tokenDto){

        validateToken(tokenDto.getRefreshToken());

        String memberId = getMemberId(tokenDto.getRefreshToken());

        tokenDto = new TokenDto(
                generateAccessToken(memberId),
                generateRefreshToken(memberId)
        );
        return tokenDto;
    }
}
