package com.example.whereareyou.member.repository;

import com.example.whereareyou.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
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

    Optional<Member> findByEmail(String email);

    @Query("SELECT m FROM Member m WHERE m.id IN :friendIds")
    List<Member> findById(List<String> friendIds);

}
