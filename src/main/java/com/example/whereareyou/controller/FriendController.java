package com.example.whereareyou.controller;

import com.example.whereareyou.dto.AcceptFriend;
import com.example.whereareyou.dto.FriendRequestDto;
import com.example.whereareyou.service.FriendService;
import com.example.whereareyou.vo.response.Friend.ResponseFriendList;
import com.example.whereareyou.vo.response.Friend.ResponseFriendRequestList;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping("/friendList")
    public ResponseEntity<ResponseFriendList> friendList(@RequestParam List<String> friendIds){
        ResponseFriendList responseFriendList = friendService.friendList(friendIds);


        return ResponseEntity.ok().body(responseFriendList);
    }

}

