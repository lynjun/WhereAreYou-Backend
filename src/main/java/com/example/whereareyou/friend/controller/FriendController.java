package com.example.whereareyou.friend.controller;

import com.example.whereareyou.FriendRequest.dto.FriendRequestDto;
import com.example.whereareyou.FriendRequest.dto.RefuseFriend;
import com.example.whereareyou.FriendRequest.dto.AcceptFriend;
import com.example.whereareyou.friend.dto.FriendDto;
import com.example.whereareyou.friend.dto.GetFriendId;
import com.example.whereareyou.friend.service.FriendService;
import com.example.whereareyou.friend.response.ResponseFriendIdList;
import com.example.whereareyou.friend.response.ResponseFriendList;
import com.example.whereareyou.friend.response.ResponseFriendRequestList;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/friend")
public class FriendController {

    private final FriendService friendService;

    @PostMapping("/request")
    public ResponseEntity<String> friendRequest(@RequestBody FriendRequestDto request){
        friendService.friendRequest(request);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/requestList")
    public ResponseEntity<ResponseFriendRequestList> friendRequestList(@RequestParam("memberId") String memberId){
        ResponseFriendRequestList responseFriendRequestList = friendService.friendRequestList(memberId);

        return ResponseEntity.ok().body(responseFriendRequestList);
    }

    @PostMapping("/accept")
    public ResponseEntity<String> accept(@RequestBody AcceptFriend acceptFriend){
        friendService.acceptFriend(acceptFriend);

        return ResponseEntity.noContent().build();
    }

    @PostMapping("/friendList")
    public ResponseEntity<ResponseFriendList> getFriendList(@RequestBody FriendDto friend){
        ResponseFriendList responseFriendList = friendService.getFriendList(friend);


        return ResponseEntity.ok().body(responseFriendList);
    }

    @PostMapping("/refuse")
    public ResponseEntity<Void> refuseFriend(@RequestBody RefuseFriend refuseFriend){

        friendService.refuseFriend(refuseFriend);

        return ResponseEntity.noContent().build();
    }

    @PostMapping("/friendIds")
    private ResponseEntity<ResponseFriendIdList> getFriendIds(@RequestBody GetFriendId getFriendId){

        ResponseFriendIdList friendId = friendService.getFriendId(getFriendId);

        return ResponseEntity.ok().body(friendId);
    }

}

