package com.lloll.myro.domain.account.user.application;

import com.lloll.myro.domain.account.jwt.RefreshTokenRepository;
import com.lloll.myro.domain.account.jwt.TokenProvider;
import com.lloll.myro.domain.account.user.dao.UserActivityLogRepository;
import com.lloll.myro.domain.account.user.dao.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@SpringBootTest
public abstract class UserServiceTestSupport {

    @Autowired
    protected UserServiceImpl userService;

    @MockitoBean
    protected UserRepository userRepository;

    @MockitoBean
    protected TokenProvider tokenProvider;

    @MockitoBean
    protected RefreshTokenRepository refreshTokenRepository;

    @MockitoBean
    protected UserActivityLogRepository userActivityLogRepository;

    protected static void setField(Object target, String fieldName, Object value) {
        try {
            java.lang.reflect.Field field = target.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(target, value);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
}
