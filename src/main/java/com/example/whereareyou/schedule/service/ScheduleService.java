package com.example.whereareyou.schedule.service;

import com.example.whereareyou.global.domain.FcmToken;
import com.example.whereareyou.global.service.FcmTokenService;
import com.example.whereareyou.global.service.FirebaseCloudMessageService;
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
import com.example.whereareyou.schedule.response.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.example.whereareyou.global.constant.ExceptionConstant.*;
import static com.example.whereareyou.schedule.constant.ScheduleConstant.*;

@Service
@Transactional
public class ScheduleService {
    private final MemberScheduleRepository memberScheduleRepository;
    private final ScheduleRepository scheduleRepository;
    private final MemberRepository memberRepository;
    private final FirebaseCloudMessageService firebaseCloudMessageService;

    private final FcmTokenService fcmTokenService;

    /**
     * Instantiates a new Schedule service.
     *
     * @param memberScheduleRepository the member schedule repository
     * @param scheduleRepository       the schedule repository
     * @param memberRepository         the member repository
     */
    @Autowired
    public ScheduleService(MemberScheduleRepository memberScheduleRepository, ScheduleRepository scheduleRepository, MemberRepository memberRepository, FirebaseCloudMessageService firebaseCloudMessageService, FcmTokenService fcmTokenService) {
        this.memberScheduleRepository = memberScheduleRepository;
        this.scheduleRepository = scheduleRepository;
        this.memberRepository = memberRepository;
        this.firebaseCloudMessageService = firebaseCloudMessageService;
        this.fcmTokenService = fcmTokenService;
    }

    /**
     * Save Schedule And MemberSchedule
     *
     * @param requestSaveSchedule the request save schedule
     * @return the response save schedule
     */
    public ResponseSaveSchedule save(RequestSaveSchedule requestSaveSchedule) {
        Member creator = returnMember(requestSaveSchedule.getMemberId());
        List<Member> friends = returnScheduleFriends(creator, requestSaveSchedule);

        Schedule schedule = saveSchedule(requestSaveSchedule, creator);
        saveMemberSchedule(friends, schedule, creator);

        ResponseSaveSchedule responseSaveSchedule = setResponseSaveSchedule(schedule);

        makeFcmMessage(friends, creator);

        return responseSaveSchedule;
    }

    private Member returnMember(String memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND_EXCEPTION_MESSAGE));
    }

    private List<Member> returnScheduleFriends(Member creator, RequestSaveSchedule requestSaveSchedule) {
        List<String> friendList = requestSaveSchedule.getMemberIdList();

        if (friendList.contains(creator.getId()))
            throw new MemberIdCannotBeInFriendListException(MEMBER_ID_CANNOT_BE_IN_FRIEND_LIST);

        List<Member> friends = memberRepository.findAllById(friendList);
        if (friends.size() != friendList.size()) {
            throw new UserNotFoundException(USER_NOT_FOUND_EXCEPTION_MESSAGE);
        }

        friends.add(creator);

        return friends;
    }

    private Schedule saveSchedule(RequestSaveSchedule requestSaveSchedule, Member creator) {
        Schedule schedule = Schedule.builder()
                .start(requestSaveSchedule.getStart())
                .end(requestSaveSchedule.getEnd())
                .title(requestSaveSchedule.getTitle())
                .place(requestSaveSchedule.getPlace())
                .memo(requestSaveSchedule.getMemo())
                .destinationLatitude(requestSaveSchedule.getDestinationLatitude())
                .destinationLongitude(requestSaveSchedule.getDestinationLongitude())
                .creator(creator)
                .build();

        scheduleRepository.save(schedule);

        return schedule;
    }

    private void saveMemberSchedule(List<Member> friends, Schedule schedule, Member creator) {
        List<MemberSchedule> memberSchedules = friends.stream()
                .map(friend -> MemberSchedule.builder()
                        .schedule(schedule)
                        .member(friend)
                        .accept(friend.equals(creator))
                        .arrived(false)
                        .build())
                .collect(Collectors.toList());

        memberScheduleRepository.saveAll(memberSchedules);
    }

    private ResponseSaveSchedule setResponseSaveSchedule(Schedule schedule) {
        ResponseSaveSchedule responseSaveSchedule = new ResponseSaveSchedule();
        responseSaveSchedule.setScheduleId(schedule.getId());

        return responseSaveSchedule;
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
        Member creator = memberRepository.findById(requestDeleteSchedule.getCreatorId())
                .orElseThrow(() -> new UserNotFoundException("존재하지 않는 memberId입니다."));

        Schedule schedule = scheduleRepository.findById(requestDeleteSchedule.getScheduleId())
                .orElseThrow(() -> new ScheduleNotFoundException("존재하지 않는 scheduleId입니다."));

        if(!schedule.getCreator().getId().equals(creator.getId()))
            throw new ScheduleNotFoundException("회원이 생성한 일정이 아닙니다.");

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
                .destinationLatitude(findSchedule.getDestinationLatitude())
                .destinationLongitude(findSchedule.getDestinationLongitude())
                .friendsIdListDTO(friendsIdList)
                .build();
    }


    /**
     * Schedule arrived.
     *
     * @param requestScheduleArrived the request schedule arrived
     */
    public void scheduleArrived(RequestScheduleArrived requestScheduleArrived){

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

    public ResponseTodaySchedule getTodaySchedule(String memberId) {
        Optional<Member> optionalMember = memberRepository.findById(memberId);
        Member member = optionalMember.orElseThrow(() -> new UserNotFoundException("존재하지 않는 memberId입니다."));

        List<MemberSchedule> memberSchedules = memberScheduleRepository.findByMemberAndAcceptIsTrue(member);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        int todayScheduleCount = 0;

        for (MemberSchedule memberSchedule : memberSchedules) {
            Schedule schedule = memberSchedule.getSchedule();
            String start = schedule.getStart().format(formatter);
            String today = LocalDateTime.now().format(formatter);

            if (start.equals(today)) {
                todayScheduleCount++;
            }
        }

        ResponseTodaySchedule responseTodaySchedule = new ResponseTodaySchedule();
        responseTodaySchedule.setTodaySchedule(todayScheduleCount);

        return responseTodaySchedule;
    }
}