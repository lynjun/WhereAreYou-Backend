package com.example.whereareyou.repository;

import com.example.whereareyou.domain.Member;
import com.example.whereareyou.domain.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

/**
 * packageName    : project.whereareyou.repository
 * fileName       : ScheduleRepository
 * author         : pjh57
 * date           : 2023-09-16
 * description    : ScheduleRepository
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023-09-16        pjh57       최초 생성
 */
@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, String> {
    @Modifying
    @Query("UPDATE Schedule s SET s.start = :start, s.end = :end, s.title = :title, s.place = :place, s.memo = :memo, s.closed = :closed, s.creator = :creator WHERE s.id = :scheduleId")
    int updateSchedule(@Param("start") LocalDateTime start,
                       @Param("end") LocalDateTime end,
                       @Param("title") String title,
                       @Param("place") String place,
                       @Param("memo") String memo,
                       @Param("closed") Boolean closed,
                       @Param("creator") Member creator,
                       @Param("scheduleId") String scheduleId);

    @Modifying
    @Query("UPDATE Schedule s SET s.closed = true WHERE s.id = :scheduleId")
    int closeSchedule(@Param("scheduleId") String scheduleId);

}