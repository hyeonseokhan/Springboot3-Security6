package com.toycode.study.security.application.service;

import com.toycode.study.security.application.port.dto.UserRegistrationRequest;
import com.toycode.study.security.application.port.out.UserDeletePort;
import com.toycode.study.security.application.port.out.UserPersistencePort;
import com.toycode.study.security.domain.Authority;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

class UserServiceTest {

    private final UserPersistencePort userSavePort = Mockito.mock(UserPersistencePort.class);

    private final UserDeletePort userDeletePort = Mockito.mock(UserDeletePort.class);

    private final UserService userService =
        new UserService(userSavePort, userDeletePort, new BCryptPasswordEncoder());

    @Test
    @DisplayName("사용자 등록")
    void register_user() {
        // Given
        UserRegistrationRequest request = new UserRegistrationRequest(
            "toycode",
            "토이코드",
            "toycode1234",
            Authority.MANAGER);

        // When
        userService.registerUser(request);
    }
}