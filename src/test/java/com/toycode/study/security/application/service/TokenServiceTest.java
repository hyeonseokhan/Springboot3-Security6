package com.toycode.study.security.application.service;

import com.toycode.study.security.domain.Authority;
import com.toycode.study.security.domain.TokenInfo;
import com.toycode.study.security.domain.User;
import com.toycode.study.security.domain.User.Username;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class TokenServiceTest {

    @Autowired
    private TokenService tokenService;

    @Test
    @DisplayName("토큰 발급 테스트")
    void generate_token() {
        // Given
        tokenService.afterPropertiesSet();

        User user = new User(
            new Username("toycode"),
            "토이코드",
            "$2a$10$p3HID6F2/OcBi06VCRSCWO4dCfk5wdhjDheMrVOjoMh7NYcLzBGri",
            Authority.MANAGER);

        // When
        TokenInfo token = tokenService.createToken(user);

        // Then
        Assertions.assertNotNull(token.getToken().getValue());
        System.out.printf("==> JWT: %s\n", token.getToken().getValue());
    }
}