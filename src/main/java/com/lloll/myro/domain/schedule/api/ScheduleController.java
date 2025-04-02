package com.lloll.myro.domain.schedule.api;

import com.lloll.myro.domain.schedule.application.ScheduleService;
import com.lloll.myro.domain.schedule.dto.ScheduleDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/schedules")
@RequiredArgsConstructor

public class ScheduleController {
    private final ScheduleService service;

    @PostMapping("/id")
    public void createSchedule(@RequestBody ScheduleDto createScheduleDto) {
        service.createSchedule(createScheduleDto);
    }
}
