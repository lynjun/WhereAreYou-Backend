package com.example.whereareyou.schedule.repository;

import com.example.whereareyou.member.domain.Member;
import com.example.whereareyou.schedule.domain.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, String> {
    @Modifying
    @Query("UPDATE Schedule s SET s.appointmentTime = :appointmentTime, s.title = :title, s.place = :place, s.memo = :memo, s.creator = :creator WHERE s.id = :scheduleId")
    int updateSchedule(@Param("appointmentTime") LocalDateTime start,
                       @Param("title") String title,
                       @Param("place") String place,
                       @Param("memo") String memo,
                       @Param("creator") Member creator,
                       @Param("scheduleId") String scheduleId);

    List<Schedule> findByCreator(Member MemberId);

}