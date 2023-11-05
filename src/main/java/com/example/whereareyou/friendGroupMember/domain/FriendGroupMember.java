package com.example.whereareyou.friendGroupMember.domain;

import com.example.whereareyou.friendGroup.domain.FriendGroup;
import com.example.whereareyou.member.domain.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * packageName    : com.example.whereareyou.domain
 * fileName       : FriendGroupMember
 * author         : pjh57
 * date           : 2023-10-20
 * description    : 친구 그룹에 들어 있는 친구 목록
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023-10-20        pjh57       최초 생성
 */
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FriendGroupMember {
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(
            name = "uuid2",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(name = "frined_group_member_id", updatable = false, nullable = false)
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "friend_group_id")
    private FriendGroup friendGroup;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "friend_id")
    private Member member;
}
