package com.example.whereareyou.schedule.controller;

import com.example.whereareyou.schedule.request.*;
import com.example.whereareyou.schedule.service.ScheduleService;
import com.example.whereareyou.schedule.response.ResponseBriefDateSchedule;
import com.example.whereareyou.schedule.response.ResponseDetailSchedule;
import com.example.whereareyou.schedule.response.ResponseMonthlySchedule;
import com.example.whereareyou.schedule.response.ResponseSaveSchedule;
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

    /**
     * 월별 일정 정보
     *
     * @param memberId the member id
     * @param year     the year
     * @param month    the month
     * @return the response entity
     */
    @GetMapping("/month")
    public ResponseEntity<ResponseMonthlySchedule> getMonthSchedule(@RequestParam
                                                                    String memberId,
                                                                    Integer year,
                                                                    Integer month){
        ResponseMonthlySchedule responseMonthlySchedule
                = scheduleService.getMonthSchedule(memberId, year, month);

        return ResponseEntity.status(HttpStatus.OK).body(responseMonthlySchedule);
    }

    /**
     * 일정 삭제
     *
     * @param requestDeleteSchedule the request delete schedule
     * @return the response entity
     */
    @DeleteMapping()
    public ResponseEntity<Void> deleteSchedule(@RequestBody RequestDeleteSchedule requestDeleteSchedule){
        scheduleService.deleteSchedule(requestDeleteSchedule);

        return ResponseEntity.noContent().build();
    }

    /**
     * 일별 일정 간략 정보
     *
     * @param memberId the member id
     * @param year     the year
     * @param month    the month
     * @param date     the date
     * @return the response entity
     */
    @GetMapping("/date")
    public ResponseEntity<ResponseBriefDateSchedule> getBriefDateSchedule(@RequestParam
                                                                          String memberId,
                                                                          Integer year,
                                                                          Integer month,
                                                                          Integer date){
        ResponseBriefDateSchedule briefDateSchedule = scheduleService.getBriefDateSchedule(memberId, year, month, date);

        return ResponseEntity.status(HttpStatus.OK).body(briefDateSchedule);
    }

    /**
     * 일별 일정 상세 정보
     *
     * @param memberId   the member id
     * @param scheduleId the schedule id
     * @return the response entity
     */
    @GetMapping("/details")
    public ResponseEntity<ResponseDetailSchedule> getDetailSchedule(@RequestParam
                                                                    String memberId,
                                                                    String scheduleId){
        ResponseDetailSchedule responseDetailSchedule = scheduleService.getDetailSchedule(memberId, scheduleId);

        return ResponseEntity.status(HttpStatus.OK).body(responseDetailSchedule);
    }

    /**
     * 일정 수락
     *
     * @param requestScheduleAccept the request schedule accept
     * @return the response entity
     */
    @PutMapping("/accept")
    public ResponseEntity<Boolean> scheduleAccept(@RequestBody RequestScheduleAccept requestScheduleAccept){
        scheduleService.scheduleAccept(requestScheduleAccept);

        return ResponseEntity.status(HttpStatus.OK).body(true);
    }

    /**
     * 일정 종료
     *
     * @param requestScheduleClosed the request schedule closed
     * @return the response entity
     */
    @PutMapping("/closed")
    public ResponseEntity<Boolean> scheduleClosed(@RequestBody RequestScheduleClosed requestScheduleClosed){
        scheduleService.scheduleClosed(requestScheduleClosed);

        return ResponseEntity.status(HttpStatus.OK).body(true);
    }

    /**
     * 도착 여부
     *
     * @param requestScheduleArrived the request schedule arrived
     * @return the response entity
     */
    @PutMapping("/arrived")
    public ResponseEntity<Boolean> scheduleArrived(@RequestBody RequestScheduleArrived requestScheduleArrived){
        scheduleService.scheduleArrived(requestScheduleArrived);

        return ResponseEntity.status(HttpStatus.OK).body(true);
    }
}