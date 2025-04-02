package com.lloll.myro.domain.schedule.application;

import com.lloll.myro.domain.schedule.dao.ScheduleRepository;
import com.lloll.myro.domain.schedule.dto.ScheduleDto;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ScheduleService {
    private final ScheduleRepository repository;

    @Transactional
    public void createSchedule(ScheduleDto createScheduleDto) {

    }
}
