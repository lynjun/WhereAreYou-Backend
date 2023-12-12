package com.example.whereareyou.friend.service;

import com.example.whereareyou.FriendRequest.dto.*;
import com.example.whereareyou.friend.domain.Friend;
import com.example.whereareyou.FriendRequest.domain.FriendRequest;
import com.example.whereareyou.friend.dto.*;
import com.example.whereareyou.friend.exception.AlreadyFriendsException;
import com.example.whereareyou.friend.exception.AlreadySent;
import com.example.whereareyou.friend.exception.FriendRequestNotFoundException;
import com.example.whereareyou.friendGroup.domain.FriendGroup;
import com.example.whereareyou.friendGroup.repository.FriendGroupRepository;
import com.example.whereareyou.friendGroupMember.domain.FriendGroupMember;
import com.example.whereareyou.friendGroupMember.repository.FriendGroupMemberRepository;
import com.example.whereareyou.global.domain.FcmToken;
import com.example.whereareyou.global.service.FcmTokenService;
import com.example.whereareyou.global.service.FirebaseCloudMessageService;
import com.example.whereareyou.member.domain.Member;
import com.example.whereareyou.member.exception.MemberIdCannotBeInFriendListException;
import com.example.whereareyou.member.exception.UserNotFoundException;
import com.example.whereareyou.friend.repository.FriendRepository;
import com.example.whereareyou.FriendRequest.repository.FriendRequestRepository;
import com.example.whereareyou.member.repository.MemberRepository;
import com.example.whereareyou.memberSchedule.domain.MemberSchedule;
import com.example.whereareyou.memberSchedule.repository.MemberScheduleRepository;
import com.example.whereareyou.schedule.domain.Schedule;
import com.example.whereareyou.schedule.repository.ScheduleRepository;
import com.example.whereareyou.friend.response.ResponseFriendIdList;
import com.example.whereareyou.friend.response.ResponseFriendList;
import com.example.whereareyou.friend.response.ResponseFriendRequestList;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

import static com.example.whereareyou.friend.constant.FriendConstant.*;

@Service
@RequiredArgsConstructor
public class FriendService {

    private final FriendRepository friendRepository;
    private final MemberRepository memberRepository;
    private final FriendRequestRepository friendRequestRepository;
    private final ScheduleRepository scheduleRepository;
    private final FirebaseCloudMessageService firebaseCloudMessageService;
    private final FcmTokenService fcmTokenService;
    private final MemberScheduleRepository memberScheduleRepository;
    private final FriendGroupRepository friendGroupRepository;
    private final FriendGroupMemberRepository friendGroupMemberRepository;

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
                .createTime(LocalDateTime.now())
                .build();

        makeFcmMessage(memberId,friendId);

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

        makeFcmMessage(owner);

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
                responseFriendIdList.getFriendsIdList().add(id);
            });
        }

        return responseFriendIdList;

    }
    public void deleteFriend(FriendDeleteDto friendDeleteDto){

        //멤버 조회
        Optional<Member> byMemberId = memberRepository.findById(friendDeleteDto.getMemberId());
        Member member = byMemberId.orElseThrow(() ->
                new UserNotFoundException("아이디가 존재하지 않습니다."));

        Optional<Member> byFriendId = memberRepository.findById(friendDeleteDto.getFriendId());
        Member friend = byFriendId.orElseThrow(() ->
                new UserNotFoundException("아이디가 존재하지 않습니다."));

        //친구 삭제
        friendRepository.deleteByOwnerAndFriends(member,friend);
        friendRepository.deleteByOwnerAndFriends(friend,member);

        //일정에서 삭제
        List<Schedule> byCreator1 = scheduleRepository.findByCreator(member);
        List<Schedule> byCreator = scheduleRepository.findByCreator(friend);

        List<MemberSchedule> memberScheduleByMember = memberScheduleRepository.findByScheduleAndMember(byCreator,member);
        List<MemberSchedule> memberScheduleByMember1 = memberScheduleRepository.findByScheduleAndMember(byCreator1,friend);

        memberScheduleRepository.deleteAll(memberScheduleByMember1);
        memberScheduleRepository.deleteAll(memberScheduleByMember);

        //그룹에서 삭제
        List<FriendGroup> byOwner = friendGroupRepository.findByOwner(member);

        List<FriendGroupMember> byFriendGroup = friendGroupMemberRepository.findByFriendGroup(byOwner, friend);

        friendGroupMemberRepository.deleteAll(byFriendGroup);

    }

    private void makeFcmMessage(Member friend, Member sender) {

        Optional<FcmToken> fcmTokenOpt = fcmTokenService.getTokenByMemberId(sender.getId());
        fcmTokenOpt.ifPresent(token -> {
            String body = friend.getUserName() + FCM_MESSAGE_TO + FRIEND_BODY;
            try {
                firebaseCloudMessageService.sendMessageTo(token.getTargetToken(), FRIEND_REQUEST_MESSAGE_TITLE, body);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private void makeFcmMessage(Friend friend) {

        Optional<FcmToken> fcmTokenOpt = fcmTokenService.getTokenByMemberId(friend.getOwner().getId());

        fcmTokenOpt.ifPresent(token -> {
            String body = friend.getFriends().getUserName() + FCM_MESSAGE_TO + ACCEPT_BODY;
            try {
                firebaseCloudMessageService.sendMessageTo(token.getTargetToken(), FRIEND_ACCEPT_MESSAGE_TITLE, body);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

}
