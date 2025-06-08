package com.lloll.myro.domain.account.user.application;

import com.lloll.myro.domain.account.admin.application.response.UserCountLoginResponse;
import com.lloll.myro.domain.account.user.dao.UserActivityLogRepository;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserActivityLogService {

    private final UserActivityLogRepository userActivityLogRepository;

    public UserCountLoginResponse getDAU(LocalDate date) {
        return userActivityLogRepository.findDailyActiveUsers(date);
    }

    public List<UserCountLoginResponse> getWAU(LocalDate date) {
        LocalDate startDate = date.minusDays(6);
        return userActivityLogRepository.findWeeklyActiveUsers(startDate, date);
    }
}