package com.example.whereareyou.controller;

import com.example.whereareyou.dto.FriendInviteRequest;
import com.example.whereareyou.service.FriendService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/friend")
public class FriendController {

    private final FriendService friendService;

    @PostMapping("/request")
    public ResponseEntity<String> friendRequest(@RequestBody FriendInviteRequest request){
        friendService.friendRequest(request);

        return ResponseEntity.ok().body("친구 신청 완료");
    }

}

