package com.example.whereareyou.service;

import com.example.whereareyou.domain.Member;
import com.example.whereareyou.domain.MemberSchedule;
import com.example.whereareyou.domain.Schedule;
import com.example.whereareyou.exception.customexception.FriendListNotFoundException;
import com.example.whereareyou.exception.customexception.MemberIdCannotBeInFriendListException;
import com.example.whereareyou.exception.customexception.UserNotFoundException;
import com.example.whereareyou.repository.MemberRepository;
import com.example.whereareyou.repository.MemberScheduleRepository;
import com.example.whereareyou.repository.ScheduleRepository;
import com.example.whereareyou.vo.request.schedule.RequestSaveSchedule;
import com.example.whereareyou.vo.response.schedule.ResponseSaveSchedule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * packageName    : project.whereareyou.service
 * fileName       : ScheduleService
 * author         : pjh57
 * date           : 2023-09-16
 * description    : 일정 관련 비즈니스 로직
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023-09-16        pjh57       최초 생성
 */
@Service
@Transactional
public class ScheduleService {
    private final MemberScheduleRepository memberScheduleRepository;
    private final ScheduleRepository scheduleRepository;
    private final MemberRepository memberRepository;

    /**
     * Instantiates a new Schedule service.
     *
     * @param memberScheduleRepository the member schedule repository
     * @param scheduleRepository       the schedule repository
     * @param memberRepository         the member repository
     */
    @Autowired
    public ScheduleService(MemberScheduleRepository memberScheduleRepository, ScheduleRepository scheduleRepository, MemberRepository memberRepository) {
        this.memberScheduleRepository = memberScheduleRepository;
        this.scheduleRepository = scheduleRepository;
        this.memberRepository = memberRepository;
    }

    /**
     * Save response save schedule.
     *
     * @param requestSaveSchedule the request save schedule
     * @return the response save schedule
     */
    public ResponseSaveSchedule save(RequestSaveSchedule requestSaveSchedule) {
        /*
         예외처리
         404 UserNotFoundException: MemberId Not Found
         400 FriendListNotFoundException: FriendListNot Found
         400 MemberIdCannotBeInFriendListException: FriendList have creatorId
         401: Unauthorized (추후에 추가할 예정)
         500: Server
        */
        Member creator = memberRepository.findById(requestSaveSchedule.getMemberId())
                .orElseThrow(() -> new UserNotFoundException("존재하지 않는 memberId입니다."));

        List<String> friendList = requestSaveSchedule.getMemberIdList();
        if(friendList == null || friendList.isEmpty())
            throw new FriendListNotFoundException("일정 추가 시 친구 설정은 필수 입니다.");

        if(friendList.contains(creator.getId()))
            throw new MemberIdCannotBeInFriendListException("일정 친구 추가 시 본인의 ID는 들어갈 수 없습니다.");

        List<Member> friends = memberRepository.findAllById(friendList);
        if(friends.size() != friendList.size()) {
            throw new UserNotFoundException("존재하지 않는 memberId입니다.");
        }

        // Schedule Entity 생성
        Schedule schedule = Schedule.builder()
                .start(requestSaveSchedule.getStart())
                .end(requestSaveSchedule.getEnd())
                .title(requestSaveSchedule.getTitle())
                .place(requestSaveSchedule.getPlace())
                .memo(requestSaveSchedule.getMemo())
                .closed(false)
                .creator(creator)
                .build();
        // Schedule 저장
        scheduleRepository.save(schedule);


        // friends 리스트를 사용하여 MemberSchedule 엔터티 생성 및 저장
        List<MemberSchedule> memberSchedules = friends.stream()
                .map(friend -> MemberSchedule.builder()
                        .schedule(schedule)
                        .member(friend)
                        .accept(false)
                        .build())
                .collect(Collectors.toList());
        // MemberSchedule 저장
        memberScheduleRepository.saveAll(memberSchedules);

        // ResponseSaveSchedule 생성
        ResponseSaveSchedule responseSaveSchedule = new ResponseSaveSchedule();
        responseSaveSchedule.setScheduleId(schedule.getId());

        return responseSaveSchedule;
    }
}