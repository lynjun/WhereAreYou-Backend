package com.example.whereareyou.controller;

import com.example.whereareyou.dto.AcceptFriend;
import com.example.whereareyou.dto.FriendRequestDto;
import com.example.whereareyou.service.FriendService;
import com.example.whereareyou.vo.response.Friend.ResponseFriendRequestList;
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

        return ResponseEntity.ok().body("친구 신청 완료");
    }

    @GetMapping("/requestList")
    public ResponseEntity<ResponseFriendRequestList> friendRequestList(@RequestParam("userId") String userId){
        ResponseFriendRequestList responseFriendRequestList = friendService.friendRequestList(userId);

        return ResponseEntity.ok().body(responseFriendRequestList);
    }

    @PostMapping("/accept")
    public ResponseEntity<String> accept(@RequestBody AcceptFriend acceptFriend){
        friendService.acceptFriend(acceptFriend);

        return ResponseEntity.ok().body("친구 수락 완료");
    }

}

