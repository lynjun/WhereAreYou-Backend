package com.example.whereareyou.global.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * packageName    : com.example.whereareyou.global.domain
 * fileName       : FcmToken
 * author         : pjh57
 * date           : 2023-11-20
 * description    : 토큰 관리 Entity
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023-11-20        pjh57       최초 생성
 */
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
@Table(name = "fcmToken")
public class FcmToken {
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(
            name = "uuid2",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(name = "fcmToken_id", updatable = false, nullable = false)
    private String id;

    private String memberId;

    private String targetToken;
}
