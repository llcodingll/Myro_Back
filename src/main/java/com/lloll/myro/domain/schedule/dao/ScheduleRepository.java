package com.lloll.myro.domain.schedule.dao;

import com.lloll.myro.domain.schedule.domain.Schedule;
import java.time.LocalDateTime;
import java.util.List;
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
}
