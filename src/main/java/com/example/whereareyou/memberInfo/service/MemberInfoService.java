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
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static com.example.whereareyou.global.constant.ExceptionConstant.*;
import static com.example.whereareyou.schedule.constant.ScheduleConstant.ZERO;

@Transactional
@Service
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

    /**
     * Set member info.
     *
     * @param requestMemberInfo the request member info
     */
    public void setMemberInfo(RequestMemberInfo requestMemberInfo) {
        checkMember(requestMemberInfo.getMemberId());

        MemberInfo existingMemberInfo = returnMemberInfoByMemberId(requestMemberInfo.getMemberId());

        if (existingMemberInfo != null) {
            updateMemberInfo(requestMemberInfo);
        } else {
            saveMemberInfo(requestMemberInfo);
        }
    }

    private void checkMember(String memberId) {
        memberRepository.findById(memberId)
                .orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND_EXCEPTION_MESSAGE));
    }

    private MemberInfo returnMemberInfoByMemberId(String memberId) {
        return memberInfoRepository.findByMemberId(memberId).orElse(null);
    }

    private void updateMemberInfo(RequestMemberInfo requestMemberInfo) {
        int update = memberInfoRepository.updateMemberInfoByMemberId(
                requestMemberInfo.getLatitude(),
                requestMemberInfo.getLongitude(),
                requestMemberInfo.getMemberId()
        );

        if (update == ZERO) {
            throw new UpdateQueryException("업데이트 실패");
        }
    }

    private void saveMemberInfo(RequestMemberInfo requestMemberInfo) {
        MemberInfo memberInfo = MemberInfo.builder()
                .memberId(requestMemberInfo.getMemberId())
                .latitude(requestMemberInfo.getLatitude())
                .longitude(requestMemberInfo.getLongitude())
                .build();

        memberInfoRepository.save(memberInfo);
    }

    /**
     * Gets member infos.
     *
     * @param requestGetMemberInfo the request get member info
     * @return the member infos
     */
    public List<ResponseMemberInfo> getMemberInfos(RequestGetMemberInfo requestGetMemberInfo) {
        Schedule schedule = returnSchedule(requestGetMemberInfo.getScheduleId());

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime oneHoursBeforeAppointmentTime = schedule.getAppointmentTime().minusHours(1);
        LocalDateTime oneHoursAfterAppointmentTime = schedule.getAppointmentTime().plusHours(1);

        checkRequestTimeWithScheduleTime(now, oneHoursBeforeAppointmentTime, oneHoursAfterAppointmentTime);

        List<MemberInfo> membersInfo = returnMemberInfos(requestGetMemberInfo);

        return returnResponseMemberInfo(membersInfo);
    }

    private Schedule returnSchedule(String scheduleId) {
        return scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new ScheduleNotFoundException(SCHEDULE_NOT_FOUND_EXCEPTION_MESSAGE));
    }

    private void checkRequestTimeWithScheduleTime(LocalDateTime now, LocalDateTime oneHoursBeforeAppointmentTime, LocalDateTime oneHoursAfterAppointmentTime) {
        if (now.isBefore(oneHoursBeforeAppointmentTime) || now.isAfter(oneHoursAfterAppointmentTime)) {
            throw new InvalidRequestTimeException(INVALID_REQUEST_TIME_EXCEPTION_MESSAGE);
        }
    }

    private List<MemberInfo> returnMemberInfos(RequestGetMemberInfo requestGetMemberInfo) {
        return memberInfoRepository.findByMemberIds(requestGetMemberInfo.getMemberId());
    }

    private List<ResponseMemberInfo> returnResponseMemberInfo(List<MemberInfo> membersInfo) {
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        return membersInfo.stream()
                .map(memberInfo -> mapper.map(memberInfo, ResponseMemberInfo.class))
                .collect(Collectors.toList());
    }
}
