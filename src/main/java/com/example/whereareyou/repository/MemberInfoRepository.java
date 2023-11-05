package com.example.whereareyou.repository;

import com.example.whereareyou.domain.MemberInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * packageName    : com.example.whereareyou.repository
 * fileName       : MemberInfoRepository
 * author         : pjh57
 * date           : 2023-10-11
 * description    : 위도 경도 Repository
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023-10-11        pjh57       최초 생성
 */
@Repository
public interface MemberInfoRepository extends JpaRepository<MemberInfo, String> {
    Optional<MemberInfo> findByMemberId(String memberId);

    @Modifying
    @Query("update MemberInfo m set m.latitude = :latitude, m.longitude = :longitude where m.memberId = :memberId")
    int updateMemberInfoByMemberId(@Param("latitude") Double latitude, @Param("longitude") Double longitude, @Param("memberId") String memberId);

    @Transactional
    void deleteByMemberId(String memberId);
}
