package com.example.whereareyou.memberSchedule.controller;

import com.example.whereareyou.memberSchedule.request.RequestModifyMemberSchedule;
import com.example.whereareyou.memberSchedule.service.MemberScheduleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * packageName    : com.example.whereareyou.memberSchedule.controller
 * fileName       : MemberScheduleController
 * author         : pjh57
 * date           : 2023-11-05
 * description    : MemberScheduleController
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023-11-05        pjh57       최초 생성
 */
@RestController
@RequestMapping("/memberschedule")
public class MemberScheduleController {
    private final MemberScheduleService memberScheduleService;

    @Autowired
    public MemberScheduleController(MemberScheduleService memberScheduleService) {
        this.memberScheduleService = memberScheduleService;
    }

    @PutMapping()
    public ResponseEntity<Void> modifyMemberScheduleFriend(@RequestBody RequestModifyMemberSchedule requestModifyMemberSchedule){
        memberScheduleService.modifyMemberSchedule(requestModifyMemberSchedule);

        return ResponseEntity.noContent().build();
    }
}
