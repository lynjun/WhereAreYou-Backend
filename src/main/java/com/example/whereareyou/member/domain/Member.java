package com.example.whereareyou.member.domain;

import com.example.whereareyou.FriendRequest.domain.FriendRequest;
import com.example.whereareyou.friend.domain.Friend;
import com.example.whereareyou.friendBookmark.domain.FriendBookmark;
import com.example.whereareyou.friendGroup.domain.FriendGroup;
import com.example.whereareyou.memberSchedule.domain.MemberSchedule;
import com.example.whereareyou.schedule.domain.Schedule;
import com.example.whereareyou.searchHistory.domain.SearchHistory;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * packageName    : project.whereareyou.domain
 * fileName       : Member
 * author         : pjh57
 * date           : 2023-09-14
 * description    : 회원 Entity
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023-09-14        pjh57       최초 생성
 */
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
@Table(name = "member")
public class Member {
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(
            name = "uuid2",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(name = "member_id", updatable = false, nullable = false)
    private String id;

    private String userName;
    private String userId;
    private String password;
    private String email;
    private String profileImage;

    @OneToMany(mappedBy = "receiverId", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FriendRequest> receiverRequestList = new ArrayList<>();

    @OneToMany(mappedBy = "senderId", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FriendRequest> senderRequestList = new ArrayList<>();

    @OneToMany(mappedBy = "friends", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Friend> friendList = new ArrayList<>();

    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Friend> ownerList = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FriendGroup> groupList = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FriendBookmark> friendBookmarkList = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SearchHistory> searchHistoryList = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MemberSchedule> memberScheduleList = new ArrayList<>();

    @OneToMany(mappedBy = "creator", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Schedule> scheduleList = new ArrayList<>();

    public Member(String id, String userName, String userId, String password, String email, String profileImage) {
        this.id = id;
        this.userName = userName;
        this.userId = userId;
        this.password = password;
        this.email = email;
        this.profileImage = profileImage;
    }
}
