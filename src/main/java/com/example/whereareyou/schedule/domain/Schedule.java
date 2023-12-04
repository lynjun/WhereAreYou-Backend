package com.example.whereareyou.schedule.domain;

import com.example.whereareyou.memberSchedule.domain.MemberSchedule;
import com.example.whereareyou.member.domain.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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

    private LocalDateTime appointmentTime;
    private LocalDateTime createTime;

    private String title;
    private String place;
    private String memo;

    private Double destinationLatitude;
    private Double destinationLongitude;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member creator;

    @OneToMany(mappedBy = "schedule", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MemberSchedule> memberScheduleList = new ArrayList<>();
}
