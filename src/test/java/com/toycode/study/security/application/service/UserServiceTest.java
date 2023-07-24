package com.toycode.study.security.application.service;

import com.toycode.study.security.application.port.dto.UserRegistrationRequest;
import com.toycode.study.security.application.port.out.UserPersistencePort;
import com.toycode.study.security.domain.Authority;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

class UserServiceTest {

    private final UserPersistencePort userSavePort = Mockito.mock(UserPersistencePort.class);

    private final UserService userService =
        new UserService(userSavePort, new BCryptPasswordEncoder());

    @Test
    @DisplayName("사용자 등록")
    void register_user() {
        // Given
        UserRegistrationRequest request = new UserRegistrationRequest(
            "admin",
            "최고 관리자",
            "admin",
            Authority.ROOT_MANAGER);

        // When
        userService.registerUser(request);
    }
}