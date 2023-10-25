package com.example.whereareyou.service;

import com.example.whereareyou.domain.Friend;
import com.example.whereareyou.domain.FriendRequest;
import com.example.whereareyou.domain.Member;
import com.example.whereareyou.dto.AcceptFriend;
import com.example.whereareyou.dto.FriendRequestDto;
import com.example.whereareyou.dto.FriendRequestList;
import com.example.whereareyou.exception.customexception.FriendRequestNotFoundException;
import com.example.whereareyou.exception.customexception.MemberMismatchException;
import com.example.whereareyou.exception.customexception.UserNotFoundException;
import com.example.whereareyou.repository.FriendRepository;
import com.example.whereareyou.repository.FriendRequestRepository;
import com.example.whereareyou.repository.MemberRepository;
import com.example.whereareyou.vo.response.Friend.ResponseFriendRequestList;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FriendService {

    private final FriendRepository friendRepository;
    private final MemberRepository memberRepository;
    private final FriendRequestRepository friendRequestRepository;

    public void friendRequest(FriendRequestDto request) {
        Member member = memberRepository.findByUserId(request.getUserId())
                .orElseThrow(() -> new UserNotFoundException("존재하지 않는 userId입니다.")); // sender

        List<String> requestList = request.getMemberIdList(); // 친구 요청 리스트 receiver

        // 본인 ID를 확인하고 예외 처리
        if (requestList.contains(member.getUserId())) {
            throw new RuntimeException("본인 ID는 친구 요청을 보낼 수 없습니다.");
        }

        List<Member> allById = memberRepository.findByUserIdIn(requestList); // 친구 요청 리스트 멤버 조회

        List<FriendRequest> friendRequests = allById.stream()
                .map(receiver -> FriendRequest.builder()
                        .senderId(member.getId())
                        .member(receiver)
                        .build())
                .collect(Collectors.toList());

        friendRequestRepository.saveAll(friendRequests);
    }

    public ResponseFriendRequestList friendRequestList(String userId) {
        Optional<Member> byUserId = memberRepository.findByUserId(userId);
        Member member = byUserId.orElseThrow(() -> new UserNotFoundException("존재하지 않는 userId 입니다."));

        List<FriendRequest> id = friendRequestRepository.findByMember(member);

        ResponseFriendRequestList responseFriendRequestList = new ResponseFriendRequestList();
        responseFriendRequestList.setFriendsRequestList(new ArrayList<>());


        for (FriendRequest friendRequest : id) {
            String senderId = friendRequest.getSenderId();
            Optional<Member> byId = memberRepository.findById(senderId);
            Member sender = byId.orElseThrow(() -> new UserNotFoundException("존재하지 않는 userId 입니다."));

            String senderUserName = sender.getUserName();
            String senderUserId = sender.getUserId();

            FriendRequestList friendRequestList = new FriendRequestList();
            friendRequestList.setFriendRequestId(friendRequest.getId());
            friendRequestList.setSenderUserName(senderUserName);
            friendRequestList.setSenderUserId(senderUserId);
            responseFriendRequestList.getFriendsRequestList().add(friendRequestList);
        }

        return responseFriendRequestList;
    }

    public void acceptFriend(AcceptFriend acceptFriend){
        Optional<Member> byUserId = memberRepository.findByUserId(acceptFriend.getUserId());
        Member userId = byUserId.orElseThrow(() -> new UserNotFoundException("존재하지 않은 userId 입니다."));

        Optional<Member> byFriendId = memberRepository.findByUserId(acceptFriend.getFriendId());
        Member friendId = byFriendId.orElseThrow(() -> new UserNotFoundException("존재하지 않은 FriendId 입니다."));

        Optional<FriendRequest> byId = friendRequestRepository.findById(acceptFriend.getFriendRequestId());
        FriendRequest friendRequest = byId.orElseThrow(() -> new FriendRequestNotFoundException("존재하지 않은 요청 입니다."));

        if(friendRequest.getMember()!=userId){
            throw new MemberMismatchException("회원 정보가 일치하지 않습니다");
        }

        friendRequestRepository.delete(friendRequest);

        Friend user = Friend.builder()
                .member(userId)
                .friend(acceptFriend.getUserId())
                .build();
        friendRepository.save(user);

        Friend friend = Friend.builder()
                .member(friendId)
                .friend(acceptFriend.getFriendId())
                .build();
        friendRepository.save(friend);

    }

}
