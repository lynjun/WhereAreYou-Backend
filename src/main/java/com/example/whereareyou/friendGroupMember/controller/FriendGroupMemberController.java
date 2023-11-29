package com.example.whereareyou.friendGroupMember.controller;

import com.example.whereareyou.friendGroupMember.request.RequestModifyGroupMember;
import com.example.whereareyou.friendGroupMember.service.FriendGroupMemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/group/member")
public class FriendGroupMemberController {
    private final FriendGroupMemberService friendGroupMemberService;

    @Autowired
    public FriendGroupMemberController(FriendGroupMemberService friendGroupMemberService) {
        this.friendGroupMemberService = friendGroupMemberService;
    }

    @PutMapping()
    public ResponseEntity<Void> modifyGroupMember(@RequestBody RequestModifyGroupMember requestModifyGroupMember){
        friendGroupMemberService.modifyGroupMember(requestModifyGroupMember);

        return ResponseEntity.noContent().build();
    }
}
