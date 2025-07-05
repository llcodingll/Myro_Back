package com.lloll.myro.domain.eventLog.domain;

import com.lloll.myro.domain.schedule.domain.Schedule;
import com.lloll.myro.domain.account.user.domain.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class EventLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String eventType; //'CREATE', 'UPDATE', 'DELETE'

    @NotNull
    @Column(name = "eventData", columnDefinition = "json")
    @JdbcTypeCode(SqlTypes.JSON)
    private List<String> eventData;

    private boolean isRead;
    private Date createdAt;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "schedule_id")
    private Schedule scheduleId;
}
