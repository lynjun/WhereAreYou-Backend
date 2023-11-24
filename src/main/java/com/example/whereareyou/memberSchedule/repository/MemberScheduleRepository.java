package com.example.whereareyou.memberSchedule.repository;

import com.example.whereareyou.member.domain.Member;
import com.example.whereareyou.memberSchedule.domain.MemberSchedule;
import com.example.whereareyou.schedule.domain.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * packageName    : project.whereareyou.repository
 * fileName       : MemberSchedule
 * author         : pjh57
 * date           : 2023-09-16
 * description    : MemberScheduleRepository
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023-09-16        pjh57       최초 생성
 */
@Repository
public interface MemberScheduleRepository extends JpaRepository<MemberSchedule, String> {
    void deleteAllBySchedule(Schedule schedule);

    @Modifying
    @Query("UPDATE MemberSchedule ms SET ms.accept = true WHERE ms.member.id = :memberId AND ms.schedule.id = :scheduleId")
    int setAcceptTrueForMemberAndSchedule(@Param("memberId") String memberId, @Param("scheduleId") String scheduleId);

    @Modifying
    @Query("UPDATE MemberSchedule ms SET ms.arrived = true WHERE ms.member.id = :memberId AND ms.schedule.id = :scheduleId")
    int setArrivedTrue(@Param("memberId") String memberId, @Param("scheduleId") String scheduleId);

    @Query("SELECT ms FROM MemberSchedule ms WHERE ms.schedule = :schedule")
    List<MemberSchedule> findMemberSchedulesBySchedule(@Param("schedule") Schedule schedule);

    List<MemberSchedule> findByMemberAndAcceptIsTrue(Member member);

    @Query("SELECT ms FROM MemberSchedule ms WHERE ms.member = :member AND ms.schedule.id = :scheduleId AND ms.accept = true")
    Optional<MemberSchedule> findByMemberAndScheduleIdAndAcceptIsTrue(@Param("member") Member member, @Param("scheduleId") String scheduleId);
    Optional<MemberSchedule> findByMemberAndSchedule(Member member, Schedule schedule);

    @Query("SELECT ms.schedule.id FROM MemberSchedule ms WHERE ms.member = :member AND ms.accept = false")
    List<String> findByMemberAndAcceptIsFalse(@Param("member") Member member);

}