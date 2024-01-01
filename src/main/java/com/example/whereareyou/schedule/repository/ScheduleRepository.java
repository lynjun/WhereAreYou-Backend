package com.example.whereareyou.schedule.repository;

import com.example.whereareyou.member.domain.Member;
import com.example.whereareyou.schedule.domain.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, String> {
    @Modifying
    @Query("UPDATE Schedule s SET s.appointmentTime = :appointmentTime, s.title = :title, s.place = :place, s.memo = :memo, s.creator = :creator, s.destinationLatitude = :destinationLatitude, s.destinationLongitude = :destinationLongitude WHERE s.id = :scheduleId")
    int updateSchedule(@Param("appointmentTime") LocalDateTime start,
                       @Param("title") String title,
                       @Param("place") String place,
                       @Param("memo") String memo,
                       @Param("creator") Member creator,
                       @Param("destinationLatitude") Double destinationLatitude,
                       @Param("destinationLongitude") Double destinationLongitude,
                       @Param("scheduleId") String scheduleId);

    @Transactional
    @Modifying
    @Query("delete from Schedule s where s.creator = :member")
    void deleteByCreator(Member member);

    @Query("SELECT s FROM Schedule s WHERE s.creator = :member")
    List<Schedule> findSchedulesByMember(@Param("member") Member member);

}