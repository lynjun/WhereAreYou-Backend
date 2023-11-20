package com.example.whereareyou.global.service;

import com.example.whereareyou.global.domain.FcmToken;
import com.example.whereareyou.global.repository.FcmTokenRepository;
import com.example.whereareyou.global.request.RequestFcmToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * packageName    : com.example.whereareyou.global.service
 * fileName       : FcmTokenService
 * author         : pjh57
 * date           : 2023-11-20
 * description    : FCM 토큰 기본 Service
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023-11-20        pjh57       최초 생성
 */
@Service
@Transactional
public class FcmTokenService {
    private final FcmTokenRepository fcmTokenRepository;

    @Autowired
    public FcmTokenService(FcmTokenRepository fcmTokenRepository) {
        this.fcmTokenRepository = fcmTokenRepository;
    }

    /**
     * Save or update token.
     *
     * @param requestFcmToken the request fcm token
     */
    public void saveOrUpdateToken(RequestFcmToken requestFcmToken) {
        Optional<FcmToken> existingToken = fcmTokenRepository.findByMemberId(requestFcmToken.getMemberId());

        if (existingToken.isPresent()) {
            // 토큰 갱신
            FcmToken updatedToken = existingToken.get().toBuilder()
                    .targetToken(requestFcmToken.getTargetToken())
                    .build();
            fcmTokenRepository.save(updatedToken);
        } else {
            // 새 토큰 저장
            FcmToken newToken = FcmToken.builder()
                    .memberId(requestFcmToken.getMemberId())
                    .targetToken(requestFcmToken.getTargetToken())
                    .build();
            fcmTokenRepository.save(newToken);
        }
    }

    /**
     * Get token by member id optional.
     *
     * @param id the id
     * @return the optional
     */
    public Optional<FcmToken> getTokenByMemberId(String id){
        return fcmTokenRepository.findByMemberId(id);
    }
}