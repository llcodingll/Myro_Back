package com.lloll.myro.schedule;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.lloll.myro.domain.schedule.application.ScheduleServiceImpl;
import com.lloll.myro.domain.schedule.dao.ScheduleRepository;
import com.lloll.myro.domain.schedule.domain.Schedule;
import com.lloll.myro.domain.schedule.dto.ScheduleDto;
import com.lloll.myro.domain.schedule.mapper.ScheduleMapper;
import java.time.LocalDateTime;
import java.util.Collections;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class ScheduleServiceImplTest {

    @InjectMocks
    private ScheduleServiceImpl scheduleService;
    @Mock
    private ScheduleRepository ScheduleRepository;
    @Mock
    private ScheduleMapper scheduleMapper;
    private ScheduleDto scheduleDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this); // Mock 초기화

        scheduleDto = ScheduleDto.builder()
                .title("Test Schedule")
                .description("Test Description")
                .build();

    }

    @Test
    @DisplayName("시간 충돌이 없는 일정 등록")
    void createScheduleTest_NoTimeConflict() {
        // given
        ScheduleDto scheduleDto = ScheduleDto.builder()
                .title("Test")
                .description("desc")
                .startDate(LocalDateTime.of(2025, 4, 21, 14, 0))
                .endDate(LocalDateTime.of(2025, 4, 21, 15, 0))
                .build();

        Schedule schedule = Schedule.builder()
                .title("Test")
                .description("desc")
                .startDate(scheduleDto.getStartDate())
                .endDate(scheduleDto.getEndDate())
                .build();

        // mock 설정
        when(ScheduleRepository.findTimeConflictsOnly(any(), any()))
                .thenReturn(Collections.emptyList());
        when(scheduleMapper.toEntity(eq(scheduleDto), any())).thenReturn(schedule);


        // when
        scheduleService.createSchedule(scheduleDto);

        // then
        verify(ScheduleRepository).save(schedule);
    }

    @Test
    @DisplayName("시간 충돌 있는 일정 등록 실패")
    void createScheduleTest_TimeConflict_Failure() {

    }

    @Test
    @DisplayName("일정 상태가 CUSTOM인 경우 customRule 있어야 등록 가능")
    void createScheduleTest_CustomRule() {

    }

    @Test
    @DisplayName("일정 상태가 CUSTOM인 경우 customRule이 없으면 등록 실패")
    void createScheduleTest_NOCustomRule_Failure() {

    }
}
