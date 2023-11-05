package com.example.whereareyou.service;

import com.example.whereareyou.domain.Friend;
import com.example.whereareyou.domain.FriendRequest;
import com.example.whereareyou.domain.Member;
import com.example.whereareyou.domain.Schedule;
import com.example.whereareyou.dto.*;
import com.example.whereareyou.exception.customexception.*;
import com.example.whereareyou.repository.FriendRepository;
import com.example.whereareyou.repository.FriendRequestRepository;
import com.example.whereareyou.repository.MemberRepository;
import com.example.whereareyou.repository.ScheduleRepository;
import com.example.whereareyou.vo.response.Friend.ResponseFriendIdList;
import com.example.whereareyou.vo.response.Friend.ResponseFriendList;
import com.example.whereareyou.vo.response.Friend.ResponseFriendRequestList;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
@RequiredArgsConstructor
public class FriendService {

    private final FriendRepository friendRepository;
    private final MemberRepository memberRepository;
    private final FriendRequestRepository friendRequestRepository;
    private final ScheduleRepository scheduleRepository;

    public void friendRequest(FriendRequestDto request) {
        Optional<Member> byId = memberRepository.findById(request.getMemberId());
        Member memberId = byId.orElseThrow(() ->
                new UserNotFoundException("아이디가 없습니다"));

        Optional<Member> byFriendId = memberRepository.findById(request.getFriendId());
        Member friendId = byFriendId.orElseThrow(() ->
                new UserNotFoundException("아이디가 없습니다."));

        // 본인 ID를 확인하고 예외 처리
        if (memberId == friendId) {
            throw new MemberIdCannotBeInFriendListException("본인 ID는 친구 요청을 보낼 수 없습니다.");
        }

        List<FriendRequest> byReceiverId = friendRequestRepository.findBySenderId(memberId);
        byReceiverId
                .stream()
                .filter(friendRequest -> Objects.equals(friendRequest.getSenderId().getId(), memberId.getId()) && friendRequest.getReceiverId().getId().equals(friendId.getId()))
                .forEach(friendRequest -> {
            throw new AlreadySent("이미 요청이 전송 되었습니다.");
        });

        List<Friend> byOwner = friendRepository.findByOwner(memberId);
        byOwner.stream().filter(friend ->
                friend.getOwner().getId().equals(memberId.getId()) && friend.getFriends().getId().equals(friendId.getId()))
                .forEach(friend -> {
            throw new AlreadyFriendsException("이미 친구 관계 입니다.");
        });

        FriendRequest friendRequest = FriendRequest.builder()
                .senderId(memberId)
                .receiverId(friendId)
                .build();

        friendRequestRepository.save(friendRequest);

    }

    public ResponseFriendRequestList friendRequestList(String memberId) {
        ResponseFriendRequestList responseFriendRequestList = new ResponseFriendRequestList();
        responseFriendRequestList.setFriendsRequestList(new ArrayList<>());

        TodaySchedule todaySchedule = new TodaySchedule();
        todaySchedule.setScheduleList(new ArrayList<>());


        Optional<Member> byId = memberRepository.findById(memberId);
        Member member = byId.orElseThrow(() -> new UserNotFoundException("존재하지 않는 아이디 입니다."));

        List<FriendRequest> id = friendRequestRepository.findByReceiverId(member);

        id.forEach(friendRequest -> {
            Member senderId = friendRequest.getSenderId();
            FriendRequestList friendRequestList = new FriendRequestList();
            friendRequestList.setFriendRequestId(friendRequest.getId());
            friendRequestList.setSenderId(senderId.getId());
            responseFriendRequestList.getFriendsRequestList().add(friendRequestList);
        });

        List<Schedule> byCreatorId = scheduleRepository.findByCreatorId(memberId);

        byCreatorId.forEach(schedule -> {
            LocalDateTime start = schedule.getStart();
            LocalDateTime now = LocalDateTime.now();

            String startTime = start.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            String nowTime = now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

            if (startTime.equals(nowTime)) {
                ScheduleList scheduleList = new ScheduleList();
                scheduleList.setScheduleId(schedule.getId());
                todaySchedule.getScheduleList().add(scheduleList);
            }
        });
        int todayScheduleCount = todaySchedule.getScheduleList().size();
        responseFriendRequestList.setTodaySchedule(todayScheduleCount);

        return responseFriendRequestList;
    }

    public void acceptFriend(AcceptFriend acceptFriend){
        Optional<Member> byMemberId = memberRepository.findById(acceptFriend.getMemberId());
        Member memberId = byMemberId.orElseThrow(() -> new UserNotFoundException("존재하지 않은 userId 입니다."));

        Optional<Member> byFriendId = memberRepository.findById(acceptFriend.getSenderId());
        Member friendId = byFriendId.orElseThrow(() -> new UserNotFoundException("존재하지 않은 FriendId 입니다."));

        Optional<FriendRequest> byId = friendRequestRepository.findById(acceptFriend.getFriendRequestId());
        FriendRequest friendRequest = byId.orElseThrow(() -> new FriendRequestNotFoundException("존재하지 않은 요청 입니다."));

        Friend owner = Friend.builder()
                .owner(memberId)
                .friends(friendId)
                .build();

        Friend friend = Friend.builder()
                .owner(friendId)
                .friends(memberId)
                .build();

        friendRepository.save(owner);
        friendRepository.save(friend);

        friendRequestRepository.delete(friendRequest);

    }

    public ResponseFriendList getFriendList(FriendDto friend){

        List<Member> byUserIdIn = memberRepository.findById(friend.getFriendId());
        ResponseFriendList responseFriendList = new ResponseFriendList();
        responseFriendList.setFriendsList(new ArrayList<>());

        byUserIdIn.forEach(member -> {
            FriendList friendList = new FriendList();
            friendList.setMemberId(member.getId());
            friendList.setUserName(member.getUserName());
            friendList.setProfileImage(member.getProfileImage());
            responseFriendList.getFriendsList().add(friendList);
        });

        return responseFriendList;
    }

    public void refuseFriend(RefuseFriend refuseFriend){
        Optional<FriendRequest> byId = friendRequestRepository.findById(refuseFriend.getFriendRequestId());

        FriendRequest friendRequest = byId.orElseThrow(() ->
                new FriendRequestNotFoundException("존재하지 않은 요청 입니다."));

        friendRequestRepository.delete(friendRequest);

    }

    public ResponseFriendIdList getFriendId(GetFriendId getFriendId){

        Optional<Member> byId = memberRepository.findById(getFriendId.getMemberId());

        Member member = byId.orElseThrow(() ->
                new UserNotFoundException("아이디가 존재하지 않습니다."));

        List<Friend> byOwner = friendRepository.findByOwner(member);

        ResponseFriendIdList responseFriendIdList = new ResponseFriendIdList();
        responseFriendIdList.setFriendsIdList(new ArrayList<>());

        if(byOwner != null) {
            byOwner.stream().map(Friend::getFriends).map(Member::getId).forEach(id -> {
                FriendIdList friendIdList = new FriendIdList();
                friendIdList.setFriendId(id);
                responseFriendIdList.getFriendsIdList().add(friendIdList);
            });
        }

        return responseFriendIdList;

    }
}
