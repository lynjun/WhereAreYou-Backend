package com.example.whereareyou.repository;

import com.example.whereareyou.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * packageName    : project.whereareyou.repository
 * fileName       : MemberRepository
 * author         : pjh57
 * date           : 2023-09-16
 * description    : MemberRepository
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023-09-16        pjh57       최초 생성
 */
@Repository
public interface MemberRepository extends JpaRepository<Member, String> {

    Optional<Member>findByUserId(String userId);

    Optional<Object> findByEmail(String email);

}
