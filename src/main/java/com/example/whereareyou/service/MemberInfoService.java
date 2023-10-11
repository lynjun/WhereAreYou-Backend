package com.example.whereareyou.service;

import com.example.whereareyou.domain.Member;
import com.example.whereareyou.domain.MemberInfo;
import com.example.whereareyou.exception.customexception.UpdateQueryException;
import com.example.whereareyou.exception.customexception.UserNotFoundException;
import com.example.whereareyou.repository.MemberInfoRepository;
import com.example.whereareyou.repository.MemberRepository;
import com.example.whereareyou.vo.request.memberInfo.RequestMemberInfo;
import com.example.whereareyou.vo.response.memberInfo.ResponseMemberInfo;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

/**
 * packageName    : com.example.whereareyou.service
 * fileName       : MemberInfoService
 * author         : pjh57
 * date           : 2023-10-11
 * description    : 위도 경도
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023-10-11        pjh57       최초 생성
 */
@Transactional
@Service
public class MemberInfoService {
    private final MemberRepository memberRepository;
    private final MemberInfoRepository memberInfoRepository;

    @Autowired
    public MemberInfoService(MemberRepository memberRepository, MemberInfoRepository memberInfoRepository) {
        this.memberRepository = memberRepository;
        this.memberInfoRepository = memberInfoRepository;
    }

    public void setMemberInfo(RequestMemberInfo requestMemberInfo){
        /*
         예외처리
         404 UserNotFoundException: memberId Not Found
         401: Unauthorized (추후에 추가할 예정)
         500 updateQueryException: update Fail
         500: Server
        */

        memberRepository.findById(requestMemberInfo.getMemberId())
                .orElseThrow(() -> new UserNotFoundException("존재하지 않는 memberId입니다."));

        MemberInfo existingMemberInfo
                = memberInfoRepository.findByMemberId(requestMemberInfo.getMemberId()).orElse(null);

        if(existingMemberInfo != null) {
            int update = memberInfoRepository.updateMemberInfoByMemberId(
                    requestMemberInfo.getLatitude(),
                    requestMemberInfo.getLongitude(),
                    requestMemberInfo.getMemberId()
            );

            if(update == 0)
                throw new UpdateQueryException("업데이트 실패");

        } else{
            MemberInfo memberInfo = MemberInfo.builder()
                    .memberId(requestMemberInfo.getMemberId())
                    .latitude(requestMemberInfo.getLatitude())
                    .longitude(requestMemberInfo.getLongitude())
                    .build();

            memberInfoRepository.save(memberInfo);
        }
    }

    /**
     * Get member info response member info.
     *
     * @param memberId the member id
     * @return the response member info
     */
    public ResponseMemberInfo getMemberInfo(String memberId){
        /*
         예외처리
         404 UserNotFoundException: memberId Not Found
         401: Unauthorized (추후에 추가할 예정)
         500: Server
        */
        memberRepository.findById(memberId)
                .orElseThrow(() -> new UserNotFoundException("존재하지 않는 memberId입니다."));

        MemberInfo memberInfo = memberInfoRepository.findByMemberId(memberId)
                .orElseThrow(() -> new UserNotFoundException("존재하지 않는 memberId입니다."));

        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        return mapper.map(memberInfo, ResponseMemberInfo.class);
    }
}
