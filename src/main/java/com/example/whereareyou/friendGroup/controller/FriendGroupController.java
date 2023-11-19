package com.example.whereareyou.friendGroup.controller;

import com.example.whereareyou.friendGroup.request.RequestCreateGroup;
import com.example.whereareyou.friendGroup.response.ResponseCreateGroup;
import com.example.whereareyou.friendGroup.response.ResponseGetGroup;
import com.example.whereareyou.friendGroup.service.FriendGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * packageName    : com.example.whereareyou.friendGroup.controller
 * fileName       : FriendGroupController
 * author         : pjh57
 * date           : 2023-11-20
 * description    : 그룹 Controller
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023-11-20        pjh57       최초 생성
 */
@RestController
@RequestMapping("/group")
public class FriendGroupController {
    private final FriendGroupService friendGroupService;

    @Autowired
    public FriendGroupController(FriendGroupService friendGroupService) {
        this.friendGroupService = friendGroupService;
    }

    /**
     * 그룹 생성
     *
     * @param requestCreateGroup the request create group
     * @return the response entity
     */
    @PostMapping
    public ResponseEntity<ResponseCreateGroup> createGroup(@RequestBody RequestCreateGroup requestCreateGroup){
        ResponseCreateGroup responseCreateGroup = friendGroupService.createGroup(requestCreateGroup);

        return ResponseEntity.status(HttpStatus.OK).body(responseCreateGroup);
    }

    /**
     * 그룹 목록 조회
     *
     * @param ownerId the owner id
     * @return the response entity
     */
    @GetMapping()
    public ResponseEntity<List<ResponseGetGroup>> getGroup(@RequestParam String ownerId){
        List<ResponseGetGroup> responseGetGroups = friendGroupService.getGroup(ownerId);

        return ResponseEntity.status(HttpStatus.OK).body(responseGetGroups);
    }
}
