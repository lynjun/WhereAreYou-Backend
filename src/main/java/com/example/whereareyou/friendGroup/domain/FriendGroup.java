package com.example.whereareyou.friendGroup.domain;

import com.example.whereareyou.friendGroupMember.domain.FriendGroupMember;
import com.example.whereareyou.member.domain.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * packageName    : way.whereareyou.domain
 * fileName       : FriendGroup
 * author         : pjh57
 * date           : 2023-09-14
 * description    : 친구 그룹 Entity
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023-09-14        pjh57       최초 생성
 */
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FriendGroup {
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(
            name = "uuid2",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(name = "group_id", updatable = false, nullable = false)
    private String id;

    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id")
    private Member owner;

    @OneToMany(mappedBy = "friendGroup", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FriendGroupMember> friendGroupMembers = new ArrayList<>();

}