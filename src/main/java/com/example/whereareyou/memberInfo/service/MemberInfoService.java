package com.example.whereareyou.memberInfo.service;

import com.example.whereareyou.memberInfo.domain.MemberInfo;
import com.example.whereareyou.memberInfo.exception.InvalidRequestTimeException;
import com.example.whereareyou.memberInfo.request.RequestGetMemberInfo;
import com.example.whereareyou.schedule.domain.Schedule;
import com.example.whereareyou.schedule.exception.ScheduleNotFoundException;
import com.example.whereareyou.schedule.exception.UpdateQueryException;
import com.example.whereareyou.member.exception.UserNotFoundException;
import com.example.whereareyou.memberInfo.repository.MemberInfoRepository;
import com.example.whereareyou.member.repository.MemberRepository;
import com.example.whereareyou.memberInfo.request.RequestMemberInfo;
import com.example.whereareyou.memberInfo.response.ResponseMemberInfo;
import com.example.whereareyou.schedule.repository.ScheduleRepository;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

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
@Slf4j
public class MemberInfoService {
    private final MemberRepository memberRepository;
    private final MemberInfoRepository memberInfoRepository;
    private final ScheduleRepository scheduleRepository;

    @Autowired
    public MemberInfoService(MemberRepository memberRepository, MemberInfoRepository memberInfoRepository, ScheduleRepository scheduleRepository) {
        this.memberRepository = memberRepository;
        this.memberInfoRepository = memberInfoRepository;
        this.scheduleRepository = scheduleRepository;
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
     * Gets member infos.
     *
     * @param requestGetMemberInfo the request get member info
     * @return the member infos
     */
    public List<ResponseMemberInfo> getMemberInfos(RequestGetMemberInfo requestGetMemberInfo) {
        // 스케줄 유효성 검사
        Schedule schedule = scheduleRepository.findById(requestGetMemberInfo.getScheduleId())
                .orElseThrow(() -> new ScheduleNotFoundException("존재하지 않는 scheduleId입니다."));

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime twoHoursBeforeStart = schedule.getStart().minusHours(2);
        LocalDateTime scheduleEnd = schedule.getEnd();

        if (now.isBefore(twoHoursBeforeStart) || now.isAfter(scheduleEnd)) {
            throw new InvalidRequestTimeException("요청 시간이 유효한 범위에 있지 않습니다.");
        }

        // 멤버 정보 조회 및 변환
        List<MemberInfo> membersInfo = memberInfoRepository.findByMemberIds(requestGetMemberInfo.getMemberId());
        if (membersInfo.size() != requestGetMemberInfo.getMemberId().size()) {
            throw new UserNotFoundException("하나 이상의 memberId가 존재하지 않습니다.");
        }

        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        return membersInfo.stream()
                .map(memberInfo -> mapper.map(memberInfo, ResponseMemberInfo.class))
                .collect(Collectors.toList());
    }
}
