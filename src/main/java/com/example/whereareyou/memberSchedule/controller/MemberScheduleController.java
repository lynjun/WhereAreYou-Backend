package com.example.whereareyou.memberSchedule.controller;

import com.example.whereareyou.memberSchedule.request.RequestModifyMemberSchedule;
import com.example.whereareyou.memberSchedule.request.RequestRefuseSchedule;
import com.example.whereareyou.memberSchedule.service.MemberScheduleService;
import com.example.whereareyou.schedule.request.RequestScheduleAccept;
import com.example.whereareyou.schedule.response.ResponseGroupInvite;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @PutMapping("/accept")
    public ResponseEntity<Boolean> scheduleAccept(@RequestBody RequestScheduleAccept requestScheduleAccept){
        memberScheduleService.scheduleAccept(requestScheduleAccept);

        return ResponseEntity.status(HttpStatus.OK).body(true);
    }

    @DeleteMapping("/refuse")
    public ResponseEntity<Void> refuseSchedule(@RequestBody RequestRefuseSchedule refuseSchedule){
        memberScheduleService.refuseSchedule(refuseSchedule);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/invite")
    public ResponseEntity<ResponseGroupInvite> getScheduleInvite(@RequestParam String memberId){
        ResponseGroupInvite test = memberScheduleService.getScheduleInvite(memberId);

        return ResponseEntity.ok().body(test);
    }
}
