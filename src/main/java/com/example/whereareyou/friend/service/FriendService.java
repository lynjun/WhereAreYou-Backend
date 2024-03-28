package com.example.whereareyou.friend.service;

import com.example.whereareyou.FriendRequest.dto.*;
import com.example.whereareyou.friend.domain.Friend;
import com.example.whereareyou.FriendRequest.domain.FriendRequest;
import com.example.whereareyou.friend.dto.*;
import com.example.whereareyou.friend.exception.AlreadyFriendsException;
import com.example.whereareyou.friend.exception.AlreadySent;
import com.example.whereareyou.friend.exception.FriendRequestNotFoundException;
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
import com.example.whereareyou.friend.response.ResponseFriendIdList;
import com.example.whereareyou.friend.response.ResponseFriendList;
import com.example.whereareyou.friend.response.ResponseFriendRequestList;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static com.example.whereareyou.friend.constant.FriendConstant.*;

@Service
@RequiredArgsConstructor
public class FriendService {

    private final FriendRepository friendRepository;
    private final MemberRepository memberRepository;
    private final FriendRequestRepository friendRequestRepository;
    private final FirebaseCloudMessageService firebaseCloudMessageService;
    private final FcmTokenService fcmTokenService;
    private final MemberScheduleRepository memberScheduleRepository;
    private final FriendGroupMemberRepository friendGroupMemberRepository;

    public void friendRequest(FriendRequestDto request) {
        Member memberId = returnMember(request.getMemberId());

        Member friendId = returnMember(request.getFriendId());

        // 본인 ID를 확인하고 예외 처리
        validateMemberAndFriendId(memberId, friendId);

        checkIfRequestAlreadySent(memberId, friendId);

        checkIfAlreadyFriends(memberId, friendId);

        FriendRequest friendRequest = createFriendRequest(memberId, friendId);

        makeFcmMessage(memberId, friendId);

        friendRequestRepository.save(friendRequest);

    }

    private static FriendRequest createFriendRequest(Member memberId, Member friendId) {
        return FriendRequest.builder()
                .senderId(memberId)
                .receiverId(friendId)
                .createTime(LocalDateTime.now())
                .build();
    }

    private void checkIfAlreadyFriends(Member memberId, Member friendId) {
        friendRepository.findByOwner(memberId).stream()
                .filter(friend -> friend.getOwner().getId().equals(memberId.getId()) && friend.getFriends().getId().equals(friendId.getId()))
                .forEach(friend -> {
                    throw new AlreadyFriendsException("이미 친구 관계 입니다.");
                });
    }

    private void checkIfRequestAlreadySent(Member memberId, Member friendId) {
        friendRequestRepository.findBySenderId(memberId)
                .stream()
                .filter(friendRequest -> Objects.equals(friendRequest.getSenderId().getId(), memberId.getId()) && friendRequest.getReceiverId().getId().equals(friendId.getId()))
                .forEach(friendRequest -> {
                    throw new AlreadySent("이미 요청이 전송 되었습니다.");
                });
    }

    private static void validateMemberAndFriendId(Member memberId, Member friendId) {
        if (memberId == friendId) {
            throw new MemberIdCannotBeInFriendListException("본인 ID는 친구 요청을 보낼 수 없습니다.");
        }
    }

    private Member returnMember(String memberId) {
        return memberRepository.findById(memberId).orElseThrow(() ->
                new UserNotFoundException("존재하지 않는 아이디 입니다."));
    }


    public ResponseFriendRequestList friendRequestList(String memberId) {
        ResponseFriendRequestList responseFriendRequestList = setResponseFriendRequestList();

        Member member = returnMember(memberId);

        setResponseFriendRequestList(member, responseFriendRequestList);

        return responseFriendRequestList;
    }

    private static ResponseFriendRequestList setResponseFriendRequestList() {
        ResponseFriendRequestList responseFriendRequestList = new ResponseFriendRequestList();
        responseFriendRequestList.setFriendsRequestList(new ArrayList<>());
        return responseFriendRequestList;
    }

    private void setResponseFriendRequestList(Member member, ResponseFriendRequestList responseFriendRequestList) {
        List<FriendRequest> id = friendRequestRepository.findByReceiverId(member);
        id.forEach(friendRequest -> {
            Member senderId = friendRequest.getSenderId();
            FriendRequestList friendRequestList = new FriendRequestList();
            friendRequestList.setFriendRequestId(friendRequest.getId());
            friendRequestList.setSenderId(senderId.getId());
            friendRequestList.setCreateTime(friendRequest.getCreateTime());
            responseFriendRequestList.getFriendsRequestList().add(friendRequestList);
        });
    }

    public void acceptFriend(AcceptFriend acceptFriend) {
        Member memberId = returnMember(acceptFriend.getMemberId());

        Member friendId = returnMember(acceptFriend.getSenderId());

        FriendRequest friendRequest = returnFriendRequest(acceptFriend.getFriendRequestId());

        Friend owner = returnOwner(memberId, friendId);
        Friend friend = returnFriend(friendId, memberId);

        friendRepository.save(owner);
        friendRepository.save(friend);

        friendRequestRepository.delete(friendRequest);

        makeFcmMessage(owner);

    }

    private static Friend returnFriend(Member friendId, Member memberId) {
        return Friend.builder()
                .owner(friendId)
                .friends(memberId)
                .build();
    }

    private static Friend returnOwner(Member memberId, Member friendId) {
        return Friend.builder()
                .owner(memberId)
                .friends(friendId)
                .build();
    }

    private FriendRequest returnFriendRequest(String acceptFriend) {
        return friendRequestRepository.findById(acceptFriend).orElseThrow(() ->
                new FriendRequestNotFoundException("존재하지 않은 요청 입니다."));
    }

    public ResponseFriendList getFriendList(FriendDto friend) {

        List<Member> byUserIdIn = memberRepository.findById(friend.getFriendId());

        ResponseFriendList responseFriendList = setResponseFriendList();

        addMembersToFriendList(byUserIdIn, responseFriendList);

        return responseFriendList;
    }

    private static void addMembersToFriendList(List<Member> byUserIdIn, ResponseFriendList responseFriendList) {
        byUserIdIn.forEach(member -> {
            FriendList friendList = new FriendList();
            friendList.setMemberId(member.getId());
            friendList.setUserName(member.getUserName());
            friendList.setProfileImage(member.getProfileImage());
            responseFriendList.getFriendsList().add(friendList);
        });
    }

    private static ResponseFriendList setResponseFriendList() {
        ResponseFriendList responseFriendList = new ResponseFriendList();
        responseFriendList.setFriendsList(new ArrayList<>());
        return responseFriendList;
    }

    public void refuseFriend(RefuseFriend refuseFriend) {
        FriendRequest friendRequest = returnFriendRequest(refuseFriend.getFriendRequestId());

        friendRequestRepository.delete(friendRequest);

    }

    public ResponseFriendIdList getFriendId(GetFriendId getFriendId) {

        Member member = returnMember(getFriendId.getMemberId());

        List<Friend> byOwner = friendRepository.findByOwner(member);

        ResponseFriendIdList responseFriendIdList = setResponseFriendIdList();

        addFriends(byOwner, responseFriendIdList);

        return responseFriendIdList;

    }

    private static void addFriends(List<Friend> byOwner, ResponseFriendIdList responseFriendIdList) {
        if (byOwner != null) {
            byOwner.stream()
                    .map(Friend::getFriends)
                    .map(Member::getId)
                    .forEach(id -> responseFriendIdList.getFriendsIdList().add(id));
        }
    }

    @NotNull
    private static ResponseFriendIdList setResponseFriendIdList() {
        ResponseFriendIdList responseFriendIdList = new ResponseFriendIdList();
        responseFriendIdList.setFriendsIdList(new ArrayList<>());
        return responseFriendIdList;
    }

    public void deleteFriend(FriendDeleteDto friendDeleteDto) {

        //멤버 조회
        Member member = returnMember(friendDeleteDto.getMemberId());
        Member friend = returnMember(friendDeleteDto.getFriendId());

        //친구 삭제
        friendRepository.deleteByOwnerAndFriends(member, friend);
        friendRepository.deleteByOwnerAndFriends(friend, member);

        //일정에서 삭제
        List<MemberSchedule> byMemberAndFriend = memberScheduleRepository.findByCreatorAndMember(member, friend);
        List<MemberSchedule> byFriendAndMember = memberScheduleRepository.findByCreatorAndMember(friend, member);

        List<String> byMemberAndFriendIds = extractMemberScheduleIds(byMemberAndFriend);
        List<String> byFriendAndMemberIds = extractMemberScheduleIds(byFriendAndMember);

        memberScheduleRepository.deleteByAllId(byMemberAndFriendIds);
        memberScheduleRepository.deleteByAllId(byFriendAndMemberIds);

        //그룹에서 삭제

        List<FriendGroupMember> byOwnerAndFriend = friendGroupMemberRepository.findByOwnerAndFriend(member, friend);
        List<FriendGroupMember> byFriendAndOwner = friendGroupMemberRepository.findByOwnerAndFriend(friend, member);

        List<String> byOwnerAndFriendIds = extractFriendGroupMemberIds(byOwnerAndFriend);
        List<String> byFriendAndOwnerIds = extractFriendGroupMemberIds(byFriendAndOwner);

        friendGroupMemberRepository.deleteByAllId(byOwnerAndFriendIds);
        friendGroupMemberRepository.deleteByAllId(byFriendAndOwnerIds);
    }

    private static List<String> extractFriendGroupMemberIds(List<FriendGroupMember> byOwnerAndFriend) {
        return byOwnerAndFriend.stream()
                .map(FriendGroupMember::getId)
                .collect(Collectors.toList());
    }

    private static List<String> extractMemberScheduleIds(List<MemberSchedule> memberScheduleList) {
        return memberScheduleList.stream()
                .map(MemberSchedule::getId)
                .collect(Collectors.toList());
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
