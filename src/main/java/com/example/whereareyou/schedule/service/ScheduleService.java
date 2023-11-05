package com.example.whereareyou.schedule.service;

import com.example.whereareyou.member.domain.Member;
import com.example.whereareyou.member.exception.MemberIdCannotBeInFriendListException;
import com.example.whereareyou.member.exception.UserNotFoundException;
import com.example.whereareyou.memberSchedule.domain.MemberSchedule;
import com.example.whereareyou.schedule.domain.Schedule;
import com.example.whereareyou.schedule.dto.BriefDateScheduleDTO;
import com.example.whereareyou.schedule.dto.MonthlyScheduleResponseDTO;
import com.example.whereareyou.member.repository.MemberRepository;
import com.example.whereareyou.memberSchedule.repository.MemberScheduleRepository;
import com.example.whereareyou.schedule.exception.*;
import com.example.whereareyou.schedule.repository.ScheduleRepository;
import com.example.whereareyou.schedule.request.*;
import com.example.whereareyou.schedule.response.ResponseBriefDateSchedule;
import com.example.whereareyou.schedule.response.ResponseDetailSchedule;
import com.example.whereareyou.schedule.response.ResponseMonthlySchedule;
import com.example.whereareyou.schedule.response.ResponseSaveSchedule;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
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
        if (friendList == null || friendList.isEmpty())
            throw new FriendListNotFoundException("일정 추가 시 친구 설정은 필수 입니다.");

        if (friendList.contains(creator.getId()))
            throw new MemberIdCannotBeInFriendListException("일정 친구 추가 시 본인의 ID는 들어갈 수 없습니다.");

        List<Member> friends = memberRepository.findAllById(friendList);
        if (friends.size() != friendList.size()) {
            throw new UserNotFoundException("존재하지 않는 memberId입니다.");
        }

        friends.add(creator);

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
                        .accept(friend.equals(creator)) // creator에 대해서만 accept를 true로 설정
                        .arrived(false)
                        .build())
                .collect(Collectors.toList());
        // MemberSchedule 저장
        memberScheduleRepository.saveAll(memberSchedules);

        // ResponseSaveSchedule 생성
        ResponseSaveSchedule responseSaveSchedule = new ResponseSaveSchedule();
        responseSaveSchedule.setScheduleId(schedule.getId());

        return responseSaveSchedule;
    }

    /**
     * Modify schedule.
     *
     * @param requestModifySchedule the request modify schedule
     */
    public void modifySchedule(RequestModifySchedule requestModifySchedule) {
        /*
         예외처리
         404 ScheduleNotFoundException: ScheduleId Not Found
         404 UserNotFoundException: MemberId Not Found
         400 FriendListNotFoundException: FriendListNot Found
         400 MemberIdCannotBeInFriendListException: FriendList have creatorId
         401: Unauthorized (추후에 추가할 예정)
         500 updateQueryException: update Fail
         500: Server
        */
        Member creator = memberRepository.findById(requestModifySchedule.getCreatorId())
                .orElseThrow(() -> new UserNotFoundException("존재하지 않는 memberId입니다."));
        Schedule savedSchedule = scheduleRepository.findById(requestModifySchedule.getScheduleId())
                .orElseThrow(() -> new ScheduleNotFoundException("존재하지 않는 scheduleId입니다."));

        if(!savedSchedule.getCreator().getId().equals(creator.getId()))
            throw new ScheduleNotFoundException("해당 schedule의 creator가 아닙니다.");

        // Schedule 변경
        int updatedCount = scheduleRepository.updateSchedule(
                requestModifySchedule.getStart(),
                requestModifySchedule.getEnd(),
                requestModifySchedule.getTitle(),
                requestModifySchedule.getPlace(),
                requestModifySchedule.getMemo(),
                savedSchedule.getClosed(),
                creator,
                savedSchedule.getId()
        );

        if (updatedCount == 0)
            throw new UpdateQueryException("업데이트 실패");
    }

    /**
     * Get month schedule response monthly schedule.
     *
     * @param memberId the member id
     * @param year     the year
     * @param month    the month
     * @return the response monthly schedule
     */
    public ResponseMonthlySchedule getMonthSchedule(String memberId, Integer year, Integer month){
        // 사용자 확인
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new UserNotFoundException("존재하지 않는 memberId입니다."));

        // 유효한 월 확인
        if(month < 1 || month > 12)
            throw new InvalidYearOrMonthOrDateException("월이 올바르지 않습니다.");

        // 해당 Member가 수락한 MemberSchedule 가져오기
        List<MemberSchedule> acceptedMemberSchedules = memberScheduleRepository.findByMemberAndAcceptIsTrue(member);

        // Response 객체 생성
        ResponseMonthlySchedule responseMonthlySchedule = new ResponseMonthlySchedule();
        responseMonthlySchedule.setYear(year);
        responseMonthlySchedule.setMonth(month);
        responseMonthlySchedule.setSchedules(new ArrayList<>());

        // 월별 최대 일수 계산
        YearMonth yearMonthObject = YearMonth.of(year, month);
        int daysInMonth = yearMonthObject.lengthOfMonth();

        // 일별로 수락된 스케줄 확인
        for (int day = 1; day <= daysInMonth; day++) {
            MonthlyScheduleResponseDTO monthlyScheduleResponseDTO = new MonthlyScheduleResponseDTO();
            monthlyScheduleResponseDTO.setDate(day);

            int finalDay = day;
            List<Schedule> schedulesForTheDay = acceptedMemberSchedules.stream()
                    .map(MemberSchedule::getSchedule)
                    .filter(schedule -> {
                        LocalDate currentDate = LocalDate.of(year, month, finalDay);
                        LocalDate scheduleStartDate = schedule.getStart().toLocalDate();
                        LocalDate scheduleEndDate = schedule.getEnd().toLocalDate();
                        return (currentDate.isEqual(scheduleStartDate) || currentDate.isAfter(scheduleStartDate)) &&
                                (currentDate.isEqual(scheduleEndDate) || currentDate.isBefore(scheduleEndDate));
                    })
                    .collect(Collectors.toList());

            monthlyScheduleResponseDTO.setHasSchedule(!schedulesForTheDay.isEmpty());
            monthlyScheduleResponseDTO.setAmountSchedule(schedulesForTheDay.size());
            responseMonthlySchedule.getSchedules().add(monthlyScheduleResponseDTO);
        }

        return responseMonthlySchedule;
    }

    /**
     * Delete schedule.
     *
     * @param requestDeleteSchedule the request delete schedule
     */
    public void deleteSchedule(RequestDeleteSchedule requestDeleteSchedule){
        /*
         예외처리
         404: ScheduleId Not Found
         401: Unauthorized (추후에 추가할 예정)
         500: Server
        */
        Schedule schedule = scheduleRepository.findById(requestDeleteSchedule.getScheduleId())
                .orElseThrow(() -> new ScheduleNotFoundException("존재하지 않는 scheduleId입니다."));

        memberScheduleRepository.deleteAllBySchedule(schedule);
        scheduleRepository.deleteById(requestDeleteSchedule.getScheduleId());
    }

    /**
     * Get brief date schedule response brief date schedule.
     *
     * @param memberId the member id
     * @param year     the year
     * @param month    the month
     * @param date     the date
     * @return the response brief date schedule
     */
    public ResponseBriefDateSchedule getBriefDateSchedule(String memberId, Integer year, Integer month, Integer date) {
        // 사용자 확인
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new UserNotFoundException("존재하지 않는 memberId입니다."));

        // 해당 Member가 수락한 MemberSchedule 가져오기
        List<MemberSchedule> acceptedMemberSchedules = memberScheduleRepository.findByMemberAndAcceptIsTrue(member);

        // Response 객체 생성
        ResponseBriefDateSchedule responseBriefDateSchedule = new ResponseBriefDateSchedule();
        responseBriefDateSchedule.setBriefDateScheduleDTOList(new ArrayList<>());

        // 주어진 날짜 객체 생성
        LocalDate givenDate = LocalDate.of(year, month, date);

        // 수락된 스케줄 중 입력된 날짜가 스케줄 기간 안에 있는지 확인
        for (MemberSchedule memberSchedule : acceptedMemberSchedules) {
            Schedule schedule = memberSchedule.getSchedule();
            LocalDate startDate = schedule.getStart().toLocalDate();
            LocalDate endDate = schedule.getEnd().toLocalDate();

            // 입력된 날짜가 스케줄 기간 안에 있는 경우
            if (!givenDate.isBefore(startDate) && !givenDate.isAfter(endDate)) {
                BriefDateScheduleDTO briefDateScheduleDTO = new BriefDateScheduleDTO();
                briefDateScheduleDTO.setScheduleId(schedule.getId());
                briefDateScheduleDTO.setTitle(schedule.getTitle());
                briefDateScheduleDTO.setStart(schedule.getStart());
                briefDateScheduleDTO.setEnd(schedule.getEnd());
                responseBriefDateSchedule.getBriefDateScheduleDTOList().add(briefDateScheduleDTO);
            }
        }

        return responseBriefDateSchedule;
    }

    /**
     * Get detail schedule response detail schedule.
     *
     * @param memberId   the member id
     * @param scheduleId the schedule id
     * @return the response detail schedule
     */
    public ResponseDetailSchedule getDetailSchedule(String memberId, String scheduleId) {
        // 사용자 확인
        Member findMember = memberRepository.findById(memberId)
                .orElseThrow(() -> new UserNotFoundException("존재하지 않는 memberId입니다."));

        // 해당 Member가 수락한 MemberSchedule 중 특정 Schedule 찾기
        MemberSchedule acceptedMemberSchedule = memberScheduleRepository.findByMemberAndScheduleIdAndAcceptIsTrue(findMember, scheduleId)
                .orElseThrow(() -> new ScheduleNotFoundException("존재하지 않는 scheduleId이거나 회원이 수락하지 않은 일정입니다."));

        Schedule findSchedule = acceptedMemberSchedule.getSchedule();

        // 친구의 ID 목록 추출 (수락한 친구들만 포함)
        List<String> friendsIdList = findSchedule.getMemberScheduleList().stream()
                .filter(MemberSchedule::getAccept)
                .map(memberSchedule -> memberSchedule.getMember().getId())
                .collect(Collectors.toList());

        // ResponseDetailSchedule 객체 반환
        return ResponseDetailSchedule.builder()
                .creatorId(findSchedule.getCreator().getId())
                .start(findSchedule.getStart())
                .end(findSchedule.getEnd())
                .title(findSchedule.getTitle())
                .place(findSchedule.getPlace())
                .memo(findSchedule.getMemo())
                .friendsIdListDTO(friendsIdList)
                .build();
    }

    /**
     * Schedule accept.
     *
     * @param requestScheduleAccept the request schedule accept
     */
    public void scheduleAccept(RequestScheduleAccept requestScheduleAccept){
        /*
         예외처리
         404 UserNotFoundException: MemberId Not Found
         404 ScheduleNotFoundException: scheduleId Not Found
         401: Unauthorized (추후에 추가할 예정)
         500 updateQueryException: update Fail
         500: Server
        */
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
     * Schedule closed.
     *
     * @param requestScheduleClosed the request schedule closed
     */
    public void scheduleClosed(RequestScheduleClosed requestScheduleClosed){
        /*
         예외처리
         404 ScheduleNotFoundException: scheduleId Not Found
         400 NotCreatedScheduleByMemberException: This is not a user-created schedule
         401: Unauthorized (추후에 추가할 예정)
         500 updateQueryException: update Fail
         500: Server
        */
        Schedule findSchedule = scheduleRepository.findById(requestScheduleClosed.getScheduleId())
                .orElseThrow(() -> new ScheduleNotFoundException("존재하지 않는 scheduleId입니다."));
        Member creator = memberRepository.findById(requestScheduleClosed.getCreatorId())
                .orElseThrow(() -> new UserNotFoundException("존재하지 않는 memberId입니다."));

        if(!findSchedule.getCreator().getId().equals(creator.getId()))
            throw new NotCreatedScheduleByMemberException("회원이 만든 일정이 아닙니다.");

        int updateCnt = scheduleRepository.closeSchedule(findSchedule.getId());
        if(updateCnt == 0)
            throw new UpdateQueryException("업데이트 실패");
    }

    /**
     * Schedule arrived.
     *
     * @param requestScheduleArrived the request schedule arrived
     */
    public void scheduleArrived(RequestScheduleArrived requestScheduleArrived){
        /*
         예외처리
         404 ScheduleNotFoundException: scheduleId Not Found
         400 NotCreatedScheduleByMemberException: This is not a user-created schedule
         401: Unauthorized (추후에 추가할 예정)
         500 updateQueryException: update Fail
         500: Server
        */
        Member arrivedMember = memberRepository.findById(requestScheduleArrived.getArrivedMemberId())
                .orElseThrow(() -> new UserNotFoundException("존재하지 않는 memberId입니다."));
        Schedule findSchedule = scheduleRepository.findById(requestScheduleArrived.getScheduleId())
                .orElseThrow(() -> new ScheduleNotFoundException("존재하지 않는 scheduleId입니다."));
        boolean isMemberPartOfSchedule = findSchedule.getMemberScheduleList()
                .stream()
                .anyMatch(ms -> ms.getMember().getId().equals(arrivedMember.getId()));
        if (!isMemberPartOfSchedule) {
            throw new UserNotFoundException("해당 Member는 Schedule에 존재하지 않습니다.");
        }

        int update = memberScheduleRepository.setArrivedTrue(arrivedMember.getId(), findSchedule.getId());
        if(update == 0)
            throw new UpdateQueryException("업데이트 실패");
    }
}