package com.example.whereareyou.schedule.service;

import com.example.whereareyou.global.constant.ExceptionConstant;
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
            throw new UserNotFoundException(FRIEND_NOT_FOUND_EXCEPTION_MESSAGE);
        }

        friends.add(creator);

        return friends;
    }

    private Schedule saveSchedule(RequestSaveSchedule requestSaveSchedule, Member creator) {
        Schedule schedule = Schedule.builder()
                .appointmentTime(requestSaveSchedule.getAppointmentTime())
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
        Member creator = returnMember(requestModifySchedule.getCreatorId());
        Schedule savedSchedule = returnSchedule(requestModifySchedule.getScheduleId());

        checkScheduleCreatedByCreator(savedSchedule, creator);

        updateSchedule(requestModifySchedule, creator, savedSchedule);
    }

    private Schedule returnSchedule(String scheduleId) {
        return scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new ScheduleNotFoundException(SCHEDULE_NOT_FOUND_EXCEPTION_MESSAGE));
    }

    private void checkScheduleCreatedByCreator(Schedule schedule, Member creator) {
        if (!schedule.getCreator().getId().equals(creator.getId()))
            throw new ScheduleNotFoundException(SCHEDULE_CREATOR_MISMATCH_EXCEPTION_MESSAGE);
    }

    private void updateSchedule(RequestModifySchedule requestModifySchedule, Member creator, Schedule schedule) {
        int updatedCount = scheduleRepository.updateSchedule(
                requestModifySchedule.getAppointmentTime(),
                requestModifySchedule.getTitle(),
                requestModifySchedule.getPlace(),
                requestModifySchedule.getMemo(),
                creator,
                schedule.getId()
        );

        if (updatedCount == UPDATE_QUERY_EXCEPTION_SIZE)
            throw new UpdateQueryException(UPDATE_QUERY_EXCEPTION_MESSAGE);
    }

    /**
     * Get monthly schedule
     *
     * @param memberId the member id
     * @param year     the year
     * @param month    the month
     * @return the response monthly schedule
     */
    public ResponseMonthlySchedule getMonthSchedule(String memberId, Integer year, Integer month) {
        Member member = returnMember(memberId);
        checkDate(month);

        List<MemberSchedule> acceptedMemberSchedules = memberScheduleRepository.findByMemberAndAcceptIsTrue(member);

        ResponseMonthlySchedule responseMonthlySchedule = setResponseMonthlySchedule(year, month);

        int daysInMonth = returnDaysInMonth(year, month);

        checkMonthlySchedule(daysInMonth, acceptedMemberSchedules, year, month, responseMonthlySchedule);

        return responseMonthlySchedule;
    }

    private void checkDate(Integer month) {
        if (month < START_OF_MONTH || month > END_OF_MONTH) {
            throw new InvalidYearOrMonthOrDateException(INVALID_MONTH_EXCEPTION);
        }
    }

    private ResponseMonthlySchedule setResponseMonthlySchedule(Integer year, Integer month) {
        ResponseMonthlySchedule responseMonthlySchedule = new ResponseMonthlySchedule();
        responseMonthlySchedule.setYear(year);
        responseMonthlySchedule.setMonth(month);
        responseMonthlySchedule.setSchedules(new ArrayList<>());

        return responseMonthlySchedule;
    }

    private int returnDaysInMonth(Integer year, Integer month) {
        YearMonth yearMonthObject = YearMonth.of(year, month);

        return yearMonthObject.lengthOfMonth();
    }

    private void checkMonthlySchedule(int daysInMonth, List<MemberSchedule> acceptedMemberSchedules, Integer year, Integer month, ResponseMonthlySchedule responseMonthlySchedule) {
        for (int day = START_OF_MONTH; day <= daysInMonth; day++) {
            MonthlyScheduleResponseDTO monthlyScheduleResponseDTO = new MonthlyScheduleResponseDTO();
            monthlyScheduleResponseDTO.setDate(day);

            int finalDay = day;
            List<Schedule> schedulesForTheDay = acceptedMemberSchedules.stream()
                    .map(MemberSchedule::getSchedule)
                    .filter(schedule -> {
                        LocalDate currentDate = LocalDate.of(year, month, finalDay);
                        LocalDate appointmentTime = schedule.getAppointmentTime().toLocalDate();
                        return currentDate.isEqual(appointmentTime);
                    })
                    .collect(Collectors.toList());

            monthlyScheduleResponseDTO.setHasSchedule(!schedulesForTheDay.isEmpty());
            monthlyScheduleResponseDTO.setAmountSchedule(schedulesForTheDay.size());
            responseMonthlySchedule.getSchedules().add(monthlyScheduleResponseDTO);
        }
    }

    /**
     * Delete schedule.
     *
     * @param requestDeleteSchedule the request delete schedule
     */
    public void deleteSchedule(RequestDeleteSchedule requestDeleteSchedule) {
        Member creator = returnMember(requestDeleteSchedule.getCreatorId());
        Schedule schedule = returnSchedule(requestDeleteSchedule.getScheduleId());

        checkScheduleCreatedByCreator(schedule, creator);

        scheduleRepository.deleteById(requestDeleteSchedule.getScheduleId());
    }

    /**
     * Get brief date schedule
     *
     * @param memberId the member id
     * @param year     the year
     * @param month    the month
     * @param date     the date
     * @return the response brief date schedule
     */
    public ResponseBriefDateSchedule getBriefDateSchedule(String memberId, Integer year, Integer month, Integer date) {
        Member member = returnMember(memberId);
        List<MemberSchedule> acceptedMemberSchedules = memberScheduleRepository.findByMemberAndAcceptIsTrue(member);

        ResponseBriefDateSchedule responseBriefDateSchedule = setResponseBriefDateSchedule();

        LocalDate givenDate = LocalDate.of(year, month, date);

        checkScheduleInGivenDate(givenDate, acceptedMemberSchedules, responseBriefDateSchedule);

        return responseBriefDateSchedule;
    }

    private ResponseBriefDateSchedule setResponseBriefDateSchedule() {
        ResponseBriefDateSchedule responseBriefDateSchedule = new ResponseBriefDateSchedule();
        responseBriefDateSchedule.setBriefDateScheduleDTOList(new ArrayList<>());

        return responseBriefDateSchedule;
    }

    private void checkScheduleInGivenDate(LocalDate givenDate, List<MemberSchedule> acceptedMemberSchedules, ResponseBriefDateSchedule responseBriefDateSchedule) {
        for (MemberSchedule memberSchedule : acceptedMemberSchedules) {
            Schedule schedule = memberSchedule.getSchedule();
            LocalDate appointmentTime = schedule.getAppointmentTime().toLocalDate();

            if (givenDate.isEqual(appointmentTime)) {
                BriefDateScheduleDTO briefDateScheduleDTO = new BriefDateScheduleDTO();
                briefDateScheduleDTO.setScheduleId(schedule.getId());
                briefDateScheduleDTO.setTitle(schedule.getTitle());
                briefDateScheduleDTO.setAppointmentTime(schedule.getAppointmentTime());
                responseBriefDateSchedule.getBriefDateScheduleDTOList().add(briefDateScheduleDTO);
            }
        }
    }

    /**
     * Get detail schedule
     *
     * @param memberId   the member id
     * @param scheduleId the schedule id
     * @return the response detail schedule
     */
    public ResponseDetailSchedule getDetailSchedule(String memberId, String scheduleId) {
        Member findMember = returnMember(memberId);
        Schedule findSchedule = returnScheduleIfMemberAccept(findMember, scheduleId);

        List<String> friendIds = extractFriendIds(findSchedule);
        List<String> arrivedFriendIs = extractArrivedFriendIds(findSchedule);

        return setResponseDetailSchedule(findSchedule, friendIds, arrivedFriendIs);
    }

    private Schedule returnScheduleIfMemberAccept(Member findMember, String scheduleId) {
        MemberSchedule acceptedMemberSchedule = memberScheduleRepository.findByMemberAndScheduleIdAndAcceptIsTrue(findMember, scheduleId)
                .orElseThrow(() -> new ScheduleNotFoundException(SCHEDULE_NOT_FOUND_OR_MEMBER_DIDNT_ACCEPT_EXCEPTION_MESSAGE));

        return acceptedMemberSchedule.getSchedule();
    }

    private List<String> extractFriendIds(Schedule findSchedule) {
        return findSchedule.getMemberScheduleList().stream()
                .filter(MemberSchedule::getAccept)
                .map(memberSchedule -> memberSchedule.getMember().getId())
                .collect(Collectors.toList());
    }

    private List<String> extractArrivedFriendIds(Schedule findSchedule) {
        return findSchedule.getMemberScheduleList().stream()
                .filter(MemberSchedule::getArrived)
                .map(memberSchedule -> memberSchedule.getMember().getId())
                .collect(Collectors.toList());
    }

    private ResponseDetailSchedule setResponseDetailSchedule(Schedule findSchedule, List<String> friendIds, List<String> arrivedFriendIds) {
        return ResponseDetailSchedule.builder()
                .creatorId(findSchedule.getCreator().getId())
                .appointmentTime(findSchedule.getAppointmentTime())
                .title(findSchedule.getTitle())
                .place(findSchedule.getPlace())
                .memo(findSchedule.getMemo())
                .destinationLatitude(findSchedule.getDestinationLatitude())
                .destinationLongitude(findSchedule.getDestinationLongitude())
                .friendsIdListDTO(friendIds)
                .arrivedFriendsIdList(arrivedFriendIds)
                .build();
    }

    /**
     * Schedule arrived.
     *
     * @param requestScheduleArrived the request schedule arrived
     */
    public void scheduleArrived(RequestScheduleArrived requestScheduleArrived) {

        Member arrivedMember = returnMember(requestScheduleArrived.getArrivedMemberId());
        Schedule findSchedule = returnSchedule(requestScheduleArrived.getScheduleId());

        checkIfMemberIsPartOfSchedule(findSchedule, arrivedMember);

        changeArrivedFalseToTrue(arrivedMember, findSchedule);
    }

    private void checkIfMemberIsPartOfSchedule(Schedule findSchedule, Member arrivedMember) {
        boolean isMemberPartOfSchedule = findSchedule.getMemberScheduleList()
                .stream()
                .anyMatch(ms -> ms.getMember().getId().equals(arrivedMember.getId()));
        if (!isMemberPartOfSchedule) {
            throw new UserNotFoundException(MEMBER_NOT_IN_SCHEDULE_EXCEPTION_MESSAGE);
        }
    }

    private void changeArrivedFalseToTrue(Member arrivedMember, Schedule findSchedule) {
        int update = memberScheduleRepository.setArrivedTrue(arrivedMember.getId(), findSchedule.getId());
        if (update == ZERO)
            throw new UpdateQueryException(UPDATE_QUERY_EXCEPTION_MESSAGE);
    }

    public ResponseTodaySchedule getTodaySchedule(String memberId) {
        Optional<Member> optionalMember = memberRepository.findById(memberId);
        Member member = optionalMember.orElseThrow(() -> new UserNotFoundException("존재하지 않는 memberId입니다."));

        List<MemberSchedule> memberSchedules = memberScheduleRepository.findByMemberAndAcceptIsTrue(member);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        int todayScheduleCount = 0;

        for (MemberSchedule memberSchedule : memberSchedules) {
            Schedule schedule = memberSchedule.getSchedule();
            String start = schedule.getAppointmentTime().format(formatter);
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