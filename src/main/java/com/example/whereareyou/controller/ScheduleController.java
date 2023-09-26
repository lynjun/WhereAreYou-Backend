package com.example.whereareyou.controller;

import com.example.whereareyou.service.ScheduleService;
import com.example.whereareyou.vo.request.schedule.RequestModifySchedule;
import com.example.whereareyou.vo.request.schedule.RequestSaveSchedule;
import com.example.whereareyou.vo.response.schedule.ResponseSaveSchedule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * packageName    : project.whereareyou.controller
 * fileName       : ScheduleController
 * author         : pjh57
 * date           : 2023-09-16
 * description    : 일정 Controller
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023-09-16        pjh57       최초 생성
 */
@RestController
@RequestMapping("/schedule")
public class ScheduleController {
    private final ScheduleService scheduleService;

    @Autowired
    public ScheduleController(ScheduleService scheduleService) {
        this.scheduleService = scheduleService;
    }

    /**
     * 일정 추가
     *
     * @param requestSaveSchedule the request save schedule
     * @return the response entity
     */
    @PostMapping()
    public ResponseEntity<ResponseSaveSchedule> saveSchedule(@RequestBody RequestSaveSchedule requestSaveSchedule){
        ResponseSaveSchedule savedSchedule = scheduleService.save(requestSaveSchedule);

        return ResponseEntity.status(HttpStatus.OK).body(savedSchedule);
    }

    /**
     * 일정 수정
     *
     * @param requestModifySchedule the request modify schedule
     * @return the response entity
     */
    @PutMapping()
    public ResponseEntity<Void> modifySchedule(@RequestBody RequestModifySchedule requestModifySchedule){
        scheduleService.modifySchedule(requestModifySchedule);

        return ResponseEntity.noContent().build();
    }
}