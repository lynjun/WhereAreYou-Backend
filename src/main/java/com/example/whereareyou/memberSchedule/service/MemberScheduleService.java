package com.example.whereareyou.memberSchedule.service;

import com.example.whereareyou.global.domain.FcmToken;
import com.example.whereareyou.global.service.FcmTokenService;
import com.example.whereareyou.global.service.FirebaseCloudMessageService;
import com.example.whereareyou.member.domain.Member;
import com.example.whereareyou.member.exception.UserNotFoundException;
import com.example.whereareyou.member.repository.MemberRepository;
import com.example.whereareyou.memberSchedule.domain.MemberSchedule;
import com.example.whereareyou.memberSchedule.exception.CreatorCannotRefuseSchedule;
import com.example.whereareyou.memberSchedule.repository.MemberScheduleRepository;
import com.example.whereareyou.memberSchedule.request.RequestModifyMemberSchedule;
import com.example.whereareyou.memberSchedule.request.RequestRefuseSchedule;
import com.example.whereareyou.schedule.domain.Schedule;
import com.example.whereareyou.schedule.dto.ScheduleInviteDto;
import com.example.whereareyou.schedule.exception.ScheduleNotFoundException;
import com.example.whereareyou.schedule.exception.UpdateQueryException;
import com.example.whereareyou.schedule.repository.ScheduleRepository;
import com.example.whereareyou.schedule.request.RequestScheduleAccept;
import com.example.whereareyou.schedule.response.ResponseGroupInvite;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import static com.example.whereareyou.global.constant.ExceptionConstant.SCHEDULE_CREATOR_MISMATCH_EXCEPTION_MESSAGE;
import static com.example.whereareyou.schedule.constant.ScheduleConstant.*;

/**
 * packageName    : com.example.whereareyou.memberSchedule.service
 * fileName       : MemberScheduleService
 * author         : pjh57
 * date           : 2023-11-05
 * description    : MemberSchedule Service
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023-11-05        pjh57       최초 생성
 */
@Service
@Transactional
@Slf4j
public class MemberScheduleService {
    private final MemberRepository memberRepository;

    private final ScheduleRepository scheduleRepository;

    private final MemberScheduleRepository memberScheduleRepository;
    private final FcmTokenService fcmTokenService;
    private final FirebaseCloudMessageService firebaseCloudMessageService;

    @Autowired
    public MemberScheduleService(MemberRepository memberRepository,
                                 ScheduleRepository scheduleRepository,
                                 MemberScheduleRepository memberScheduleRepository,
                                 FcmTokenService fcmTokenService,
                                 FirebaseCloudMessageService firebaseCloudMessageService) {
        this.memberRepository = memberRepository;
        this.scheduleRepository = scheduleRepository;
        this.memberScheduleRepository = memberScheduleRepository;
        this.fcmTokenService = fcmTokenService;
        this.firebaseCloudMessageService = firebaseCloudMessageService;
    }

    /**
     * Modify member schedule.
     *
     * @param requestModifyMemberSchedule the request modify member schedule
     */
    public void modifyMemberSchedule(RequestModifyMemberSchedule requestModifyMemberSchedule) {
        Member creator = returnMember(requestModifyMemberSchedule.getCreatorId());
        Schedule schedule = returnSchedule(requestModifyMemberSchedule.getScheduleId());

        checkScheduleCreatedByCreator(schedule, creator);

        // 현재 MemberSchedule 목록 가져오기
        List<MemberSchedule> currentMemberSchedules = memberScheduleRepository.findMemberSchedulesBySchedule(schedule);

        // 요청된 friendId 목록
        Set<String> requestedFriendIds = new HashSet<>(requestModifyMemberSchedule.getFriendId());

        // 현재 MemberSchedule에 있는 멤버 ID 추출
        Set<String> currentMemberIds = returnMemberIdFromMemberSchedule(currentMemberSchedules);

        // 요청된 멤버들 중 현재 MemberSchedule에 없는 멤버 추가
        // 푸시알림을 추가적으로 날립니다.
        addToMemberScheduleNotInMemberSchedule(requestedFriendIds, currentMemberIds, creator, schedule);

        // 요청에 없는 현재 MemberSchedule 삭제
        // 여기서 creator의 MemberSchedule이 삭제되지 않도록 예외 처리
        deleteCurrentMemberScheduleNotInRequest(currentMemberSchedules, requestedFriendIds, creator);
    }

    private Member returnMember(String memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new UserNotFoundException("존재하지 않는 memberId입니다."));
    }

    private Schedule returnSchedule(String scheduleId) {
        return scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new ScheduleNotFoundException("존재하지 않는 scheduleId입니다."));
    }

    private void checkScheduleCreatedByCreator(Schedule schedule, Member creator) {
        if (!schedule.getCreator().getId().equals(creator.getId()))
            throw new ScheduleNotFoundException(SCHEDULE_CREATOR_MISMATCH_EXCEPTION_MESSAGE);
    }

    private Set<String> returnMemberIdFromMemberSchedule(List<MemberSchedule> currentMemberSchedules) {
        return currentMemberSchedules.stream()
                .map(memberSchedule -> memberSchedule.getMember().getId())
                .collect(Collectors.toSet());
    }

    private void addToMemberScheduleNotInMemberSchedule(Set<String> requestedFriendIds, Set<String> currentMemberIds, Member creator, Schedule schedule) {
        List<Member> newFriends = new ArrayList<>();

        requestedFriendIds.stream()
                .filter(friendId -> !currentMemberIds.contains(friendId) && !friendId.equals(creator.getId()))
                .forEach(friendId -> {
                    Optional<Member> memberOpt = memberRepository.findById(friendId);
                    if (memberOpt.isPresent()) {
                        Member member = memberOpt.get();
                        MemberSchedule newMemberSchedule = MemberSchedule.builder()
                                .member(member)
                                .schedule(schedule)
                                .accept(false)
                                .arrived(false)
                                .build();
                        memberScheduleRepository.save(newMemberSchedule);

                        newFriends.add(member); // 새로운 멤버를 newFriends 리스트에 추가
                    }
                });

        makeFcmMessage(newFriends, creator);
    }

    private void makeFcmMessage(List<Member> friends, Member creator) {
        friends.stream()
                .filter(friend -> !friend.equals(creator))
                .forEach(friend -> {
                    Optional<FcmToken> fcmTokenOpt = fcmTokenService.getTokenByMemberId(friend.getId());
                    fcmTokenOpt.ifPresent(token -> {
                        String body = creator.getUserName() + FCM_MESSAGE_FROM + friend.getUserName() + FCM_MESSAGE_TO;
                        try {
                            firebaseCloudMessageService.sendMessageTo(token.getTargetToken(), FCM_MESSAGE_TITLE, body);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    });
                });
    }

    private void deleteCurrentMemberScheduleNotInRequest(List<MemberSchedule> currentMemberSchedules, Set<String> requestedFriendIds, Member creator) {
        currentMemberSchedules.stream()
                .filter(memberSchedule -> !requestedFriendIds.contains(memberSchedule.getMember().getId())
                        && !memberSchedule.getMember().equals(creator))
                .forEach(memberScheduleRepository::delete);
    }

    /**
     * Schedule accept.
     *
     * @param requestScheduleAccept the request schedule accept
     */
    public void scheduleAccept(RequestScheduleAccept requestScheduleAccept){

        Member acceptMember = memberRepository.findById(requestScheduleAccept.getAcceptMemberId())
                .orElseThrow(() -> new UserNotFoundException("존재하지 않는 memberId입니다."));
        Schedule findSchedule = scheduleRepository.findById(requestScheduleAccept.getScheduleId())
                .orElseThrow(() -> new ScheduleNotFoundException("존재하지 않는 scheduleId입니다."));

        int updateCnt
                = memberScheduleRepository.setAcceptTrueForMemberAndSchedule(acceptMember.getId(), findSchedule.getId());
        if(updateCnt == 0)
            throw new UpdateQueryException("업데이트 실패");
    }

    /**
     * Refuse schedule.
     *
     * @param requestRefuseSchedule the request refuse schedule
     */
    public void refuseSchedule(RequestRefuseSchedule requestRefuseSchedule){
        // 거부하는 멤버 찾기
        Member refuseMember = memberRepository.findById(requestRefuseSchedule.getRefuseMemberId())
                .orElseThrow(() -> new UserNotFoundException("존재하지 않는 memberId입니다."));

        // 거부 대상 스케줄 찾기
        Schedule findSchedule = scheduleRepository.findById(requestRefuseSchedule.getScheduleId())
                .orElseThrow(() -> new ScheduleNotFoundException("존재하지 않는 scheduleId입니다."));

        // 스케줄의 생성자인 경우 거부 불가
        if(findSchedule.getCreator().equals(refuseMember)) {
            throw new CreatorCannotRefuseSchedule("스케줄의 생성자는 스케줄을 거부할 수 없습니다.");
        }

        // 해당 MemberSchedule 찾기 및 삭제
        memberScheduleRepository.findByMemberAndSchedule(refuseMember, findSchedule)
                .ifPresent(memberScheduleRepository::delete);
    }

    public ResponseGroupInvite getScheduleInvite(String memberId){
        Optional<Member> byId = memberRepository.findById(memberId);
        Member member = byId.orElseThrow(() -> new UserNotFoundException("존재하지 않는 userId 입니다."));

        List<String> scheduleId = memberScheduleRepository.findByMemberAndAcceptIsFalse(member);

        return getScheduleInvite(scheduleId);
    }

    private ResponseGroupInvite getScheduleInvite(List<String> scheduleId){

        List<Schedule> byId = scheduleRepository.findAllById(scheduleId);

        ResponseGroupInvite responseGroupInvite = new ResponseGroupInvite();
        responseGroupInvite.setTestList(new ArrayList<>());

        byId.forEach(schedule -> {
            ScheduleInviteDto scheduleInviteDto = new ScheduleInviteDto();
            scheduleInviteDto.setScheduleId(schedule.getId());
            scheduleInviteDto.setTitle(schedule.getTitle());
            scheduleInviteDto.setUserName(schedule.getCreator().getUserName());
            scheduleInviteDto.setStart(schedule.getStart());
            responseGroupInvite.getTestList().add(scheduleInviteDto);
        });

        return responseGroupInvite;
    }
}
