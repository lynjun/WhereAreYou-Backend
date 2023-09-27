package com.example.whereareyou.repository;

import com.example.whereareyou.domain.Member;
import com.example.whereareyou.domain.MemberSchedule;
import com.example.whereareyou.domain.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

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
}