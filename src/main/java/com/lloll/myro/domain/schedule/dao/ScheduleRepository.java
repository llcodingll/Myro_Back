package com.lloll.myro.domain.schedule.dao;

import com.lloll.myro.domain.schedule.domain.Schedule;
import com.lloll.myro.domain.schedule.domain.ScheduleStatus;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
    @Query("SELECT s FROM Schedule s WHERE DATE(s.startDate) = DATE(:startDate) " +
            "AND ((:startDate < s.endDate AND :endDate > s.startDate))")
    List<Schedule> findConflictingSchedules(@Param("startDate") LocalDateTime startDate,
                                            @Param("endDate") LocalDateTime endDate);

    // DELETED 아닌 모든 일정 조회
    List<Schedule> findByScheduleStatusNot(ScheduleStatus status);

    // DELETED 제외 + 특정 id 일정 조회
    Optional<Schedule> findByIdAndScheduleStatusNot(Long id, ScheduleStatus status);
}
