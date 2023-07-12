package com.toycode.study.security.application.service;

import com.toycode.study.security.application.port.dto.LoginRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;

@ExtendWith(MockitoExtension.class)
class LoginServiceTest {

    @InjectMocks
    private LoginService authenticationService;

    @Mock
    private AuthenticationManager authenticationManager;

    @Test
    @DisplayName("사용자 등록")
    void login_user() {
        // Given
        LoginRequest request = new LoginRequest(
            "toycode",
            "toycode1234");

        // When
        authenticationService.login(request);
    }
}