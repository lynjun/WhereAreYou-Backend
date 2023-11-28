package com.example.whereareyou.FriendRequest.domain;

import com.example.whereareyou.member.domain.Member;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * packageName    : project.whereareyou.domain
 * fileName       : FriendRequest
 * author         : pjh57
 * date           : 2023-09-14
 * description    : 친구 요청 Entity
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023-09-14        pjh57       최초 생성
 */
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FriendRequest {
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(
            name = "uuid2",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(name = "friendRequest_id", updatable = false, nullable = false)
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_id")
    private Member senderId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receiver_id")
    private Member receiverId;

    private LocalDateTime createTime;
}