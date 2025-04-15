package com.lloll.myro.schedule;

import com.lloll.myro.domain.schedule.application.ScheduleServiceImpl;
import com.lloll.myro.domain.schedule.dao.ScheduleRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class ScheduleServiceImplTest {

    @InjectMocks
    private ScheduleServiceImpl scheduleService;
    @Mock
    private ScheduleRepository ScheduleRepository;

    @Test
    @DisplayName("일정 등록")
    void createScheduleTest() {
        //given


    }
    @Test
    @DisplayName("제목 없는 일정 등록 불가")
    void createScheduleNoTitleFailTest() {

    }
    @Test
    @DisplayName("내용 없는 일정 등록 불가")
    void createScheduleNoDescriptionFailTest() {

    }
}
