package com.lloll.myro.domain.schedule.domain;

import com.lloll.myro.domain.user.domain.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.util.Date;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Schedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "scheduleId")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId")
    private User userId;

    private String title;
    private String description;
    private Date startTime;
    private Date endTime;
    private boolean isRecurring; //반복 일정
    private String recurrenceRule; //반복 일정 규칙(ex: "FREQ=WEEKLY;BYDAY=MO,WE,FR")

    private String scheduleStatus; //'PENDING', 'CONFIRMED', 'CANCELLED'

    private Date startDate;
    private Date endDate;

}
