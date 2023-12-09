package com.example.whereareyou.global.service;

import com.example.whereareyou.global.domain.FcmToken;
import com.example.whereareyou.global.repository.FcmTokenRepository;
import com.example.whereareyou.global.request.RequestFcmToken;
import com.example.whereareyou.member.exception.UserNotFoundException;
import com.example.whereareyou.member.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static com.example.whereareyou.global.constant.ExceptionConstant.USER_NOT_FOUND_EXCEPTION_MESSAGE;

@Service
@Transactional
public class FcmTokenService {
    private final FcmTokenRepository fcmTokenRepository;
    private final MemberRepository memberRepository;

    @Autowired
    public FcmTokenService(FcmTokenRepository fcmTokenRepository,
                           MemberRepository memberRepository) {
        this.fcmTokenRepository = fcmTokenRepository;
        this.memberRepository = memberRepository;
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

    /**
     * Delete FCM Token by member ID
     *
     * @param memberId
     */
    public void deleteFcmToken(String memberId){
        FcmToken fcmToken = fcmTokenRepository.findByMemberId(memberId)
                .orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND_EXCEPTION_MESSAGE));

        fcmTokenRepository.delete(fcmToken);
    }
}