package com.example.whereareyou.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * packageName    : project.whereareyou.domain
 * fileName       : Schedule
 * author         : pjh57
 * date           : 2023-09-14
 * description    : 일정 Entity
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
@Table(name = "schedule")
public class Schedule {
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(
            name = "uuid2",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(name = "schedule_id", updatable = false, nullable = false)
    private String id;

    private LocalDateTime start;
    private LocalDateTime end;

    private String title;
    private String place;
    private String memo;

    private Boolean closed;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member creator;

    @OneToMany(mappedBy = "schedule", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MemberSchedule> memberScheduleList = new ArrayList<>();
}
