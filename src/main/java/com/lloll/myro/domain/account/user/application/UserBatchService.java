package com.lloll.myro.domain.account.user.application;

import com.lloll.myro.domain.account.user.dao.UserRepository;
import com.lloll.myro.domain.account.user.domain.User;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserBatchService {
    private final UserRepository userRepository;
    private final UserServiceImpl userService;

    @Scheduled(cron = "0 30 * * * ?")
    public void cleanUpExpiredTokens() {
        userService.deleteExpiredTokens();
    }

    @Scheduled(cron = "0 0 0 * * ?")
    public void performRealDelete() {
        LocalDateTime threeMonthsAgo = LocalDateTime.now().minusMonths(3);

        List<User> usersToDelete = userRepository.findByDeleteDateBeforeAndDeleteDateIsNotNull(threeMonthsAgo);

        if (!usersToDelete.isEmpty()) {
            userRepository.deleteAll(usersToDelete);
        }
    }
}
