package com.example.whereareyou.memberInfo.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;


/**
 * packageName    : com.example.whereareyou.domain
 * fileName       : MemberInfo
 * author         : pjh57
 * date           : 2023-09-27
 * description    : 회원 위도/경도 정보
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023-09-27        pjh57       최초 생성
 */
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class MemberInfo {
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(
            name = "uuid2",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(name = "member_info_id", updatable = false, nullable = false)
    private String id;

    private Double latitude;
    private Double longitude;

    private String memberId;
}
