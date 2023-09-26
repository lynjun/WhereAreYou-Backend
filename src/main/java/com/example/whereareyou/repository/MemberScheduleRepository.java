package com.example.whereareyou.repository;

import com.example.whereareyou.domain.MemberSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

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

}
