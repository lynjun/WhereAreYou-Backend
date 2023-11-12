package com.example.whereareyou.memberSchedule.service;

import com.example.whereareyou.member.domain.Member;
import com.example.whereareyou.member.exception.UserNotFoundException;
import com.example.whereareyou.member.repository.MemberRepository;
import com.example.whereareyou.memberSchedule.domain.MemberSchedule;
import com.example.whereareyou.memberSchedule.exception.CreatorCannotRefuseSchedule;
import com.example.whereareyou.memberSchedule.repository.MemberScheduleRepository;
import com.example.whereareyou.memberSchedule.request.RequestModifyMemberSchedule;
import com.example.whereareyou.memberSchedule.request.RequestRefuseSchedule;
import com.example.whereareyou.schedule.domain.Schedule;
import com.example.whereareyou.schedule.exception.ScheduleNotFoundException;
import com.example.whereareyou.schedule.exception.UpdateQueryException;
import com.example.whereareyou.schedule.repository.ScheduleRepository;
import com.example.whereareyou.schedule.request.RequestScheduleAccept;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

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

    @Autowired
    public MemberScheduleService(MemberRepository memberRepository,
                                 ScheduleRepository scheduleRepository,
                                 MemberScheduleRepository memberScheduleRepository) {
        this.memberRepository = memberRepository;
        this.scheduleRepository = scheduleRepository;
        this.memberScheduleRepository = memberScheduleRepository;
    }

    /**
     * Modify member schedule.
     *
     * @param requestModifyMemberSchedule the request modify member schedule
     */
    public void modifyMemberSchedule(RequestModifyMemberSchedule requestModifyMemberSchedule){
        // 스케줄과 생성자 정보 확인
        Member creator = memberRepository.findById(requestModifyMemberSchedule.getCreatorId())
                .orElseThrow(() -> new UserNotFoundException("존재하지 않는 memberId입니다."));

        Schedule schedule = scheduleRepository.findById(requestModifyMemberSchedule.getScheduleId())
                .orElseThrow(() -> new ScheduleNotFoundException("존재하지 않는 scheduleId입니다."));

        // 스케줄 생성자 확인
        if(!schedule.getCreator().equals(creator)) {
            throw new ScheduleNotFoundException("해당 스케줄을 수정할 권한이 없습니다.");
        }

        // 현재 MemberSchedule 목록 가져오기
        List<MemberSchedule> currentMemberSchedules = memberScheduleRepository.findMemberSchedulesBySchedule(schedule);

        // 요청된 friendId 목록
        Set<String> requestedFriendIds = new HashSet<>(requestModifyMemberSchedule.getFriendId());

        // 현재 MemberSchedule에 있는 멤버 ID 추출
        Set<String> currentMemberIds = currentMemberSchedules.stream()
                .map(memberSchedule -> memberSchedule.getMember().getId())
                .collect(Collectors.toSet());

        // 요청된 멤버들 중 현재 MemberSchedule에 없는 멤버 추가
        // 참고: 이 로직은 creator를 포함시키지 않으므로 creator의 MemberSchedule은 건드리지 않습니다.
        requestedFriendIds.stream()
                .filter(friendId -> !currentMemberIds.contains(friendId) && !friendId.equals(creator.getId()))
                .forEach(friendId -> {
                    Optional<Member> memberOpt = memberRepository.findById(friendId);
                    if (memberOpt.isPresent()) {
                        MemberSchedule newMemberSchedule = MemberSchedule.builder()
                                .member(memberOpt.get())
                                .schedule(schedule)
                                .accept(false) // 초대받은 상태이므로 false로 설정
                                .arrived(false)
                                .build();
                        memberScheduleRepository.save(newMemberSchedule);
                    }
                });

        // 요청에 없는 현재 MemberSchedule 삭제
        // 여기서 creator의 MemberSchedule이 삭제되지 않도록 예외 처리
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

}
